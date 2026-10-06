# GameLog: plano do projeto

App Android nativo, estilo Letterboxd, só que de jogos. Registrar o que jogou, dar nota, escrever review, montar listas e acompanhar o próprio diário.

**Objetivo duplo:** ter um app que eu realmente use e aprender Android moderno (Kotlin, Compose, Room) de ponta a ponta, com backend em Spring Boot nas etapas finais.

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| UI | Jetpack Compose (Material 3) |
| Navegação | Navigation Compose |
| Estado | ViewModel + StateFlow (MVVM) |
| Banco local | Room (com KSP) |
| Injeção de dependência | Hilt (entra na etapa 2, quando a estrutura estiver estável) |
| Rede (etapa 3) | Retrofit + Coil para imagens |
| Backend (etapas 3 e 4) | Spring Boot + Postgres |

Configuração inicial: pacote `com.allan.gamelog`, Minimum SDK API 26, build em Kotlin DSL, dependências num version catalog (`gradle/libs.versions.toml`).

---

## Etapa 0: ambiente

- [x] Instalar Android Studio
- [x] Criar emulador (Pixel 7)
- [ ] Criar projeto "GameLog" com o template Empty Activity
- [ ] Rodar o "Hello Android!" no emulador
- [ ] Criar o repositório Git e fazer o primeiro commit

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
- [ ] Estrutura de pastas por camada (`data`, `domain`, `ui`)
- [ ] Entity, DAO e Database do Room
- [ ] Repository expondo `Flow<List<Game>>`
- [ ] Tela de lista com cards
- [ ] Tela de adicionar/editar jogo (formulário)
- [ ] Tela de detalhe com nota, status e review
- [ ] Componente de nota em estrelas
- [ ] Deletar jogo (com confirmação)
- [ ] ViewModels com StateFlow para cada tela
- [ ] Navegação entre as três telas

**Pronto quando:** consigo fechar o app, abrir de novo e meus jogos continuam lá.

## Etapa 2: diário e organização

**Entrega:** a parte "Letterboxd" de verdade.

- [ ] Diário: timeline agrupada por mês do que joguei
- [ ] Filtros por status e por plataforma
- [ ] Ordenação (data, nota, título)
- [ ] Listas personalizadas (ex.: "Melhores de 2026", "Pra jogar nas férias")
- [ ] Estatísticas simples: jogos zerados no ano, nota média, plataforma mais jogada
- [ ] Migração de Hilt para injeção de dependência
- [ ] Testes unitários dos ViewModels e instrumentados do DAO
- [ ] Tema claro/escuro

## Etapa 3: busca de jogos (IGDB)

**Entrega:** buscar o jogo pelo nome e já trazer capa, ano e gênero.

O IGDB exige credenciais de desenvolvedor da Twitch. Elas **não podem ficar dentro do app**, então entra um backend pequeno que guarda as credenciais e repassa as buscas.

- [ ] Backend Spring Boot: endpoint `GET /games/search?q=`
- [ ] Backend: autenticação na Twitch (client credentials) com cache do token
- [ ] Backend: deploy (Render, Railway, Fly.io ou similar)
- [ ] App: Retrofit consumindo o backend
- [ ] App: tela de busca e "adicionar a partir do resultado"
- [ ] App: capas com Coil e cache em disco
- [ ] App: campos novos no Room (`coverUrl`, `releaseYear`, `genres`, `igdbId`) com migração

## Etapa 4 (opcional): social

- [ ] Cadastro e login (JWT)
- [ ] Sincronizar o diário com o backend (Postgres)
- [ ] Perfis públicos
- [ ] Seguir amigos e feed de atividade
- [ ] Publicação na Play Store (conta de desenvolvedor, ícone, política de privacidade)

---

## Decisões em aberto

- Nome final do app (GameLog é provisório)
- Backend em Spring Boot confirmado, mas o provedor de deploy ainda não foi escolhido
- Se vou sincronizar na nuvem já na etapa 3 ou só na 4

## Como trabalhar

- Uma etapa por vez, com algo rodando no emulador ao final de cada uma
- Commits pequenos, um por tarefa
- Marcar as caixinhas deste arquivo conforme avança
