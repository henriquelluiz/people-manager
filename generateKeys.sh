#!/bin/sh

CERTS_DIR=src/main/resources/certs

echo "[DEPLOY] Gerando chaves RSA com openssl"

if ! openssl version; then 
echo "[ERROR] O openssl não está instalado em sua máquina"
exit
fi

[ ! -d  $CERTS_DIR ] && { mkdir $CERTS_DIR; }

echo "[DEPLOY] Criando o par de chaves"
openssl genrsa -out keypair.pem 2048

echo "[DEPLOY] Extraindo chave pública"
openssl rsa -in keypair.pem -pubout -out public.pem

echo "[DEPLOY] Extraindo chave privada"
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem

echo "[DEPLOY] Tudo pronto!"