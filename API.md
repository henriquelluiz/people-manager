### Notas
Usuário padrão:
- **Username:** developer@account.dev
- **Senha:** developer
- **Authorities:** manager 

### Health
Verifique se a aplicação está funcionando corretamente.

```bash
curl -X GET \
  'http://localhost:8080/api/actuator/health' \
  --header 'Accept: application/hal+json'
```
Resposta esperada:

```json
{
  "status": "UP"
}
```

### Root Entry Point
Porta de entrada da aplicação.

```bash
curl -X GET \
  'http://localhost:8080/api/' \
  --header 'Accept: application/hal+json'
```
Resposta esperada:

```json
{
  "_links": {
    "createUser": {
      "href": "http://localhost:8080/api/users/save"
    }
  }
}
```

### User Controller: Save User
Cria um usuário sem uma função definida.

```bash
curl -X POST \
  'http://localhost:8080/api/users/save' \
  --header 'Accept: application/hal+json' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "email": "test@mail.dev",
  "password": "test"
}'
```
Resposta esperada:

```json
{
  "email": "test@mail.dev",
  "authorities": [],
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/roles/get/all"
    }
  }
}
```

### User Controller: Save Role
Cria uma função/cargo.

> Somente para usuários com a role **manager**.

> Pode ser em caixa alta ou caixa baixa: TEST ou test.

```bash
curl -X POST \
  'http://localhost:8080/api/roles/save' \
  --header 'Accept: application/hal+json' \
  --header 'Authorization: Bearer <token>' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "roleName": "test"
}'
```
Resposta esperada:

```json
{
  "name": "test",
  "_links": {
    "self": [
      {
        "href": "http://localhost:8080/api/roles/add"
      },
      {
        "href": "http://localhost:8080/api/roles/get/all"
      }
    ]
  }
}
```

### User Controller: Add Roles to User
Adiciona uma função/cargo ao usuário.

> Inicialmente existem duas opções de roles/scopes pré-definidas: **read** ou **write**.

> Se você tentar adicionar a role **manager** ou **admin**, você recebera **Method Not Allowed (405)**.

```bash
curl -X PUT \
  'http://localhost:8080/api/roles/add' \
  --header 'Accept: application/hal+json' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "roleName": "read",
  "userEmail": "test@mail.dev"
}'
```
Resposta esperada:

```json
{
  "_links": {
    "generateTokens": {
      "href": "http://localhost:8080/api/token"
    }
  }
}
```

### User Controller: Get Tokens
Cria um jwt token com as credenciais do usuário.

```bash
curl -X POST \
  'http://localhost:8080/api/token' \
  --header 'Accept: application/json' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "email": "test@mail.dev",
  "password":"test"
}'
```
Resposta esperada:

```json
{
  "type": "bearer",
  "access_token": {
    "exp": "2023-01-06T22:09:35.728410385Z",
    "token": "<token>"
  },
  "refresh_token": {
    "exp": "2023-01-07T21:44:35.728410385Z",
    "token": "<token>"
  }
}
```

### User Controller: Refresh Tokens
Criar novos tokens a partir de um refresh token.

```bash
curl -X GET \
  'http://localhost:8080/api/refresh' \
  --header 'Accept: application/json' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "token": "<token>"
}'
```
Resposta esperada:

```json
{
  "type": "bearer",
  "access_token": {
    "exp": "2023-01-06T22:09:35.728410385Z",
    "token": "<token>"
  },
  "refresh_token": {
    "exp": "2023-01-07T21:44:35.728410385Z",
    "token": "<token>"
  }
}
```

### Person Controller: Save Person
Salva uma entidade Pessoa.

```bash
curl -X POST \
  'http://localhost:8080/api/persons/save' \
  --header 'Accept: application/hal+json' \
  --header 'Authorization: Bearer <token>' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "firstName": "Nome",
  "lastName": "Sobrenome",
  "dateOfBirth": "dd-MM-yyyy"
}'
```
Resposta esperada:

```json
{
  "personId": 1,
  "firstName": "Nome",
  "lastName": "Sobrenome",
  "dateOfBirth": "dd-MM-yyyy",
  "_links": {
    "createAddress": {
      "href": "http://localhost:8080/api/addresses/save?personId=1"
    },
    "self": [
      {
        "href": "http://localhost:8080/api/persons/get?personId=1"
      },
      {
        "href": "http://localhost:8080/api/persons/get/all"
      }
    ]
  }
}
```

### Person Controller: Get Person By Id
Procura por uma pessoa baseada no identificador.

```bash
curl -X GET \
  'http://localhost:8080/api/persons/get?personId=1' \
  --header 'Accept: application/hal+json' \
  --header 'Authorization: Bearer <token>'
```
Resposta esperada:

```json
{
  "personId": 1,
  "firstName": "Nome",
  "lastName": "Sobrenome",
  "dateOfBirth": "dd-MM-yyyy",
  "_links": {
    "self": [
      {
        "href": "http://localhost:8080/api/persons/update?personId=1"
      },
      {
        "href": "http://localhost:8080/api/persons/delete?personId=1"
      }
    ],
    "getAddressesByPerson": {
      "href": "http://localhost:8080/api/addresses/get/all?personId=1"
    }
  }
}
```

### Person Controller: Get All Persons
Procura por todas as pessoas disponíveis.

```bash
curl -X GET \
  'http://localhost:8080/api/persons/get/all?page=0&size=1' \
  --header 'Accept: application/hal+json' \
  --header 'Authorization: Bearer <token>'
```
Resposta esperada:

```json
{
  "_embedded": {
    "personList": [
      {
        "personId": 1,
        "firstName": "Nome",
        "lastName": "Sobrenome",
        "dateOfBirth": "dd-MM-yyyy"
      }
    ]
  }
}
```

### Person Controller: Update Person
Atualiza uma pessoa.

```bash
curl -X PUT \
  'http://localhost:8080/api/persons/update?personId=1' \
  --header 'Accept: application/json' \
  --header 'Authorization: Bearer <token>' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "firstName": "Nome",
  "lastName": "Sobrenome",
  "dateOfBirth": "dd-MM-yyyy"
}'
```
Resposta esperada:

```
No content 204
```

### Person Controller: Delete Person
Remove uma pessoa.

```bash
curl -X DELETE \
  'http://localhost:8080/api/persons/delete?personId=1' \
  --header 'Accept: application/json' \
  --header 'Authorization: Bearer <token>'
```
Resposta esperada:

```
No content 204
```

### Address Controller: Save Address
Cria um endereço para uma pessoa específica.

```bash
curl -X POST \
  'http://localhost:8080/api/addresses/save?personId=2' \
  --header 'Accept: application/hal+json' \
  --header 'Authorization: Bearer <token>' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "city": "Cidade",
  "number": "10A",
  "street": "Rua",
  "postalCode": "00000000"
}'
```
Resposta esperada:

```json
{
  "addressId": 1,
  "street": "Rua",
  "postalCode": "00000000",
  "number": "10A",
  "city": "Cidade",
  "preferred": false,
  "_links": {
    "self": [
      {
        "href": "http://localhost:8080/api/addresses/edit/preferred?personId=2&addressId=4"
      },
      {
        "href": "http://localhost:8080/api/addresses/get/all?personId=2"
      }
    ]
  }
}
```

### Address Controller: Get All Addresses By Person
Busca por todos os endereços de uma pessoa.

```bash
curl -X GET \
  'http://localhost:8080/api/addresses/get/all?personId=1&page=0&size=1' \
  --header 'Accept: application/hal+json' \
  --header 'Authorization: Bearer <token>'
```
Resposta esperada:

```json
{
  "_embedded": {
    "addressList": [
      {
        "addressId": 1,
        "street": "Rua",
        "postalCode": "00000000",
        "number": "10A",
        "city": "Cidade",
        "preferred": false
      },
      {
        "addressId": 2,
        "street": "Rua",
        "postalCode": "00000000",
        "number": "10B",
        "city": "Cidade",
        "preferred": true
      }
    ]
  }
}
```

### Address Controller: Set Preferred
Define o endereço preferencial/principal da pessoa.

```bash
curl -X PUT \
  'http://localhost:8080/api/addresses/edit/preferred?personId=2&addressId=1' \
  --header 'Accept: application/json' \
  --header 'Authorization: Bearer <token>'
```
Resposta esperada:

```
No content 204
```

### Address Controller: Get Preferred
Busca o endereço preferencial/principal da pessoa.

```bash
curl -X GET \
  'http://localhost:8080/api/addresses/get/preferred?personId=2&addressId=1' \
  --header 'Accept: application/hal+json' \
  --header 'Authorization: Bearer <token>'
```
Resposta esperada:

```json
{
  "addressId": 2,
  "street": "Rua",
  "postalCode": "",
  "number": "10B",
  "city": "Cidade",
  "preferred": true,
  "_links": {
    "self": [
      {
        "href": "http://localhost:8080/api/addresses/edit/preferred?personId=2&addressId={addressId}",
        "templated": true
      },
      {
        "href": "http://localhost:8080/api/addresses/get/all?personId=2"
      }
    ]
  }
}
```
