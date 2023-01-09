## Apresentação
O projeto People Manager é um teste prático para a vaga de desenvolvedor back-end na Attornatus Procuradoria Digital.

## Requisitos Funcionais
Uma API RESTful que deve ser capaz de: 
1. Criar uma entidade Pessoa;
2. Editar uma Pessoa;
3. Consultar uma Pessoa;
4. listar todas as pessoas criadas;
5. Criar entidade Endereço para uma Pessoa específica;
6. Listar todos os endereços de uma pessoa;
7. Poder definir qual endereço é o principal.

## O Que Você Vai Encontrar
- Spring Boot 3
- Java 17
- JWT Authentication
- HATEOAS
- JPA
- Clean Code
- Testes de Aceitação, de Integração e Unitários
- Deploy automátizado com shell scripting.

## Como Rodar Localmente
Você pode iniciar essa aplicação de três formas: a partir de um arquivo *.jar, com docker ou com kubernetes/minikube.

### Deploy Simples Com um Arquivo *.jar
É necessário ter o [OpenSSL](https://www.openssl.org/source/) instalado – ele já vem instalado na maioria das distros Linux.

Antes de tudo, é preciso gerar as chaves RSA 256. Portanto, execute o comando a seguir:

```bash
chmod +x generateKeys.sh
./generateKeys.sh
``` 
> Isso irá criar as chaves RSA necessárias para autenticação.

Em seguida execute:

```bash
chmod +x simpleDeploy.sh
./simpleDeploy.sh
``` 
> Isso vai fazer o build com MVN, gerar o arquivo **peoplemanager-0.0.1.jar** e executá-lo.


### Deploy Com Docker
É necessário ter o Docker instalado e com acesso non-root.
Caso não tenha, confira: [Pós-instalação do Docker no Linux.](https://docs.docker.com/engine/install/linux-postinstall/)

Execute os comandos a seguir:
> **As chaves RSA são requeridas.**

```bash
chmod +x dockerDeploy.sh
./dockerDeploy.sh
``` 
> Isso irá criar a imagem e executá-la.


### Deploy Com Kubernetes/Minikube
É necessário ter instaldos em sua máquina o [Docker](https://docs.docker.com/engine/install/ubuntu/), [Minikube](https://minikube.sigs.k8s.io/docs/start/) e [Kubectl](https://kubernetes.io/docs/tasks/tools/).

Execute os comandos a seguir:
> **As chaves RSA são requeridas.**

```bash
chmod +x kubernetesDeploy.sh
./kubernetesDeploy.sh
``` 
> Isso irá criar a imagem docker, criar os pods e expor a rota/serviço.

## Detalhes
Mapeamento relacional das entidades Pessoa e Endereço.

  > Veja: [Entidade Pessoa](https://github.com/heenluy/people-manager/blob/main/src/main/java/dev/henriqueluiz/peoplemanager/model/Person.java) e [Entidade Endereço](https://github.com/heenluy/people-manager/blob/main/src/main/java/dev/henriqueluiz/peoplemanager/model/Address.java).

Você pode definir um endereço preferencial a qualquer momento. Mas, você só pode ter um endereço preferencial.

  > Veja: [Endpoint Endereço](https://github.com/heenluy/people-manager/blob/main/src/main/java/dev/henriqueluiz/peoplemanager/controller/AddressController.java) e [Repositório Endereço](https://github.com/heenluy/people-manager/blob/main/src/main/java/dev/henriqueluiz/peoplemanager/repository/AddressRepository.java).

A aplicação já inicia com um usuário padrão: 
  - Username/Email: developer@account.dev
  - Password: developer
  - Authorities: Manager

> **Veja a documentação da API detalhada em [API REFERENCE.](https://github.com/heenluy/people-manager/blob/main/API.md)**

## Melhorias
Caso queira fazer o deploy em alguma plataforma, eu recomendaria:
1. Configurar o CORS;
2. Configurar o CSRF;
3. Adicionar algum sistema de métricas como o Prometheus/Grafana.
4. Talvez adicionar o Swegger para documentação.

**E é isso, até mais**✌️
