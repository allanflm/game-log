package com.allan.gamelog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.allan.gamelog.domain.Game
import com.allan.gamelog.domain.GameStatus
import java.time.Instant
import java.time.LocalDate

// Representa a tabela "games". Datas viram Long porque o SQLite só guarda
// números e texto. Assim a gente evita criar TypeConverters por enquanto.
@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val platform: String,
    val status: GameStatus,
    val rating: Float?,
    val review: String?,
    // Dias desde 1970-01-01 (LocalDate.toEpochDay)
    val playedAtEpochDay: Long?,
    // Milissegundos desde 1970 (Instant.toEpochMilli)
    val createdAtEpochMilli: Long,
)

fun GameEntity.toDomain() = Game(
    id = id,
    title = title,
    platform = platform,
    status = status,
    rating = rating,
    review = review,
    playedAt = playedAtEpochDay?.let(LocalDate::ofEpochDay),
    createdAt = Instant.ofEpochMilli(createdAtEpochMilli),
)

fun Game.toEntity() = GameEntity(
    id = id,
    title = title,
    platform = platform,
    status = status,
    rating = rating,
    review = review,
    playedAtEpochDay = playedAt?.toEpochDay(),
    createdAtEpochMilli = createdAt.toEpochMilli(),
)
