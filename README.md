# PbDes03_BrunaCristinaBrandaoeSilva

## Projeto de Microserviços com Spring Boot, Docker e MongoDB

Este projeto consiste em dois microserviços:

1. **ms-event-manager**: Gerencia eventos.
2. **ms-ticket-manager**: Gerencia tickets para os eventos.

Os microserviços se comunicam via **OpenFeign** e utilizam **MongoDB** para persistência de dados. O projeto está configurado para rodar em containers Docker.

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos
- **Docker** instalado e configurado.
- **Docker Compose** instalado.
- **Git** (para clonar o repositório).

### Passos para Execução

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/seu-usuario/PbDes03_BrunaCristinaBrandaoeSilva.git
   cd PbDes03_BrunaCristinaBrandaoeSilva
   ```

2. **Construa e inicie os containers**:

```bash
docker-compose up --build
```

3. **Acesse os microserviços**:

* **ms-event-manager**: [http://localhost:8080](http://localhost:8080)
* **ms-ticket-manager**: [http://localhost:8081](http://localhost:8081)

4. **Acesse o RabbitMQ Management**:

* URL: [http://localhost:15672](http://localhost:15672)
* Usuário: `admin`
* Senha: `admin`

5. **Acesso ao MongoDB via Docker**:

* **db_event**:

  ```
  mongodb://admin:admin@db_event:27017/db_event?authSource=admin
  ```

* **db_ticket**:

  ```
  mongodb://admin:admin@db_ticket:27018/db_ticket?authSource=admin
  ```

---

## ⚠️ Problema Conhecido

### Erro no db_ticket

O microserviço **ms-ticket-manager** pode apresentar o erro:

```
Error: socket hang up
```

Esse erro ocorre ao tentar criar um ticket (por exemplo, via Postman) e está relacionado a um problema de conexão com o MongoDB **db_ticket**. O container do banco encerra abruptamente, impedindo a conexão do microserviço.

**Observação:**

* O banco **db_event** funciona normalmente.
* O microserviço **ms-event-manager** não é afetado por esse problema.

---

## 🛠️ Estrutura do Projeto

### Microserviços

#### ms-event-manager

* Gerencia eventos
* Conecta-se ao **db_event** (MongoDB)
* Expõe endpoints para criação e consulta de eventos

#### ms-ticket-manager

* Gerencia tickets para eventos
* Conecta-se ao **db_ticket** (MongoDB)
* Integra-se ao **RabbitMQ**
* Expõe endpoints para criação e consulta de tickets

### Banco de Dados

* **db_event**: MongoDB do microserviço ms-event-manager
* **db_ticket**: MongoDB do microserviço ms-ticket-manager

### Mensageria

* **RabbitMQ**: Utilizado para comunicação assíncrona entre os microserviços

---

## 🐳 Docker Compose

O projeto utiliza **Docker Compose** para orquestrar todos os containers necessários (microserviços, bancos de dados e mensageria).

---

## 📝 Requisições de Exemplo

### 1. Criar um Evento

* **URL:** `http://localhost:8080/events/create-event`
* **Método:** `POST`

**Corpo da Requisição (Entrada):**

```json
{
  "eventName": "Slipknot",
  "dateTime": "2025-03-30T21:00:00",
  "cep": "01010-000"
}
```

**Resposta (Saída):**

```json
{
  "id": "679ce3cca231cf7f90cda604",
  "eventName": "Slipknot",
  "dateTime": "2025-03-30T21:00:00",
  "cep": "01010-000",
  "logradouro": "Rua São Bento",
  "bairro": "Centro",
  "localidade": "São Paulo",
  "uf": "SP"
}
```

---

### 2. Criar um Ticket

* **URL:** `http://localhost:8081/tickets/create-ticket`
* **Método:** `POST`

**Corpo da Requisição (Entrada):**

```json
{
  "customerName": "Maria",
  "cpf": "12345678",
  "customerMail": "maria@email.com",
  "eventId": "679d0b92dfcc89425a0d4bda",
  "eventName": "Slipknot",
  "brlamount": "R$ 50,00",
  "usdamount": "$ 10,00"
}
```

**Resposta (Saída):**

```json
{
  "ticketId": "679d0b92dfcc89425a0d4bda",
  "cpf": "12345678",
  "customerName": "Maria",
  "customerMail": "maria@email.com",
  "event": {
    "eventId": "679ce3cca231cf7f90cda604",
    "eventName": "Slipknot",
    "eventDateTime": "2025-03-30T21:00:00",
    "logradouro": "Rua São Bento",
    "bairro": "Centro",
    "localidade": "São Paulo",
    "uf": "SP"
  },
  "BRLAmount": "R$ 50,00",
  "USDAmount": "$ 10,00",
  "status": "concluído"
}
```

---

## 📌 Considerações Finais

Este projeto foi desenvolvido com **Spring Boot**, **Docker**, **MongoDB** e **RabbitMQ**, com foco em arquitetura de microserviços.

O problema conhecido relacionado ao **db_ticket** será tratado em futuras atualizações do projeto.
