[Add new client?](#5-generate-client-side-certificate)

### 1. Generate Certificate Authority

Please always change ``L``(Loation) to match the project utilizing the certificate!

```shell
openssl req -x509 -sha256 -days 3650 -newkey rsa:4096 -keyout rootCA.key -out rootCA.crt -subj "/C=DE/O=\"dotSpace Development\"/CN=dotspace.team/OU=\"Icarus Core System\"/L=Pluginstube"
```

### 2. Generate Server-side Certificate
```shell
# Generate Cert
openssl req -new -newkey rsa:4096 -keyout localhost.key -out localhost.csr -nodes
# Create Configuration to sign
touch localhost.ext
cat <<EOF > localhost.ext
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
subjectAltName = @alt_names
[alt_names]
DNS.1 = localhost
DNS.2 = 127.0.0.1
EOF
# Sign request with CA
openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in localhost.csr -out localhost.crt -days 365 -CAcreateserial -extfile localhost.ext
```

### 3. Import to the Keystone
````shell
# Bundle Certificate and Key
openssl pkcs12 -export -out localhost.p12 -name "localhost" -inkey localhost.key -in localhost.crt

#Create keystore
keytool -importkeystore -srckeystore localhost.p12 -srcstoretype PKCS12 -destkeystore keystore.jks -deststoretype JKS
````

### 4. Create Truststore
```shell
# Import Root CA into Truststore
keytool -import -trustcacerts -noprompt -alias ca -ext san=dns:localhost,ip:127.0.0.1 -file rootCA.crt -keystore truststore.jks
```
Only client certificates signed by the rootCA can access the server. SAN doesnt really matter and should be ignored

### 5. Generate Client-side Certificate
The ``CN``(Common Name) does represent the clients username! **Always change**!
```shell
# Generate Client Certificate
openssl req -new -newkey rsa:4096 -nodes -keyout clientBob.key -out clientBob.csr ##-subj "/CN=Bob/C=DE/O=\"dotSpace Development\"/OU=\"Icarus Core System\"/L=Pluginstube"

# Sign with rootCA
openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in clientBob.csr -out clientBob.crt -days 365 -CAcreateserial
```