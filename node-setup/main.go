package main

import (
	"errors"
	"fmt"
	"github.com/common-nighthawk/go-figure"
	"github.com/fatih/color"
	"github.com/manifoldco/promptui"
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
		Items: []string{"Quick setup", "Generate a new server certificate", "Generate a new client certificate"},
	}
	actionIdx, _, err := selection.Run()
	if err != nil {
		fmt.Printf("Prompt failed %v\n", err)
		return
	}

	var ca_pw string
	var fqdn string
	var keystore_pw string
	var truststore_pw string
	var username string

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
		ca_pw = result

		// Verify CA PW
		prompt = promptui.Prompt{
			Label: "Verifying - Enter CA pass phrase",
			Mask:  '*',
			Validate: func(input string) error {
				if input != ca_pw {
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

		generateRootCA(location, ca_pw)
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
		keystore_pw = result

		// Verify Keystore PW
		prompt = promptui.Prompt{
			Label: "Verifying - Enter keystore password",
			Mask:  '*',
			Validate: func(input string) error {
				if input != keystore_pw {
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
		truststore_pw = result

		// Verify Truststore PW
		prompt = promptui.Prompt{
			Label: "Verifying - Enter truststore password",
			Mask:  '*',
			Validate: func(input string) error {
				if input != truststore_pw {
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
	}

	if actionIdx == 0 {
		fmt.Println()
		color.Yellow("Please enter the username of the first client")
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
			fmt.Printf("Prompt failed %v\n", err)
			return
		}
		username = result
	}

	fmt.Printf("%s %s %s %s\n", fqdn, keystore_pw, truststore_pw, username)
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

func generateServerSideCert(path string, fqdn string) {

}
