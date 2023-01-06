#!/bin/sh

CERTS_DIR=src/main/resources/certs

echo "[DEPLOY] Gerando chaves RSA com openssl"

if ! openssl version; then 
echo "[ERROR] O openssl não está instalado em sua máquina"
exit
fi

[ ! -d  $CERTS_DIR ] && { mkdir $CERTS_DIR; }

echo "[DEPLOY] Criando o par de chaves"
openssl genrsa -out $CERTS_DIR/keypair.pem 2048

echo "[DEPLOY] Extraindo chave pública"
openssl rsa -in $CERTS_DIR/keypair.pem -pubout -out $CERTS_DIR/public.pem

echo "[DEPLOY] Extraindo chave privada"
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in $CERTS_DIR/keypair.pem -out $CERTS_DIR/private.pem

echo "[DEPLOY] Tudo pronto!"
