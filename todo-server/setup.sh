#!/bin/bash

#Create a keystore for JWT
keytool -genkeypair -alias jwtkey -keyalg RSA -keysize 2048 -validity 36500 -keystore build/libs/JWT-keystore.jks
# genkeypair: Generates a key pair (public and private key).
# alias jwtkey: The alias for the key pair in the keystore.
# keyalg RSA: The algorithm for the key, RSA in this case.
# keysize 2048: The size of the key.
# validity 36500: The validity period of the key in days.
# keystore jwtkeystore.jks: The name of the keystore file.

#export the public key from the keystore so that it can be shared with clients or other services that need to verify the JWTs
keytool -export -alias jwtkey -keystore build/libs/JWT-keystore.jks -rfc -file build/libs/JWT-publickey.crt
# export: Exports the certificate.
# alias jwtkey: The alias for the key pair.
# keystore jwtkeystore.jks: The keystore file.
# rfc: Output in PEM format.
# file jwtpublickey.crt: The file to which the public key will be exported.
