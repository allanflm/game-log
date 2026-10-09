package com.allan.gamelog.backend.igdb;

import com.allan.gamelog.backend.games.GameSummary;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

// Conversa com a API do IGDB. As consultas usam a linguagem deles (Apicalypse), que é um
// texto enviado no corpo do POST, parecido com um SQL simplificado.
@Component
public class IgdbClient {

    private static final String GAMES_URL = "https://api.igdb.com/v4/games";
    private static final String POPULARITY_URL = "https://api.igdb.com/v4/popularity_primitives";
    private static final String COVER_URL_TEMPLATE = "https://images.igdb.com/igdb/image/upload/t_cover_big/%s.jpg";
    private static final String GAME_FIELDS = "fields name, cover.image_id, first_release_date, genres.name;";
    private static final int SEARCH_LIMIT = 20;
    private static final int POPULAR_LIMIT = 18;
    // Tipo de popularidade do IGDB: 1 = visitas à página do jogo no próprio IGDB.
    private static final int POPULARITY_TYPE_VISITS = 1;

    private final TwitchTokenService tokenService;
    private final RestClient restClient = RestClient.create();

    public IgdbClient(TwitchTokenService tokenService) {
        this.tokenService = tokenService;
    }

    public List<GameSummary> search(String query) {
        String body = "search \"" + escape(query) + "\"; " + GAME_FIELDS + " limit " + SEARCH_LIMIT + ";";
        return post(GAMES_URL, body, new ParameterizedTypeReference<List<IgdbGame>>() { })
                .stream()
                .map(IgdbClient::toSummary)
                .toList();
    }

    public List<GameSummary> popular() {
        // Duas chamadas: primeiro o ranking (só ids), depois os dados dos jogos desses ids.
        String rankingBody = "fields game_id, value; where popularity_type = " + POPULARITY_TYPE_VISITS
                + "; sort value desc; limit " + POPULAR_LIMIT + ";";
        List<Long> rankedIds = post(POPULARITY_URL, rankingBody, new ParameterizedTypeReference<List<PopularityEntry>>() { })
                .stream()
                .map(PopularityEntry::gameId)
                .toList();
        if (rankedIds.isEmpty()) {
            return List.of();
        }

        String ids = rankedIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        String gamesBody = GAME_FIELDS + " where id = (" + ids + "); limit " + POPULAR_LIMIT + ";";
        Map<Long, IgdbGame> gamesById = post(GAMES_URL, gamesBody, new ParameterizedTypeReference<List<IgdbGame>>() { })
                .stream()
                .collect(Collectors.toMap(IgdbGame::id, Function.identity()));

        // O IGDB devolve os jogos sem ordem garantida, então reaplicamos a ordem do ranking.
        return rankedIds.stream()
                .map(gamesById::get)
                .filter(game -> game != null)
                .map(IgdbClient::toSummary)
                .toList();
    }

    private <T> T post(String url, String body, ParameterizedTypeReference<T> responseType) {
        return restClient.post()
                .uri(url)
                .header("Client-ID", tokenService.clientId())
                .header("Authorization", "Bearer " + tokenService.getToken())
                .contentType(MediaType.TEXT_PLAIN)
                .body(body)
                .retrieve()
                .body(responseType);
    }

    // O texto do usuário entra dentro de aspas na consulta. Sem escapar, alguém poderia
    // fechar as aspas e injetar outros comandos (a mesma ideia de SQL injection).
    static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    static GameSummary toSummary(IgdbGame game) {
        String coverUrl = game.cover() == null
                ? null
                : COVER_URL_TEMPLATE.formatted(game.cover().imageId());
        // O IGDB manda a data de lançamento em segundos desde 1970 (timestamp Unix).
        Integer releaseYear = game.firstReleaseDate() == null
                ? null
                : Instant.ofEpochSecond(game.firstReleaseDate()).atZone(ZoneOffset.UTC).getYear();
        List<String> genres = game.genres() == null
                ? List.of()
                : game.genres().stream().map(Genre::name).toList();
        return new GameSummary(game.id(), game.name(), coverUrl, releaseYear, genres);
    }

    // Formatos da resposta do IGDB. Só declaramos os campos que usamos; o resto é ignorado.
    record IgdbGame(
            long id,
            String name,
            Cover cover,
            @JsonProperty("first_release_date") Long firstReleaseDate,
            List<Genre> genres) {
    }

    record Cover(@JsonProperty("image_id") String imageId) {
    }

    record Genre(String name) {
    }

    record PopularityEntry(@JsonProperty("game_id") long gameId) {
    }
}
