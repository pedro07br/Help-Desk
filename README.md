# 🎫 Help Desk — Sistema de Chamados

Sistema de gerenciamento de chamados técnicos desenvolvido com **Java Spring Boot**, **Apache Kafka** e **React**. Permite criar, visualizar e atualizar o status de chamados em tempo real com integração assíncrona via Kafka.

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)
- [Endpoints da API](#endpoints-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Testes](#testes)
- [Fluxo Kafka](#fluxo-kafka)

---

## 💡 Sobre o Projeto

O **Help Desk** é uma aplicação full-stack para gerenciamento de chamados de suporte técnico. Os chamados possuem três estados possíveis:

| Status | Descrição |
|--------|-----------|
| `ABERTO` | Chamado criado, aguardando atendimento |
| `EM_ANDAMENTO` | Chamado sendo tratado por um técnico |
| `FECHADO` | Chamado resolvido e encerrado |

A cada mudança de status, uma mensagem é publicada no **Apache Kafka**, permitindo rastreabilidade e integração com outros sistemas.

---

## 🛠️ Tecnologias

### Backend
| Tecnologia | Versão | Função |
|------------|--------|--------|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.3.2 | Framework web |
| Spring Data JPA | — | Persistência de dados |
| Spring Kafka | — | Integração com Kafka |
| MySQL | 8.x | Banco de dados relacional |
| Maven | 3.x | Gerenciador de dependências |

### Frontend
| Tecnologia | Versão | Função |
|------------|--------|--------|
| React | 18.x | Interface do usuário |
| Axios | 1.6.x | Requisições HTTP |
| React Scripts | 5.0.1 | Toolchain CRA |

### Infraestrutura
| Tecnologia | Função |
|------------|--------|
| Docker + Docker Compose | MySQL, Zookeeper e Kafka |
| Apache Kafka | Mensageria assíncrona |
| Apache Zookeeper | Coordenação do Kafka |

---

## 🏗️ Arquitetura

```
┌─────────────────────────────────────────────────────────┐
│                     FRONTEND (React)                    │
│                    localhost:3000                       │
└──────────────────────────┬──────────────────────────────┘
                           │ HTTP (proxy → 8080)
┌──────────────────────────▼──────────────────────────────┐
│                  BACKEND (Spring Boot)                  │
│                    localhost:8080                       │
│                                                         │
│  Controller → Service → Repository (JPA)                │
│                   ↓                                     │
│             KafkaProducer                               │
└──────────┬────────────────────────────┬─────────────────┘
           │                            │
           ▼                            ▼
┌──────────────────┐        ┌──────────────────────┐
│      MySQL       │        │    Apache Kafka      │
│  localhost:3308  │        │   localhost:9092     │
└──────────────────┘        │                      │
                            │  Tópicos:            │
                            │  • chamados-abertos  │
                            │  • chamados-em-      │
                            │    andamento         │
                            │  • chamados-fechados │
                            └──────────────────────┘
```

---

## ✅ Pré-requisitos

Certifique-se de ter instalado:

- [Java 21+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [Node.js 18+ e npm](https://nodejs.org/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

---

## 🚀 Instalação e Execução

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/help-desk.git
cd help-desk
```

### 2. Suba a infraestrutura com Docker

```bash
docker-compose up -d
```

Isso irá iniciar:
- **MySQL** na porta `3308`
- **Zookeeper** na porta `2181`
- **Kafka** na porta `9092`

Verifique se os containers estão rodando:

```bash
docker ps
```

### 3. Execute o Backend

```bash
cd backend
mvn spring-boot:run
```

O servidor iniciará em `http://localhost:8080`.

> 💡 Você também pode rodar diretamente pelo **IntelliJ IDEA** clicando no botão Run na classe `HelpdeskApplication.java`.

### 4. Execute o Frontend

```bash
cd Frontend
npm install
npm start
```

A aplicação abrirá automaticamente em `http://localhost:3000`.

---

## 📡 Endpoints da API

Base URL: `http://localhost:8080`

### Chamados

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/chamados` | Lista todos os chamados |
| `GET` | `/chamados/{id}` | Busca chamado por ID |
| `GET` | `/chamados/status/{status}` | Lista chamados por status |
| `POST` | `/chamados` | Cria um novo chamado |
| `PATCH` | `/chamados/{id}/status` | Atualiza o status de um chamado |

### Exemplos de Requisição

**Criar um chamado:**
```http
POST /chamados
Content-Type: application/json

{
  "titulo": "Computador não liga",
  "descricao": "O computador do setor financeiro não está ligando desde ontem."
}
```

**Atualizar status:**
```http
PATCH /chamados/1/status
Content-Type: application/json

{
  "status": "EM_ANDAMENTO"
}
```

**Resposta de sucesso:**
```json
{
  "id": 1,
  "titulo": "Computador não liga",
  "descricao": "O computador do setor financeiro não está ligando desde ontem.",
  "status": "EM_ANDAMENTO",
  "criadoEm": "2026-04-29T14:00:00"
}
```

---

## 📁 Estrutura do Projeto

```
help-desk/
├── docker-compose.yml
├── README.md
│
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/pedro/helpdesk/
│       │   │   ├── HelpdeskApplication.java
│       │   │   ├── config/
│       │   │   │   └── CorsConfig.java
│       │   │   ├── controller/
│       │   │   │   └── ChamadoController.java
│       │   │   ├── entity/
│       │   │   │   └── Chamado.java          # Enum: ABERTO, EM_ANDAMENTO, FECHADO
│       │   │   ├── exception/
│       │   │   │   └── GlobalExceptionHandler.java
│       │   │   ├── kafka/
│       │   │   │   ├── KafkaConfig.java
│       │   │   │   ├── ChamadoProducer.java
│       │   │   │   └── ChamadoConsumer.java
│       │   │   ├── repository/
│       │   │   │   └── ChamadoRepository.java
│       │   │   └── service/
│       │   │       └── ChamadoService.java
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│           └── java/com/pedro/helpdesk/
│               ├── ApplicationTests.java
│               ├── controller/
│               │   └── ChamadoControllerTest.java
│               ├── kafka/
│               │   ├── ChamadoProducerTest.java
│               │   └── ChamadoConsumerTest.java
│               └── service/
│                   └── ChamadoServiceTest.java
│
└── Frontend/
    ├── package.json
    └── src/
        ├── index.js
        ├── App.jsx
        ├── App.css
        ├── components/
        │   ├── ChamadoForm.jsx
        │   ├── ChamadoCard.jsx
        │   └── ChamadoLista.jsx
        └── services/
            └── api.js
```

---

## 🧪 Testes

O projeto possui testes **unitários** e de **integração** para as camadas de serviço, Kafka e controller.

### Executar todos os testes

```bash
cd backend
mvn test
```

### Executar sem testes (apenas build)

```bash
mvn clean install -DskipTests
```

### Cobertura dos Testes

| Classe de Teste | Tipo | Quantidade |
|----------------|------|-----------|
| `ChamadoServiceTest` | Unitário (Mockito) | 9 testes |
| `ChamadoProducerTest` | Unitário (Mockito) | 3 testes |
| `ChamadoConsumerTest` | Unitário (Mockito) | 3 testes |
| `ChamadoControllerTest` | Integração (MockMvc) | 6 testes |

---

## 📨 Fluxo Kafka

Ao criar ou atualizar um chamado, o `ChamadoProducer` publica uma mensagem no tópico correspondente ao status:

```
Status ABERTO       → tópico: chamados-abertos
Status EM_ANDAMENTO → tópico: chamados-em-andamento
Status FECHADO      → tópico: chamados-fechados
```

O `ChamadoConsumer` escuta os três tópicos e loga as mensagens recebidas em tempo real no console.

### Configuração dos Tópicos (`application.properties`)

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.consumer.group-id=grupo-helpdesk
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=com.pedro.helpdesk.entity
spring.kafka.consumer.properties.spring.json.value.default.type=com.pedro.helpdesk.entity.Chamado
```

---

## ⚙️ Configurações

### Banco de Dados (`application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3308/helpdesk
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Docker Compose

```yaml
services:
  mysql:
    image: mysql:8
    ports:
      - "3308:3306"
    environment:
      MYSQL_ROOT_PASSWORD: ""
      MYSQL_ALLOW_EMPTY_PASSWORD: "yes"
      MYSQL_DATABASE: helpdesk

  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:latest
    ports:
      - "9092:9092"
    depends_on:
      - zookeeper
```

---

## 👨‍💻 Autor

Desenvolvido por **Pedro** como projeto de estudo de **Java Spring Boot + Apache Kafka + React**.

---

## 📄 Licença

Este projeto está sob a licença **MIT**. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
