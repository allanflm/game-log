package com.allan.gamelog.ui.screens.gamelist

import com.allan.gamelog.domain.Game

// Tudo o que a tela de lista precisa saber para se desenhar. A tela só lê isto,
// nunca consulta o banco.
data class GameListUiState(
    val games: List<Game> = emptyList(),
    // Começa true porque o primeiro valor do banco ainda não chegou. Sem isso, a tela
    // piscaria "nenhum jogo ainda" antes de a lista aparecer.
    val isLoading: Boolean = true,
)
