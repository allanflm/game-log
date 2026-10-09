package com.allan.gamelog.backend.games;

import com.allan.gamelog.backend.igdb.IgdbClient;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameController {

    private final IgdbClient igdbClient;

    public GameController(IgdbClient igdbClient) {
        this.igdbClient = igdbClient;
    }

    @GetMapping("/search")
    public List<GameSummary> search(@RequestParam("q") String query) {
        // Busca vazia nem chega ao IGDB, para não gastar a cota de chamadas à toa.
        if (query.isBlank()) {
            return List.of();
        }
        return igdbClient.search(query.trim());
    }

    @GetMapping("/popular")
    public List<GameSummary> popular() {
        return igdbClient.popular();
    }
}
