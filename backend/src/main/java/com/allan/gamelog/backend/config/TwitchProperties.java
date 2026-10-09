package com.allan.gamelog.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Credenciais do app registrado na Twitch (o IGDB é da Twitch). Vêm de variáveis de
// ambiente (veja application.properties) e nunca ficam no código nem no git.
@ConfigurationProperties(prefix = "twitch")
public record TwitchProperties(String clientId, String clientSecret) {

    public boolean hasCredentials() {
        return clientId != null && !clientId.isBlank()
                && clientSecret != null && !clientSecret.isBlank();
    }
}
