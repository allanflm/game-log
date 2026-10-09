# Backend do GameLog

Spring Boot (Java 21). Por enquanto só repassa buscas ao IGDB, guardando as credenciais da
Twitch longe do app.

| Endpoint | O que faz |
|---|---|
| `GET /games/search?q=zelda` | Busca jogos pelo nome |
| `GET /games/popular` | Jogos mais populares do momento |

## Credenciais da Twitch

1. Em <https://dev.twitch.tv/console> crie um aplicativo (redirect URL `http://localhost`).
2. Copie o Client ID e gere um Client Secret.
3. Configure como variáveis de ambiente, **nunca** em arquivo dentro do repositório:

```powershell
$env:TWITCH_CLIENT_ID = "seu-client-id"
$env:TWITCH_CLIENT_SECRET = "seu-client-secret"
```

Sem elas o servidor sobe, mas as buscas respondem `503`.

## Rodando

```powershell
cd backend
.\gradlew.bat bootRun    # sobe em http://localhost:8080
.\gradlew.bat test       # testes
```

O emulador Android enxerga o seu PC em `http://10.0.2.2:8080` (já configurado no app, build debug).
