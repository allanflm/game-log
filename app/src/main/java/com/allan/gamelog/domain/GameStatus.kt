package com.allan.gamelog.domain

// Em que ponto o jogo está na vida do jogador. Enum porque são só 4 valores fixos.
enum class GameStatus {
    BACKLOG,
    PLAYING,
    COMPLETED,
    DROPPED,
}
