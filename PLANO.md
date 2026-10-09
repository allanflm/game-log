# GameLog: plano do projeto

App Android nativo, estilo Letterboxd, só que de jogos. Registrar o que jogou, dar nota, escrever review, montar listas, acompanhar o próprio diário, com conta de usuário e parte social.

**Objetivo duplo:** ter um app que eu realmente use e aprender Android moderno (Kotlin, Compose, Room) de ponta a ponta, com backend em Spring Boot.

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| UI | Jetpack Compose (Material 3) |
| Navegação | Navigation Compose |
| Estado | ViewModel + StateFlow (MVVM) |
| Banco local | Room (com KSP) |
| Injeção de dependência | Hilt (entra na etapa 2, quando a estrutura estiver estável) |
| Rede (etapa 3 em diante) | Retrofit; Coil para imagens (etapa 4) |
| Backend (etapas 3 a 5) | Spring Boot + Postgres |
| Autenticação | JWT, senha com hash (BCrypt) no backend |

Configuração inicial: pacote `com.allan.gamelog`, Minimum SDK API 26, build em Kotlin DSL, dependências num version catalog (`gradle/libs.versions.toml`).

## Referências de design

Prints do app Letterboxd ficam em `referencias/`. Usamos como guia de **navegação e layout**; o visual (nome, logo, cores, identidade) é próprio, sem copiar a marca deles.

| Print | O que mostra | Vira no GameLog |
|---|---|---|
| `pagina de login.png` | Tela de entrada: "Sign in", "Create account", "Open the tour" | Tela de boas-vindas (etapa 3) |
| `pagina-inicial do letterbox.png` | Abas Films / Reviews / Lists / Journal, grade "Popular this week", barra inferior com 5 ícones (início, busca, adicionar, atividade, perfil) | Home e barra de navegação inferior |
| `pagina do perfil do usuario.png` | Abas Profile / Diary / Lists / Watchlist, avatar, bio, 4 favoritos, atividade recente | Tela de perfil (etapas 2 e 5) |
| `layout-do-projeto.png` (Figma, gratuito) | Splash; home com grade de 2 colunas (pôster, nota em selo laranja, título, gênero); botão "Log in" / "Profile" no topo; tema escuro com laranja de destaque | Splash, home pública e card de jogo. Conferir a licença do arquivo e não copiar o logo |

---

## Etapa 0: ambiente

- [x] Instalar Android Studio
- [x] Criar emulador (Pixel 7)
- [x] Criar projeto "GameLog" com o template Empty Activity
- [x] Rodar o "Hello Android!" no emulador
- [x] Criar o repositório Git e fazer o primeiro commit

## Etapa 1: app offline (MVP)

**Entrega:** cadastrar jogos à mão, ver a lista e abrir o detalhe, tudo salvo no celular.

**Modelo de dados (`Game`):**
- `id`, `title`, `platform`
- `status`: `BACKLOG`, `PLAYING`, `COMPLETED`, `DROPPED`
- `rating`: de 0,5 a 5,0 em passos de 0,5 (nulo enquanto não avaliado)
- `review`: texto opcional
- `playedAt`: data em que jogou ou zerou (opcional)
- `createdAt`

**Tarefas:**
- [x] Estrutura de pastas por camada (`data`, `domain`, `ui`)
- [x] Entity, DAO e Database do Room
- [x] Repository expondo `Flow<List<Game>>`
- [x] Tela de lista com cards
- [ ] Tela de adicionar/editar jogo (formulário)
- [ ] Tela de detalhe com nota, status e review
- [ ] Componente de nota em estrelas
- [ ] Deletar jogo (com confirmação)
- [ ] ViewModels com StateFlow para cada tela
- [ ] Navegação entre as três telas

**Pronto quando:** consigo fechar o app, abrir de novo e meus jogos continuam lá.

## Etapa 2: diário e organização

**Entrega:** a parte "Letterboxd" de verdade, ainda sem conta.

- [ ] Barra de navegação inferior (início, busca, adicionar, atividade, perfil)
- [ ] Diário: timeline agrupada por mês do que joguei
- [ ] Filtros por status e por plataforma
- [ ] Ordenação (data, nota, título)
- [ ] Listas personalizadas (ex.: "Melhores de 2026", "Pra jogar nas férias")
- [ ] Favoritos: escolher 4 jogos para o perfil
- [ ] Estatísticas simples: jogos zerados no ano, nota média, plataforma mais jogada
- [ ] Migração para Hilt (injeção de dependência)
- [ ] Testes unitários dos ViewModels e instrumentados do DAO
- [ ] Tema claro/escuro

## Etapa 3: conta e login (backend)

**Entrega:** criar conta, entrar e sair, com os dados do usuário ligados à conta.

Senha nunca fica no app: quem guarda (com hash) e valida é o backend. O app só guarda o token (JWT) depois do login.

**Backend (Spring Boot + Postgres):**
- [ ] Projeto Spring Boot e banco Postgres local
- [ ] Tabela de usuários (`id`, `username`, `email`, `passwordHash`, `createdAt`)
- [ ] `POST /auth/register` (username, e-mail, senha) com validação e e-mail único
- [ ] `POST /auth/login` devolvendo o JWT
- [ ] Hash de senha com BCrypt e filtro que valida o JWT nas rotas protegidas
- [ ] `GET /me` devolvendo o usuário logado
- [ ] Deploy (Render, Railway, Fly.io ou similar)

**App:**
- [ ] Retrofit consumindo o backend
- [ ] Tela de boas-vindas (Sign in / Create account), conforme `referencias/pagina de login.png`
- [ ] Telas de cadastro e de login (e-mail, senha, mensagens de erro)
- [ ] Guardar o token de forma segura e restaurar a sessão ao abrir o app
- [ ] Logout
- [ ] Home pública: sem sessão o app mostra a home normalmente, com botão "Log in" no topo (vira "Profile" quando logado), conforme `layout-do-projeto.png`
- [ ] Rotas protegidas: ações que dependem de conta (perfil, seguir, sincronizar) levam à tela de boas-vindas

**Exigências para um lançamento futuro (decidir antes de fechar a etapa):**
- [ ] Recuperação de senha (fluxo "esqueci minha senha" por e-mail)
- [ ] Verificação de e-mail no cadastro
- [ ] Excluir conta: dentro do app e também por um link na web (a Play Store exige para apps com conta)
- [ ] Apagar os dados do usuário no backend quando a conta for excluída (LGPD)

**Decisões a tomar nesta etapa:** onde guardar o token no app; qual serviço envia os e-mails.

## Etapa 4: busca de jogos (IGDB)

**Entrega:** buscar o jogo pelo nome e já trazer capa, ano e gênero.

O IGDB exige credenciais de desenvolvedor da Twitch. Elas **não podem ficar dentro do app**, então o mesmo backend da etapa 3 guarda as credenciais e repassa as buscas.

- [ ] Backend: endpoint `GET /games/search?q=`
- [ ] Backend: autenticação na Twitch (client credentials) com cache do token
- [ ] App: tela de busca e "adicionar a partir do resultado"
- [ ] App: capas com Coil e cache em disco
- [ ] App: grade "Popular this week" na home
- [ ] App: campos novos no Room (`coverUrl`, `releaseYear`, `genres`, `igdbId`) com migração

## Etapa 5: social

**Entrega:** perfis públicos, seguir amigos e feed, como no Letterboxd.

- [ ] Sincronizar o diário, as reviews e as listas com o backend (Postgres)
- [ ] Perfil: avatar, bio, favoritos e atividade recente, conforme `referencias/pagina do perfil do usuario.png`
- [ ] Perfil público de outros usuários, com opção de privacidade
- [ ] Seguir e deixar de seguir
- [ ] Feed de atividade de quem eu sigo

## Etapa 6 (opcional): preparar o lançamento

**Entrega:** o app pronto para publicar na Play Store, gratuito, para a comunidade e como portfólio. Os termos das APIs e da loja mudam, então conferir cada item na fonte oficial na hora.

- [ ] Revisar os termos de uso da IGDB (e, se usada, da RAWG) para um app gratuito e sem monetização, incluindo créditos obrigatórios
- [ ] Portfólio: repositório público com README bom (prints, stack, arquitetura, como rodar), sem segredos no histórico do git
- [ ] Portfólio: vídeo curto ou GIFs do app e link do backend no ar
- [ ] Nome, ícone e identidade visual definitivos, sem semelhança com o Letterboxd; checar se o nome já existe na Play Store
- [ ] Política de privacidade publicada numa URL
- [ ] Formulário "Data safety" da Play Store (dados coletados: e-mail, etc.)
- [ ] HTTPS, backups e monitoramento do backend em produção
- [ ] Conta de desenvolvedor na Play Store
- [ ] Teste fechado com testadores (conferir o mínimo exigido na época)
- [ ] Publicar na produção

---

## Decisões tomadas

- Backend em Spring Boot + Postgres, com login por e-mail e senha (JWT)
- App gratuito e sem monetização, para a comunidade usar e como portfólio
- Visual próprio, usando o Letterboxd e o layout do Figma só como referência de navegação e layout
- Navegação com barra inferior (estilo Letterboxd), e home pública com botão de login no topo (estilo Figma)

## Decisões em aberto

- Nome final do app (GameLog é provisório) e identidade visual própria
- Provedor de deploy do backend
- Como sincronizar os dados que já estão no Room quando o usuário criar conta

## Como trabalhar

- Uma etapa por vez, com algo rodando no emulador ao final de cada uma
- Commits pequenos, um por tarefa
- Marcar as caixinhas deste arquivo conforme avança
