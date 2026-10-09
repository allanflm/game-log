package com.allan.gamelog.backend.igdb;

import com.allan.gamelog.backend.config.TwitchProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

// Pega o token de acesso da Twitch (fluxo "client credentials") e guarda em memória.
// O token vale por semanas; pedir um novo a cada busca seria desperdício e a Twitch
// limita esses pedidos.
@Service
public class TwitchTokenService {

    private static final String TOKEN_URL = "https://id.twitch.tv/oauth2/token";
    // Renova um pouco antes de vencer, para nunca mandar um token que expira no meio da chamada.
    private static final long EXPIRY_MARGIN_SECONDS = 60;

    private final TwitchProperties properties;
    private final RestClient restClient = RestClient.create();

    private String token;
    private Instant expiresAt = Instant.EPOCH;

    public TwitchTokenService(TwitchProperties properties) {
        this.properties = properties;
    }

    // synchronized: várias requisições podem chegar juntas, e só uma deve renovar o token.
    public synchronized String getToken() {
        if (token == null || !Instant.now().isBefore(expiresAt)) {
            refresh();
        }
        return token;
    }

    public String clientId() {
        return properties.clientId();
    }

    private void refresh() {
        if (!properties.hasCredentials()) {
            throw new MissingCredentialsException();
        }

        // O secret vai no corpo do POST, não na URL: se a chamada falhar, as mensagens de
        // erro do Spring mostram a URL, e o secret acabaria nos logs.
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", properties.clientId());
        form.add("client_secret", properties.clientSecret());
        form.add("grant_type", "client_credentials");

        TokenResponse response = restClient.post()
                .uri(TOKEN_URL)
                .body(form)
                .retrieve()
                .body(TokenResponse.class);

        token = response.accessToken();
        expiresAt = Instant.now().plusSeconds(response.expiresInSeconds() - EXPIRY_MARGIN_SECONDS);
    }

    private record TokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") long expiresInSeconds) {
    }
}
