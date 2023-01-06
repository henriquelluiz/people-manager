## Apresentação
O projeto People Manager é um teste prático para a vaga de desenvolvedor back-end na Attornatus Procuradoria Digital.

## Requisitos Funcionais
API RESTful que deve ser capaz de: 
1. Criar uma entidade Pessoa;
2. Editar uma Pessoa;
3. Consultar uma Pessoa;
4. listar todas as pessoas criadas;
5. Criar entidade Endereço para uma Pessoa específica;
6. Listar todos os endereços de uma pessoa;
7. Poder definir qual endereço é o principal.

## O Que você vai encontrar
1. Spring Boot 3
2. Java 17
3. JWT Authentication
4. HATEOAS
5. H2 Database
6. Clean Code
7. Testes de Aceitação, de Integração e Unitários

## Como rodar localmente
Bom, eu separei três maneiras de implementação(deploy):
1. Simples, a partir de um JAR gerado;
2. Docker, a partir de uma imagem;
3. Kubernetes, a partir de arquivos yaml;

### Deploy simples
É necessário ter o Openssl instalado.

Execute o comando a seguir:

```bash
chmod +x generateKeys.sh
./generateKeys.sh
``` 
*Ele irá criar as chaves RSA necessárias para autenticação.*

Em seguida execute:

```bash
chmod +x simpleDeploy.sh
./simpleDeploy.sh
``` 
*Vai fazer o build com MVN, gerar o arquivo JAR e executá-lo.*


### Deploy com Docker
É necessário ter o Docker instalado e com acesso NON-ROOT.
Caso não tenha, confira: [Pós-instalação](https://docs.docker.com/engine/install/linux-postinstall/)

Execute o comando a seguir (se já o executou, não precisa):

```bash
chmod +x generateKeys.sh
./generateKeys.sh
``` 

Em seguida execute:

```bash
chmod +x dockerDeploy.sh
./dockerDeploy.sh
``` 
*Vai criar a imagem e executá-la.*


### Deploy com Kubernetes
É necessário ter o Docker instalado e com acesso NON-ROOT, Minikube e Kubectl.

Execute o comando a seguir (se já o executou, não precisa):

```bash
chmod +x generateKeys.sh
./generateKeys.sh
``` 

Em seguida execute:

```bash
chmod +x kubernetesDeploy.sh
./kubernetesDeploy.sh
``` 
*Vai criar a imagem docker, criar os pods e abrir o navegador padrão com a aplicação rodando.*

## Detalhes
Eu criei a entidade Endereço independente da Pessoa. Isso significa que uma pessoa não precisa ter um endereço.
  > Veja: [Entidade Pessoa](https://github.com/heenluy/people-manager/blob/main/src/main/java/dev/henriqueluiz/peoplemanager/model/Person.java) e [Entidade Endereço](https://github.com/heenluy/people-manager/blob/main/src/main/java/dev/henriqueluiz/peoplemanager/model/Address.java).

Você pode definir um endereço preferncial a qualquer momento.
Você só pode ter um endereço prefencial.

  > Veja: [Controlador de Endereço](https://github.com/heenluy/people-manager/blob/main/src/main/java/dev/henriqueluiz/peoplemanager/controller/AddressController.java)

A aplicação já inicia com um usuário padrão: 
  - Username: developer@account.dev
  - Password: developer

> Veja a documentação da API detalhada em [APIDOC.](https://github.com/heenluy/people-manager/blob/main/API.md)

## Melhorias
Caso queira fazer o deploy em alguma plataforma, eu recomendaria:
1. Configurar o CORS;
2. Configurar o CSFR;
3. Adicio algum sistema de métricas com Prometheus e Grafana.
4. Talvez adicionar o Swegger para documentação.

**E é isso, até mais**✌️