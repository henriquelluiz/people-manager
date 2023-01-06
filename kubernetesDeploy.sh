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

if ! minikube version; then
    echo "[ERROR] É preciso ter o minikube instalado";
    exit;
fi

if ! kubectl version; then
    echo "[ERROR] É preciso ter o kubectl instalado";
    exit;
fi

echo "[DEPLOY] Gerando o arquivo 'JAR'"
bash mvnw package

[ -e $JAR_FILE ]
{
    echo "[DEPLOY] Criando imagem docker";
    docker build -t $IMAGE_NAME .;
}

minikube start --driver=docker
eval '$(minikube docker-env)'

echo "[DEPLOY] Criando o 'deployment'"
kubectl create -f k8s/appDeployment.yaml

echo "[DEPLOY] Criando um serviço para o 'deployment'"
kubectl create -f k8s/appService.yaml

read -p '[DEPLOY] A aplicação será iniciada no seu navegador padrão, tudo bem? (SIM/NÃO): ' USER_ANSWER

if [ "$USER_ANSWER" = "SIM" ] || [ "$USER_ANSWER" = "S" ] || [ "$USER_ANSWER" = "sim" ] || [ "$USER_ANSWER" = "s" ]; then
    minikube service app-people-manager
else
echo "[DEPLOY] Você precisa expor o serviço 'kubectl port-forward service/app-people-manager 8080:8080'"
exit;
fi