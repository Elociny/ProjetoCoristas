# 🎼 Coristas: Sistema de Gerenciamento de Coristas e Agenda

## 🌟 Visão Geral do Projeto

O Coristas é uma plataforma web desenvolvida para simplificar e otimizar os processos administrativos de um coro profissional. O sistema centraliza o **Gerenciamento de Coristas e Músicos**, a **Agenda de Eventos (Ensaios e Apresentações)** e, crucialmente, aplica as **Regras de Negócio (RNs)** para garantir que apenas membros aptos participem das apresentações.

Este projeto segue uma arquitetura **Decoupled (API RESTful)**, separando o Front-end (HTML/JS) do Back-end (Java/Servlets) para garantir segurança, escalabilidade e manutenibilidade.

---

## 🚀 Tecnologias Utilizadas

O projeto foi construído usando tecnologias Java e padrões modernos de desenvolvimento web:

### Back-end (Servidor e Persistência)

| Componente | Tecnologia | Função |
| :--- | :--- | :--- |
| **Linguagem** | Java (JDK 21+) | Linguagem principal do Back-end. |
| **Servlets** | Jakarta Servlet API 6.0 | Gerenciamento de requisições HTTP (Controlador da API). |
| **Persistência** | JPA (Hibernate Core 6.x) | Mapeamento Objeto-Relacional (ORM) para o banco de dados. |
| **Banco de Dados** | MySQL | Armazenamento persistente de dados. |
| **JSON** | Google Gson | Conversão de objetos Java em JSON e vice-versa. |
| **Servidor** | Apache Tomcat 11 | Container Servlet para execução da aplicação. |

### Front-end (Interface)

| Componente | Tecnologia | Função |
| :--- | :--- | :--- |
| **Estrutura** | HTML5 / CSS3 | Criação do layout da interface. |
| **Interação** | JavaScript (ES6+) | Lógica de Front-end, manipulação do DOM. |
| **Comunicação** | Fetch API | Realiza requisições assíncronas (AJAX) para o Back-end Java (API REST). |

---

## 🏛️ Arquitetura do Sistema

O Coristas segue o padrão **MVC (Model-View-Controller)** dentro de uma arquitetura de Três Camadas:

### 1. Camada de Persistência (DAO)
* **Responsabilidade:** Conexão direta com o MySQL, CRUD (Create, Read, Update, Delete) em entidades (`Corista`, `Apresentacao`).
* **Classes:** `CoristaDAO.java`, `ApresentacaoDAO.java`.

### 2. Camada de Serviço (Service/RN)
* **Responsabilidade:** Implementa a **Regra de Negócio (RN)**. O Controlador sempre consulta o Serviço para tomar decisões.
* **Classe:** `CoristaService.java`.
* **RNs Chave:** Verifica se o corista **pode se apresentar** (checa pendências e faltas nos últimos ensaios).

### 3. Camada de Controle (Servlets / API REST)
* **Responsabilidade:** Receber requisições HTTP, chamar as camadas de Serviço/Persistência e formatar a resposta em JSON.
* **Classes:** `CoristaAPIServlet.java`, `AgendaAPIServlet.java`, `PresencaAPIServlet.java`, `LoginAPIServlet.java`.

### 4. Camada de Apresentação (Front-end)
* **Responsabilidade:** Apresentar os dados ao usuário, coletar *input* e fazer requisições `fetch` para a API.
* **Arquivos:** `index.html` (Login), `listar.html` (Dashboard CRUD), `agendar.html`, etc.

---

## 🔑 Funcionalidades e Endpoints Principais

O sistema implementa o CRUD completo para Coristas e Agendamentos.

### Segurança e Login (UC01)
* **Endpoint:** `/api/login` (POST)
* **RNs:** Usuário e senha fixos (`secretaria` / `coral123`).
* **Mecanismo:** Criação de **Sessão HTTP** no sucesso e verificação de **Status 401 (Unauthorized)** em todos os Servlets de API para proteger os dados.

### Gerenciamento de Coristas (CRUD)
* **Endpoint:** `/api/coristas`
* **Métodos:**
    * **GET** (`/api/coristas?id=X` ou sem ID): Lista todos ou busca por ID.
    * **POST**: Cadastra um novo corista.
    * **PUT**: Atualiza um corista existente.
    * **DELETE** (`/api/coristas?id=X`): Exclui um corista.

### Validação de Presença e RNs (UC05)
* **Endpoint:** `/api/presenca`
* **Métodos:**
    * **GET** (`/api/presenca?coristaId=X`): **Valida Aptidão** chamando `CoristaService.podeSeApresentar()`.
    * **POST** (`/api/presenca?coristaId=X`): **Registra Falta** e atualiza o contador de faltas no banco de dados.

### Agenda de Eventos (CRUD)
* **Endpoint:** `/api/agenda`
* **Mecanismo:** Usa adaptadores GSON (`LocalDateTimeAdapter`) para manipular datas e horas de forma segura com o tipo `java.time.LocalDateTime`.

---

## ⚙️ Configuração e Execução

### 1. Pré-requisitos
* JDK 21+
* Apache Tomcat 11+
* MySQL Server (com o banco de dados `crud_java` criado).

### 2. Configuração do Banco
No seu arquivo `src/main/resources/META-INF/persistence.xml`, verifique e ajuste as credenciais:
```xml
<property name="jakarta.persistence.jdbc.user" value="root" />
<property name="jakarta.persistence.jdbc.password" value="SUA_SENHA" />
```

---

## 👨‍💻 Desenvolvedores (Autores)

A tabela abaixo detalha as principais responsabilidades de cada membro da equipe na construção da arquitetura em camadas do sistema Coristas:

| Desenvolvedor | Função Principal | Contribuição Específica | Perfil GitHub |
| :--- | :--- | :--- | :--- |
| **Ana Paula** | **Persistência / Banco de Dados** | Criação do Schema MySQL (`crud_java`), Configuração do JPA/Hibernate e Desenvolvimento dos **DAOs**. | [GitHub](https://github.com/AnaPaula2024) |
| **Cíntia** | **Integração / Configuração** | Colaboração na Configuração do ambiente e Integração dos módulos do Front-end com o Back-end. | [GitHub](https://github.com/cintiacarvv) |
| **Melvin** | **Back-end Principal** | Desenvolvimento dos **Servlets API**, Lógica JSON/Gson e Modelagem da Arquitetura RESTful. | [GitHub](https://github.com/MelvinGomes) |
| **Miguel Luiz** | **Front-end / Desenvolvimento** | Desenvolvimento de páginas web (`cadastrar.html`, `editar.html`), Layout HTML/CSS e Implementação da lógica **Fetch API**. | [GitHub](https://github.com/limmuz) |
| **Nicole** | **Front-end / UI/UX** | Design e Estilização (`CSS`), Desenvolvimento da página **`listar.html`** (Dashboard) e Lógica de **Login/Segurança** (*Client-Side*). | [GitHub](https://github.com/Elociny/) |
| **Tayna** | **Front-end / Lógica JS** | Desenvolvimento da página **`agenda.html`**, Implementação das funções de **CRUD** no JavaScript (Deleção e Edição), e Validação *Client-Side*. | [GitHub](https://github.com/taynaaraujobispo) |
| **Vinicius** | **Integração / Configuração** | Configuração do ambiente (Tomcat, Maven, IntelliJ), Implementação da **Camada de Serviço** (`CoristaService`). | [GitHub](https://github.com/ViniAvarelo) |


