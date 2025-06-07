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

Para iniciar o projeto faz se necessário seguir algumas etapas abaixo:

### 📝 Pré-requisitos

- Java 17+
- Maven 3.5.0+
- IDE (como IntelliJ, Eclipse ou VS Code)

### 🗃️ Instalação
1. Clone o repositório para a sua pasta:
    ```sh
    https://github.com/SolidarMap/solidar-api.git
    ```
2. Acesse a pasta onde você colocou seu projeto.
3. Copile e execute o projeto:
   ```sh
   mvn spring-boot:run
   ```
4. Acesse o Swagger para testar os endpoints:
   ```link
   http://localhost:8080/swagger-ui/index.html
   ```
   OBS: Projeto utiliza Banco de Dados Oracle da FIAP, veja o modelo do .env.example para realizar a inserção do `Usuário` e `Senha`. (Após colocar as credenciais do Database, tire o `.example` do .env.)
<p align="right"><a href="#readme-top">Voltar ao topo</a></p>
