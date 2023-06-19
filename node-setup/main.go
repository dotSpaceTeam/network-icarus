package main

import (
	"errors"
	"fmt"
	"github.com/common-nighthawk/go-figure"
	"github.com/fatih/color"
	"github.com/manifoldco/promptui"
	"log"
	"os"
	"os/exec"
	"regexp"
)

const CA_CN = "dotspace.team"
const CA_O = "dotSpace Development"
const CA_OU = "Icarus Network"
const CA_C = "DE"

var fqdn_pattern = regexp.MustCompile(`(?i)^([a-z0-9]|[a-z0-9][a-z0-9\-]{0,61}[a-z0-9])(\.([a-z0-9]|[a-z0-9][a-z0-9\-]{0,61}[a-z0-9]))*$`)

func main() {
	header()
	checkRequirements()

	color.Yellow("To quickly setup an icarus environment, please answer the following questions.")

	selection := promptui.Select{
		Label: "What do you want to do?",
		Items: []string{"Quick setup", "Generate a new server certificate", "Generate client certificates"},
	}
	actionIdx, _, err := selection.Run()
	if err != nil {
		fmt.Printf("Prompt failed %v\n", err)
		return
	}

	var caKeyPasswd string
	var fqdn string
	var keystorePasswd string
	var truststorePasswd string

	caCertFile := "rootCA.crt"
	caKeyFile := "rootCA.key"

	if actionIdx == 0 {
		var location string

		fmt.Println()
		color.Yellow("What project should Icarus be used for?")
		prompt := promptui.Prompt{
			Label: "Enter project name",
			Validate: func(input string) error {
				if len(input) < 4 {
					return errors.New("name must have 4 or more letters")
				}
				return nil
			},
			Default: "dotSpace",
		}

		result, err := prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}
		location = result

		// CA PW
		fmt.Println()
		color.Yellow("Please enter a secure password for the certificate authority!")
		color.HiRed("This is crucial, please use a strong combination of characters.")
		prompt = promptui.Prompt{
			Label: "Enter CA pass phrase",
			Mask:  '*',
			Validate: func(input string) error {
				if len(input) <= 6 && len(input) > 1024 {
					return errors.New("you must type in 4 to 1024 characters")
				}
				return nil
			},
		}

		result, err = prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}
		caKeyPasswd = result

		// Verify CA PW
		prompt = promptui.Prompt{
			Label: "Verifying - Enter CA pass phrase",
			Mask:  '*',
			Validate: func(input string) error {
				if input != caKeyPasswd {
					return errors.New("pass phrases dont match")
				}
				return nil
			},
		}

		result, err = prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}

		generateRootCA(location, caKeyPasswd)
	}

	if !fileExists(caKeyFile) || !fileExists(caCertFile) {
		prompt := promptui.Prompt{
			Label:     "Missing CA files - Do you want to use another authority",
			IsConfirm: true,
		}

		_, err := prompt.Run()
		if err != nil {
			color.HiRed("Please provide valid rootCa.crt and rootCa.key files or complete the quick setup.")
			color.HiRed("Exiting...")
			os.Exit(-1)
			return
		}

		// Cert file
		prompt = promptui.Prompt{
			Label: "Using own CA - Enter path to CA cert",
			Validate: func(input string) error {
				if !fileExists(input) {
					return errors.New("file not found")
				}
				return nil
			},
		}
		result, err := prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}
		caCertFile = result

		// Key file
		prompt = promptui.Prompt{
			Label: "Using own CA - Enter path to CA key",
			Validate: func(input string) error {
				if !fileExists(input) {
					return errors.New("file not found")
				}
				return nil
			},
		}
		result, err = prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}
		caKeyFile = result
	}

	if len(caKeyPasswd) == 0 {
		prompt := promptui.Prompt{
			Label: "Enter CA pass phrase",
			Mask:  '*',
			Validate: func(input string) error {
				if len(input) <= 4 && len(input) > 1024 {
					return errors.New("you must type in 4 to 1024 characters")
				}
				return nil
			},
		}

		result, err := prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}
		caKeyPasswd = result
	}

	if actionIdx < 2 {

		// Server-side certificate CN & alias
		fmt.Println()
		color.Yellow("Please enter the node´s hostname")
		prompt := promptui.Prompt{
			Label: "Enter the FQDN",
			Validate: func(input string) error {
				match := fqdn_pattern.MatchString(input)
				if !match {
					return errors.New("invalid FQDN")
				}
				return nil
			},
		}

		result, err := prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}
		fqdn = result

		// Keystore PW
		fmt.Println()
		color.Yellow("Please enter a password for the keystore")
		color.HiRed("This is crucial, please use a strong combination of characters.")
		prompt = promptui.Prompt{
			Label: "Enter keystore password",
			Mask:  '*',
			Validate: func(input string) error {
				if len(input) < 6 {
					return errors.New("keystore password is too short - must be at least 6 characters")
				}
				return nil
			},
		}
		result, err = prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}
		keystorePasswd = result

		// Verify Keystore PW
		prompt = promptui.Prompt{
			Label: "Verifying - Enter keystore password",
			Mask:  '*',
			Validate: func(input string) error {
				if input != keystorePasswd {
					return errors.New("passwords dont match")
				}
				return nil
			},
		}

		result, err = prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}

		generateServerSideCert(caCertFile, caKeyFile, fqdn, caKeyPasswd)
		createKeystore(fqdn, keystorePasswd)

		// Truststore PW
		fmt.Println()
		color.Yellow("Please enter a password for the truststore")
		color.HiRed("This is crucial, please use a strong combination of characters.")
		prompt = promptui.Prompt{
			Label: "Enter truststore password",
			Mask:  '*',
			Validate: func(input string) error {
				if len(input) < 6 {
					return errors.New("truststore password is too short - must be at least 6 characters")
				}
				return nil
			},
		}
		result, err = prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}
		truststorePasswd = result

		// Verify Truststore PW
		prompt = promptui.Prompt{
			Label: "Verifying - Enter truststore password",
			Mask:  '*',
			Validate: func(input string) error {
				if input != truststorePasswd {
					return errors.New("passwords dont match")
				}
				return nil
			},
		}

		result, err = prompt.Run()
		if err != nil {
			fmt.Printf("Prompt failed %v\n", err)
			return
		}
		createTruststore(caCertFile)
	}

	if actionIdx == 0 || actionIdx == 2 {
		fmt.Println()
		color.Yellow("Client Certificate Generation")
		for {
			addClientRoutine(caCertFile, caKeyFile, caKeyPasswd, actionIdx == 2)
		}
	}

}

func header() {
	myFigure := figure.NewFigure("Icarus Network", "colossal", true) //alt. font "doom"
	myFigure.Print()
	fmt.Println()
}

func checkRequirements() {
	cmd := exec.Command("sh", "-c", "openssl && keytool")
	err := cmd.Run()
	if err != nil {
		color.HiRed("For this application openssl and keytool must be installed and available in the PATH!")
		os.Exit(127)
	}
}

func generateRootCA(location string, passwd string) {
	command := fmt.Sprintf(`openssl req -x509 -sha256 -days 3650 -newkey rsa:4096 -keyout rootCA.key -out rootCA.crt -subj "/C=%s/O=\"%s\"/CN=%s/OU=\"%s\"/L=%s" -passout pass:%s`,
		CA_C, CA_O, CA_CN, CA_OU, location, passwd)
	cmd := exec.Command("sh", "-c", command)
	if err := cmd.Run(); err == nil {
		color.GreenString("Success!")
	}
}

func generateServerSideCert(caCert string, caKey string, fqdn string, caKeyPass string) {
	command := fmt.Sprintf(`openssl req -new -newkey rsa:4096 -keyout %s.key -out %[1]s.csr -nodes -subj "/CN=%[1]s/OU=\"%s\""`,
		fqdn, CA_OU)
	cmd := exec.Command("sh", "-c", command)
	if err := cmd.Run(); err != nil {
		log.Fatal(err)
	}

	f, err := os.Create(fmt.Sprintf("%s.ext", fqdn))
	if err != nil {
		log.Fatal("Unable to write file!")
	}
	defer f.Close()

	_, err = f.WriteString("" +
		"authorityKeyIdentifier=keyid,issuer\n" +
		"basicConstraints=CA:FALSE\n" +
		"subjectAltName = @alt_names\n" +
		"[alt_names]\n" +
		"DNS.1 = " + fqdn + "\n")

	if err != nil {
		log.Fatal(err)
	}

	command = fmt.Sprintf("openssl x509 -req -CA %s -CAkey %s -in %s.csr -out %[3]s.crt -days 365 -CAcreateserial -extfile %[3]s.ext -passin pass:%s",
		caCert, caKey, fqdn, caKeyPass)
	cmd = exec.Command("sh", "-c", command)
	if err := cmd.Run(); err != nil {
		log.Fatal(err)
	}

}

func createKeystore(fqdn string, keystorePasswd string) {
	command := fmt.Sprintf(`openssl pkcs12 -export -out %s.p12 -name "%[1]s" -inkey %[1]s.key -in %[1]s.crt -password pass:%s`, fqdn, keystorePasswd)
	cmd := exec.Command("sh", "-c", command)
	if err := cmd.Run(); err != nil {
		log.Fatal(err)
	}

	command = fmt.Sprintf("keytool -importkeystore -srckeystore %s.p12 -srcstoretype PKCS12 -destkeystore keystore.jks -deststoretype pkcs12 -srcstorepass %s -storepass %[2]s", fqdn, keystorePasswd)
	cmd = exec.Command("sh", "-c", command)
	if err := cmd.Run(); err != nil {
		log.Fatal(err)
	}
}

func createTruststore(caCert string) {
	command := fmt.Sprintf(`keytool -import -trustcacerts -noprompt -alias ca -ext san=dns:localhost,ip:127.0.0.1 -file %s -keystore truststore.jks`, caCert)
	cmd := exec.Command("sh", "-c", command)
	if err := cmd.Run(); err != nil {
		log.Fatal(err)
	}
}

func fileExists(filename string) bool {
	info, err := os.Stat(filename)
	if os.IsNotExist(err) {
		return false
	}
	return !info.IsDir()
}

func addClientRoutine(caCert string, caKey string, caPasswd string, skipConfirm bool) {
	if !skipConfirm {
		prompt := promptui.Prompt{
			Label:     "Do you want to generate a client Certificate",
			IsConfirm: true,
		}
		_, err := prompt.Run()
		if err != nil {
			os.Exit(0)
			return
		}
	}

	prompt := promptui.Prompt{
		Label: "Enter the username",
		Validate: func(input string) error {
			match, _ := regexp.MatchString(`^([\w-.]+)$`, input)
			if !match {
				return errors.New("invalid username. Only alphanumeric '-' and '.' are allowed")
			}
			return nil
		},
	}

	result, err := prompt.Run()
	if err != nil {
		log.Fatalf("Aborted\n")
		return
	}
	username := result

	command := fmt.Sprintf(`openssl req -new -newkey rsa:4096 -nodes -keyout client%s.key -out client%[1]s.csr -subj "/CN=%[1]s"`, username)
	cmd := exec.Command("sh", "-c", command)
	if err := cmd.Run(); err != nil {
		log.Fatal(err)
	}

	command = fmt.Sprintf(`openssl x509 -req -CA %s -CAkey %s -in client%s.csr -out client%[3]s.crt -days 365 -CAcreateserial -passin pass:%s`,
		caCert, caKey, username, caPasswd)
	cmd = exec.Command("sh", "-c", command)
	if err := cmd.Run(); err != nil {
		log.Fatal(err)
	}
	color.Green(fmt.Sprintf("Success! Generated client%s.crt", username))
}
