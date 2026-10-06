package com.allan.gamelog.domain

import java.time.Instant
import java.time.LocalDate

// Modelo de domínio: é o que o app inteiro usa. Não tem nada de Room aqui,
// de propósito. A Entity do banco vai ser uma classe separada em data/local.
data class Game(
    val id: Long = 0,
    val title: String,
    val platform: String,
    val status: GameStatus,
    // Nota de 0,5 a 5,0 em passos de 0,5. Nulo enquanto o jogo não foi avaliado.
    val rating: Float? = null,
    val review: String? = null,
    // Dia em que jogou ou zerou. Opcional, então só LocalDate (sem hora).
    val playedAt: LocalDate? = null,
    val createdAt: Instant = Instant.now(),
)
