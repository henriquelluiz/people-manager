#!/bin/sh

CERTS_DIR=src/main/resources/certs
KEY=$CERTS_DIR/private.pem
JAR_FILE=target/peoplemanager-0.0.1.jar
IMAGE_NAME=peoplemanager

[ ! -d  $CERTS_DIR ] &&
{ echo "[ERROR] Diretório '$CERTS_DIR' não encontrado"; exit; }

[ ! -e $KEY ] &&
{
    echo "[ERROR] Chaves não encontradas";
    echo "[INFO] Execute o arquivo 'generateKeys.sh' e tente novamente";
    exit;
}

if ! docker --version; then
    echo "[ERROR] É preciso ter o docker instalado";
    exit;
fi

echo "[DEPLOY] Gerando o arquivo 'JAR'"
bash mvnw package

[ -e $JAR_FILE ]
{
    echo "[DEPLOY] Criando imagem docker";
    docker build -t $IMAGE_NAME .;
}

echo "[DEPLOY] Executando imagem '$IMAGE_NAME'";
docker run -p 8080:8080 --memory 256m --memory-swap 256m $IMAGE_NAME
echo "[DEPLOY] Tudo pronto!"

