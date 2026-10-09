package com.allan.gamelog.data.remote

import com.allan.gamelog.domain.CatalogGame
import kotlinx.serialization.Serializable

// DTO (Data Transfer Object): o formato exato do JSON que o backend manda. Fica separado
// do modelo de domínio para que mudanças na API não se espalhem pelo app.
// @Serializable gera, em tempo de compilação, o código que lê o JSON para esta classe.
@Serializable
data class CatalogGameDto(
    val id: Long,
    val title: String,
    val coverUrl: String? = null,
    val releaseYear: Int? = null,
    val genres: List<String> = emptyList(),
)

fun CatalogGameDto.toDomain() = CatalogGame(
    id = id,
    title = title,
    coverUrl = coverUrl,
    releaseYear = releaseYear,
    genres = genres,
)
