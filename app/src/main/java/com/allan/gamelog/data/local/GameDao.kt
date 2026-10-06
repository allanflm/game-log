package com.allan.gamelog.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    // Flow: o Room avisa de novo toda vez que a tabela muda, e a lista atualiza sozinha.
    @Query("SELECT * FROM games ORDER BY createdAtEpochMilli DESC")
    fun observeAll(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :id")
    fun observeById(id: Long): Flow<GameEntity?>

    // Insere se o id não existe, atualiza se existe. Devolve o id gerado.
    @Upsert
    suspend fun upsert(game: GameEntity): Long

    @Delete
    suspend fun delete(game: GameEntity)
}
