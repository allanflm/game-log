# CLAUDE.md

Contexto e regras para trabalhar neste projeto. Leia também o `PLANO.md`, que tem as etapas e o que já foi feito. A pasta `referencias/` tem prints do Letterboxd que guiam navegação e layout das telas (o visual é próprio, sem copiar a marca).

## Sobre o projeto

**GameLog** é um app Android nativo, estilo Letterboxd, só que de jogos: registrar jogos, dar nota, escrever reviews, montar listas e acompanhar um diário. É um projeto pessoal de aprendizado, então o código precisa ser claro e fácil de entender, não só funcional.

## Sobre quem está programando

- Brasileiro, comunica em português do Brasil, estilo casual e direto. Responda em português.
- Desenvolvedor novo em Android e Kotlin. Tem boa base de SQL e alguma de Java, então analogias com Java e SQL ajudam (Room ≈ SQLite com anotações, por exemplo).
- Quer entender o que está sendo feito. Ao introduzir um conceito novo (ViewModel, StateFlow, remember, Flow, etc.), explique em poucas linhas o que é e por que está sendo usado.

## Como trabalhar

- Siga o `PLANO.md`: uma etapa por vez, sem adiantar funcionalidades de etapas futuras.
- Mudanças pequenas e incrementais. Prefira várias alterações curtas a uma grande.
- Antes de uma mudança grande (nova dependência, mudança de arquitetura, migração de banco), explique o plano e espere confirmação.
- Não adicione bibliotecas que não estejam no plano sem perguntar.
- Ao terminar uma tarefa, marque a caixinha correspondente no `PLANO.md`.
- Se algo estiver ambíguo, pergunte em vez de assumir.

## Stack

- Kotlin, Jetpack Compose (Material 3)
- Navigation Compose
- ViewModel + StateFlow (MVVM)
- Room com KSP
- Hilt (a partir da etapa 2)
- Retrofit (etapa 3, conta e login) + Coil (etapa 4)
- Backend Spring Boot + Postgres, auth com JWT (etapa 3)
- Build em Kotlin DSL, dependências no version catalog `gradle/libs.versions.toml`
- Pacote base: `com.allan.gamelog`, minSdk 26

## Arquitetura

Organização por camada, com fluxo de dados em uma direção:

```
ui (Composable) → ViewModel → Repository → DAO / API
```

```
app/src/main/java/com/allan/gamelog/
├── data/
│   ├── local/        # Entity, DAO, Database (Room)
│   ├── remote/       # Retrofit (etapa 3)
│   └── repository/   # Repositories
├── domain/           # Modelos de domínio e regras simples
└── ui/
    ├── navigation/   # NavHost e rotas
    ├── theme/
    ├── components/   # Composables reutilizáveis (ex.: RatingBar)
    └── screens/      # Uma pasta por tela: Screen + ViewModel + UiState
```

Regras:
- Composables **não** acessam o banco nem o Repository diretamente. Só o ViewModel faz isso.
- Cada tela tem um `UiState` (data class) exposto pelo ViewModel como `StateFlow`.
- A UI coleta o estado com `collectAsStateWithLifecycle()`.
- Eventos da UI chegam ao ViewModel por funções (`onSaveClick()`, `onStatusChange(...)`).
- O banco expõe `Flow` para a lista atualizar sozinha quando os dados mudam.

## Convenções de código

- Nomes de código (classes, funções, variáveis) em inglês. Textos exibidos ao usuário em português, em `strings.xml`.
- Composables em PascalCase, um arquivo por tela.
- Prefira `val` a `var` e classes imutáveis (`data class`).
- Enums para valores fixos, como `GameStatus { BACKLOG, PLAYING, COMPLETED, DROPPED }`.
- Evite lógica pesada dentro de Composables.
- Comentários explicam o "porquê", não o "o quê".
- Sem números e strings soltos no código: use constantes ou recursos.

## Modelo de dados (etapa 1)

`Game`: `id`, `title`, `platform`, `status`, `rating` (0,5 a 5,0 em passos de 0,5, nulo se não avaliado), `review`, `playedAt`, `createdAt`.

Todo campo novo no banco exige **migração do Room**. Nunca usar `fallbackToDestructiveMigration()`, porque apagaria os dados do usuário.

## Comandos úteis

```bash
./gradlew assembleDebug        # compilar
./gradlew test                 # testes unitários
./gradlew connectedAndroidTest # testes instrumentados (precisa do emulador ligado)
./gradlew lint                 # análise estática
```

No Windows, use `gradlew.bat` no lugar de `./gradlew`.

## Git

- Commits pequenos, um por tarefa, com mensagem curta em português no imperativo (ex.: "Adiciona DAO de jogos").
- Não commitar `local.properties`, `.gradle/`, `build/` nem chaves de API.

## Segredos

- Credenciais do IGDB/Twitch **nunca** vão para dentro do app nem para o repositório. Ficam só no backend, em variável de ambiente.
- Se precisar de uma chave durante o desenvolvimento, usar `local.properties` (que está no `.gitignore`).
