# GameLog

App Android nativo, estilo Letterboxd, só que de jogos: registrar o que jogou, dar nota, escrever reviews, montar listas e acompanhar o próprio diário.

É um projeto pessoal de aprendizado de Android moderno (Kotlin, Compose, Room), com backend em Spring Boot nas etapas finais.

## Status

Etapa 0 (ambiente) concluída. Próximo passo: Etapa 1, o app offline (MVP).
As etapas e tarefas estão no [PLANO.md](PLANO.md).

## Stack

- Kotlin e Jetpack Compose (Material 3)
- Navigation Compose
- ViewModel + StateFlow (MVVM)
- Room com KSP
- Build em Kotlin DSL, dependências no version catalog (`gradle/libs.versions.toml`)
- Planejado para as próximas etapas: Hilt, Retrofit, Coil e backend em Spring Boot + Postgres

Pacote base: `com.allan.gamelog`, minSdk 26.

## Arquitetura

O fluxo de dados vai em uma direção só:

```
ui (Composable) → ViewModel → Repository → DAO / API
```

Estrutura de pastas planejada:

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
    ├── components/   # Composables reutilizáveis
    └── screens/      # Uma pasta por tela: Screen + ViewModel + UiState
```

## Como rodar

1. Abra o projeto no Android Studio.
2. Ligue um emulador (o plano usa um Pixel 7) ou conecte um celular.
3. Rode o app pelo Android Studio ou compile pelo terminal:

```bash
gradlew.bat assembleDebug        # compilar
gradlew.bat test                 # testes unitários
gradlew.bat connectedAndroidTest # testes instrumentados (emulador ligado)
gradlew.bat lint                 # análise estática
```

No Linux e no macOS, use `./gradlew` no lugar de `gradlew.bat`.

## Segredos

As credenciais do IGDB/Twitch nunca vão para dentro do app nem para o repositório. Ficam só no backend, em variável de ambiente. Durante o desenvolvimento, chaves locais vão no `local.properties`, que está no `.gitignore`.
