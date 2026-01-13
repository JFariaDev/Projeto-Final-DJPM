# 🛍️ ShopApp - E-Commerce Android

**ShopApp** é uma aplicação de comércio eletrónico para a plataforma Android, desenvolvida inteiramente com tecnologias modernas do ecossistema Kotlin. O objetivo é fornecer uma experiência de compra móvel, fluida e completa.

O projeto utiliza **Jetpack Compose** para a interface de utilizador e **Firebase** para o backend.

---

## ✨ Funcionalidades Principais

* **Autenticação:** Registo e login via Firebase Authentication.
* **Listagem de Produtos:** Dados obtidos via API externa (`dummyjson.com`).
* **Filtragem Dinâmica:** Filtros por categoria usando `FilterChip`.
* **Carrinho de Compras:** Gestão de estado em tempo real.
* **Checkout:** Finalização de pedidos com persistência no Firestore.
* **Histórico de Pedidos:** Consulta de compras anteriores no perfil do utilizador.

---

## 🛠️ Tecnologias e Arquitetura

Este projeto segue os princípios da **Clean Architecture** e utiliza:

* **UI:** Jetpack Compose (Declarativo)
* **Linguagem:** Kotlin (Coroutines & Flow)
* **Injeção de Dependência:** Hilt
* **Base de Dados & Auth:** Firebase Firestore & Auth
* **Rede:** Retrofit & Gson
* **Navegação:** Jetpack Navigation for Compose
* **Imagens:** Coil

---

## 📂 Estrutura do Projeto

* `models`: Classes de dados (`Product`, `User`, etc).
* `network`: Configuração do Retrofit e `ApiService`.
* `repositories`: Camada de abstração de dados (Auth, Product, Cart, Order).
* `ui`: Componentes e ecrãs organizados por *feature* (login, products, cart).
