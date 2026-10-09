package com.allan.gamelog.backend.igdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.allan.gamelog.backend.games.GameSummary;
import java.util.List;
import org.junit.jupiter.api.Test;

class IgdbClientTest {

    @Test
    void escapeBlocksQuoteThatWouldCloseTheSearchString() {
        assertEquals("zelda\\\"; fields *; \\\"", IgdbClient.escape("zelda\"; fields *; \""));
    }

    @Test
    void escapeDoublesBackslashes() {
        assertEquals("a\\\\b", IgdbClient.escape("a\\b"));
    }

    @Test
    void toSummaryMapsAllFields() {
        // 1_490_918_400 = 31/03/2017 em UTC (lançamento de Zelda: Breath of the Wild)
        var game = new IgdbClient.IgdbGame(
                7346,
                "The Legend of Zelda: Breath of the Wild",
                new IgdbClient.Cover("co3p2d"),
                1_490_918_400L,
                List.of(new IgdbClient.Genre("Adventure")));

        GameSummary summary = IgdbClient.toSummary(game);

        assertEquals(7346, summary.id());
        assertEquals("The Legend of Zelda: Breath of the Wild", summary.title());
        assertEquals("https://images.igdb.com/igdb/image/upload/t_cover_big/co3p2d.jpg", summary.coverUrl());
        assertEquals(2017, summary.releaseYear());
        assertEquals(List.of("Adventure"), summary.genres());
    }

    @Test
    void toSummaryHandlesMissingOptionalFields() {
        var game = new IgdbClient.IgdbGame(1, "Jogo sem dados", null, null, null);

        GameSummary summary = IgdbClient.toSummary(game);

        assertNull(summary.coverUrl());
        assertNull(summary.releaseYear());
        assertTrue(summary.genres().isEmpty());
    }
}
