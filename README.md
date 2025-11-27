##🏨 Hotelaria API
##Sistema Completo de Gestão Hoteleira

Java • Spring Boot • PostgreSQL • Clean Architecture • DTOs • Validações

A Hotelaria API é uma solução backend moderna e escalável para gerenciamento completo de operações hoteleiras, incluindo usuários, clientes, quartos e fluxo de ocupação.

🚧 Status: Em desenvolvimento ativo
O projeto está evoluindo com novas features, incluindo autenticação, níveis de acesso e integração com Angular.

🚀 Principais Funcionalidades
👥 Gestão de Usuários

Cadastro estruturado usando DTOs

Perfis (admin / padrão)

Validação completa dos campos

Estrutura pronta para JWT + RBAC

🧍 Gestão de Clientes

Cadastro de hóspedes

Listagem e busca por nome

🏨 Gestão de Quartos

Listar todos os quartos

Visualizar quarto específico

Ocupar/Desocupar quartos

Status dinâmico: DISPONÍVEL / OCUPADO

Associação automática com cliente

🧱 Tecnologias Utilizadas
Backend

Java 17

Spring Boot 3

Spring Web

Spring Data JPA / Hibernate

Jakarta Validation

Lombok

Banco de Dados

PostgreSQL

Build & Tools

Maven

REST API padrão

🏗️ Arquitetura & Boas Práticas

✔️ Separação clara de camadas (Controller → Service → Repository → Entity)

✔️ DTOs para isolar o domínio

✔️ Services focados em regra de negócio

✔️ Repositórios seguindo o padrão JPA

✔️ Validações robustas com @Valid

✔️ Estrutura preparada para autenticação, documentação e expansão

✔️ Código limpo, organizado e escalável

📁 Estrutura do Projeto
src/main/java/com.hotelaria
│
├── usuario
│   ├── entity
│   ├── dto
│   ├── repository
│   └── service
│
├── cliente
├── quarto
└── controller

⚙️ Como Executar o Projeto
1️⃣ Clonar o repositório
git clone https://github.com/SEU-USUARIO/hotelaria-api.git

2️⃣ Configurar o application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hotelaria
spring.datasource.username=postgres
spring.datasource.password=12345

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

3️⃣ Executar o projeto
mvn spring-boot:run

📡 Endpoints Disponíveis (v1)
🔐 Usuários
Método	Endpoint	Descrição
POST	/usuarios	Criar usuário
🧍 Clientes
Método	Endpoint	Descrição
POST	/clientes	Criar cliente
GET	/clientes	Listar clientes
🏨 Quartos
Método	Endpoint	Descrição
GET	/quartos	Listar quartos
GET	/quartos/{num}	Ver detalhes
PUT	/quartos/ocupar	Ocupar quarto
PUT	/quartos/desocupar/{num}	Desocupar quarto
🧭 Roadmap de Evolução
✔️ Implementado

🚀 CRUD de usuários

🚀 CRUD de clientes

🚀 Ocupação e desocupação de quartos

🚀 DTOs e validações

🔧 Em desenvolvimento

🔒 Autenticação e autorização (JWT)

🛡️ Criptografia de senha (BCrypt)

📘 Documentação Swagger

🖥️ Integração completa com Angular

📊 Painel administrativo

☁️ Deploy em nuvem
