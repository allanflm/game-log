package com.allan.gamelog.domain

// Um jogo do catálogo (vindo da busca/populares do IGDB via backend). Não é o mesmo que
// Game: Game é o registro do usuário (nota, status, review), CatalogGame é só a
// informação pública do jogo, sem nada pessoal.
data class CatalogGame(
    val id: Long,
    val title: String,
    val coverUrl: String?,
    val releaseYear: Int?,
    val genres: List<String>,
)
