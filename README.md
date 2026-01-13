# 🛍️ ShopApp

## 1. Visão Geral
**ShopApp** é uma aplicação de e-commerce para **Android**, que simula uma experiência de compra online completa.  

A aplicação permite que os utilizadores:
- Se registem e façam login  
- Naveguem por produtos  
- Filtragem por categorias  
- Adicionem itens ao carrinho  
- Finalizem compras  
- Consultem o histórico de pedidos  

O projeto foi construído com uma **arquitetura modular e escalável**, utilizando:
- **Jetpack Compose** para a interface de utilizador  
- **Hilt** para injeção de dependência  
- **Firebase** para autenticação e persistência de dados  

---

## 2. Funcionalidades Principais

- 🔐 **Autenticação**  
  Sistema de registo e login utilizando **Firebase Authentication**.

- 🛒 **Catálogo de Produtos Dinâmico**  
  Listagem de produtos obtidos em tempo real da API externa **dummyjson.com**.

- 🏷️ **Filtragem por Categoria**  
  Barra de **FilterChip** para refinar os produtos apresentados.

- 📦 **Detalhes do Produto**  
  Ecrã dedicado com:
  - Múltiplas imagens  
  - Descrição detalhada  
  - Controlo de quantidade  

- ♻️ **Carrinho de Compras**  
  Estado do carrinho gerido centralmente pelo **CartRepository**, garantindo consistência em toda a aplicação.

- 💳 **Checkout e Histórico de Pedidos**  
  - Submissão de pedidos  
  - Persistência no **Firebase Firestore**  
  - Consulta do histórico no perfil do utilizador  

- 🧭 **Navegação**  
  Barra de navegação inferior (**BottomBar**) com acesso rápido a:
  - Início  
  - Carrinho  
  - Perfil  

---

## 3. Tecnologias e Arquitetura

A aplicação segue boas práticas de desenvolvimento Android:

### 🔧 Tecnologias
- **Linguagem:** Kotlin  

- **UI:** Jetpack Compose  

- **Injeção de Dependência:** Hilt  
  - Código desacoplado e testável  

- **Backend e Base de Dados:**
  - Firebase Authentication  
  - Firebase Firestore  

- **Comunicação de Rede:**
  - Retrofit  
  - Json  

- **Carregamento de Imagens:** Coil  

- **Navegação:** Jetpack Navigation for Compose  

### 🧱 Arquitetura
Inspirada na **Clean Architecture**, com separação clara de responsabilidades:

- **Camada de UI (Apresentação):**  
  Composables, ViewModels e estados de UI  

- **Camada de Domínio / Repositórios:**  
  Lógica de negócio e abstração das fontes de dados  

- **Camada de Dados:**  
  Comunicação com APIs e Firebase  

---

## 4. Explicação dos Componentes do Projeto

### 4.1 Camada de Dados (`models`, `network`, `repositories`)

#### 📦 models
- **Product.kt**  
  Estrutura de dados de um produto da API dummyjson.

- **CartItem.kt**  
  Representa um item no carrinho (produto + quantidade).

- **Order.kt**  
  Modelo de um pedido finalizado, contendo:
  - ID  
  - Utilizador  
  - Itens  
  - Preço total  

- **User.kt**  
  Estrutura de dados do utilizador armazenada no Firestore.

#### 🌐 network
- **ApiService.kt**  
  Interface Retrofit com os endpoints da API

- **NetworkModule.kt**  
  Configuração singleton do Retrofit:
  - Base URL: `https://dummyjson.com/`
  - Conversor Json  

#### 🗂️ repositories
- **AuthRepository.kt**  
  Centraliza autenticação com Firebase:
  - Login  
  - Registo  
  - Criação do documento do utilizador no Firestore  

- **ProductRepository.kt**  
  Fonte única da verdade para os produtos, consumindo a API.

- **CartRepository.kt**  
  Gestão do carrinho com `MutableStateFlow`:
  - Adicionar  
  - Remover  
  - Atualizar itens  
  - Anotado como `@Singleton`

- **OrderRepository.kt**  
  Gestão de pedidos:
  - `placeOrder()` – cria e guarda pedidos no Firestore  
  - `getOrderHistory()` – obtém pedidos por utilizador, ordenados por data  

- **ProfileRepository.kt**  
  Carrega e guarda dados do perfil do utilizador no Firestore.

---

### 4.2 Camada de UI (`ui`) e Injeção de Dependência (`di`)

#### 🔌 di – Injeção de Dependência
- **AppModule.kt**  
  Módulo Hilt para fornecer dependências externas:
  - FirebaseAuth  
  - FirebaseFirestore  

- **ShopApplication.kt**  
  Classe da aplicação com `@HiltAndroidApp`, inicializa o Hilt.

---

#### 🎨 ui – Interface de Utilizador e ViewModels

##### 📍 Main
- **MainActivity.kt**  
  - Define o `Scaffold` principal  
  - Configura TopBar, BottomBar e NavHost  
  - Controla a visibilidade das barras conforme a rota  

##### 🧩 components
- **MyTopBar.kt**  
  Barra superior com:
  - Título da aplicação  
  - FilterChip de categorias  

- **MyBottomBar.kt**  
  Navegação inferior:
  - Home  
  - Cart  
  - Profile  
  - Badge no carrinho com quantidade de itens  

##### 🔐 login / register
- **LoginView.kt / RegisterView.kt**  
  Ecrãs de autenticação com campos de texto e botões.

- **LoginViewModel.kt / RegisterViewModel.kt**  
  Validação de campos e chamadas ao `AuthRepository`.

##### 🛍️ products
- **ProductListScreen.kt**  
  Lista de produtos agrupados por categoria (ecrã principal).

- **ProductDetailScreen.kt**  
  Detalhes do produto e opção de adicionar ao carrinho.

- **ProductListViewModel.kt**  
  Gestão de produtos, categorias e filtros.

- **ProductDetailViewModel.kt**  
  Carrega dados de um produto e interage com o carrinho.

##### 🛒 cart
- **CartScreen.kt**  
  Mostra:
  - Itens do carrinho  
  - Total  
  - Botão de checkout  

- **CartViewModel.kt**  
  Obtém itens do carrinho e finaliza pedidos via `OrderRepository`.

##### 👤 profile
- **ProfileView.kt**  
  Exibe:
  - Dados do utilizador  
  - Botão de logout  
  - Histórico de pedidos (`OrderCard`)  

- **ProfileViewModel.kt**  
  Gere logout e carrega histórico de pedidos.

---
