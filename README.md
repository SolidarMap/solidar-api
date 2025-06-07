<a id="readme-top"></a>

# 📱 Challange - Mottu - Java API - Find Mottu

![Static Badge](https://img.shields.io/badge/build-passing-brightgreen) ![Static Badge](https://img.shields.io/badge/Version-1.0.0-black) ![License](https://img.shields.io/badge/license-MIT-lightgrey)

## 🧑‍🤝‍🧑 Informações dos Contribuintes

| Nome | Matricula | Turma |
| :------------: | :------------: | :------------: |
| Pedro Herique Vasco Antonieti | 556253 | 2TDSPH |
<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 🚩 Características

API RESTful desenvolvida em Java com o framework Spring Boot, focada na gestão de pátios do SolidarMap. O SolidarMap é um sistema que conecta pessoas em situações de emergência com recursos essenciais como comida, abrigo, remédios e água. A plataforma utiliza localização geográfica, comunicação direta e avaliações para promover a confiança e agilidade em momentos críticos.
<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 🛠️ Tecnologias Utilizadas

![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 💻 Inicializar projeto

Para iniciar o projeto faz-se necessário seguir algumas etapas abaixo:

### 📝 Pré-requisitos

- Java 17+
- Maven 3.5.0+
- IDE (como IntelliJ, Eclipse ou VS Code)

### 🗃️ Instalação
1. Clone o repositório para a sua pasta:
    ```sh
    git clone https://github.com/SolidarMap/solidar-api.git
    ```
2. Acesse a pasta onde você colocou seu projeto:
    ```sh
    cd solidar-api
    ```
3. Copile e execute o projeto (caso possua Maven instalado):
    ```sh
    mvn spring-boot:run
    ```
4. Acesse o Swagger para testar os endpoints:
    ```text
    http://localhost:8080/swagger-ui/index.html
    ```
📌 **OBS:** O projeto utiliza Banco de Dados Oracle da FIAP. Veja o modelo do `.env.example` para inserir o `USUARIO` e `SENHA`. Após configurar, renomeie o arquivo para `.env`.

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 🧑‍💻 Executando o projeto via IDE

Você pode rodar o projeto diretamente pelas IDEs abaixo:

### 🟦 IntelliJ IDEA

1. Abra o IntelliJ IDEA.
2. Clique em **File > Open** e selecione a pasta `solidar-api`.
3. O IntelliJ detectará o projeto Maven e fará a importação automática.
4. Certifique-se de que o SDK está como **Java 17 ou superior**:
   - Vá em **File > Project Structure > Project SDK**.
5. Navegue até a classe principal:
   - `src/main/java/br/com/solidarmap/solidar_api/SolidarApiApplication.java`
6. Clique com o botão direito na classe e selecione **Run 'SolidarApiApplication'**.
7. O projeto será iniciado e poderá ser acessado em:
   - `http://localhost:8080/swagger-ui/index.html`

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

---

### 🟨 Eclipse
1. Abra o Eclipse.
2. Vá em **File > Import > Maven > Existing Maven Projects**.
3. Selecione a pasta `solidar-api` que foi clonada.
4. Clique em **Finish** para importar.
5. Certifique-se de que o JDK está como **Java 17 ou superior**:
   - Clique com o botão direito no projeto → **Build Path > Configure Build Path**.
6. Navegue até a classe:
    - `src/main/java/br/com/solidarmap/solidar_api/SolidarApiApplication.java`
7. Clique com o botão direito nela → **Run As > Java Application**.
8. A aplicação será iniciada em:
    - `http://localhost:8080/swagger-ui/index.html`

---

### 🪟 Dica para Windows com Maven Wrapper
Se estiver usando Windows e **não tem Maven instalado**, use o Maven Wrapper:

```cmd
.\mvnw.cmd spring-boot:run
```
   
<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

