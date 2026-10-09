package com.allan.gamelog.backend.games;

import java.util.List;

// O que o app recebe de cada jogo. É de propósito bem menor que a resposta do IGDB:
// o app não precisa conhecer o formato deles, só este. coverUrl e releaseYear podem ser nulos.
public record GameSummary(
        long id,
        String title,
        String coverUrl,
        Integer releaseYear,
        List<String> genres) {
}
