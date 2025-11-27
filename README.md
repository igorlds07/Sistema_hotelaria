🏨 Hotelaria API — Sistema Completo de Gestão Hoteleira

✨ Java | Spring Boot | Clean Architecture | DTOs | Validações | PostgreSQL

A Hotelaria API é um backend estruturado para gerenciamento completo de um sistema hoteleiro, permitindo o controle de usuários, clientes, quartos, ocupações e operações internas.
Construída com foco em arquitetura limpa, modularidade, boas práticas de engenharia e expansão futura.

🚧 Status: Em desenvolvimento ativo
As funcionalidades principais já estão implementadas, e novas capacidades — como autenticação, níveis de acesso, integração com Angular e dashboards administrativos — estão sendo evoluídas iterativamente.

🚀 Principais Funcionalidades
👥 Gestão de Usuários

Cadastro utilizando DTOs

Perfis (admin/usuário) — preparado para RBAC

Validação completa dos campos

Pronta para implementação de autenticação JWT

🧍 Gestão de Clientes

Cadastro de hóspedes

Busca por nome

🏨 Gestão de Quartos

Listagem geral e específica

Ocupação e desocupação

Status automático: DISPONÍVEL / OCUPADO

Associação entre cliente e quarto

🧱 Tecnologias Utilizadas
Tecnologia	Finalidade
Java 17	Linguagem base
Spring Boot 3	Framework principal
Spring Data JPA / Hibernate	Persistência
Jakarta Validation	Validações
Lombok	Redução de boilerplate
PostgreSQL	Banco de dados
Maven	Gerenciamento de dependências
🏗️ Arquitetura e Boas Práticas

Arquitetura limpa com separação das camadas

Uso de DTOs para segurança e organização

Services com regras de negócio isoladas

Repository pattern com Spring Data

Validações declarativas com @Valid

Estrutura preparada para escalabilidade

Código organizado e pronto para expandir para frontend Angular

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

⚙️ Como Executar
1️⃣ Clonar o repositório
git clone https://github.com/SEU-USUARIO/hotelaria-api.git

2️⃣ Configurar o banco no application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hotelaria
spring.datasource.username=postgres
spring.datasource.password=12345

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

3️⃣ Rodar o projeto
mvn spring-boot:run

📡 Principais Endpoints (versão atual)
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
GET	/quartos/{num}	Detalhes de um quarto
PUT	/quartos/ocupar	Ocupar um quarto
PUT	/quartos/desocupar/{num}	Liberar quarto
🧭 Roadmap de Evolução

✔️ CRUD completo de usuários
✔️ Cadastro e consulta de clientes
✔️ Sistema de ocupação/desocupação
✔️ DTOs e validação aplicada

📌 Em desenvolvimento próximos ciclos

Criptografia de senha (BCrypt)

Autenticação e Autorização (JWT + roles)

Painel administrativo

Integração com Angular (frontend web)

Documentação Swagger

Deploy em nuvem
