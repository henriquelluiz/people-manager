#!/bin/sh

CERTS_DIR=src/main/resources/certs
KEY=$CERTS_DIR/private.pem
JAR_FILE=target/peoplemanager-0.0.1.jar

[ ! -d  $CERTS_DIR ] &&
{ echo "[ERROR] Diretório '$CERTS_DIR' não encontrado"; exit; }

[ ! -e $KEY ] &&
{
    echo "[ERROR] Chaves não encontradas";
    echo "[INFO] Execute o arquivo 'generateKeys.sh' e tente novamente";
    exit;
}

echo "[DEPLOY] Gerando o arquivo 'JAR'"
bash mvnw package

echo "[DEPLOY] Executando a aplicação a partir de '$JAR_FILE'"
java -jar $JAR_FILE
