package com.allan.gamelog.data.repository

import com.allan.gamelog.data.local.GameDao
import com.allan.gamelog.data.local.toDomain
import com.allan.gamelog.data.local.toEntity
import com.allan.gamelog.domain.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Único ponto de acesso aos jogos para o resto do app. Os ViewModels falam com
// esta classe e só enxergam Game, nunca GameEntity. Assim, se a fonte dos dados
// mudar (API, sincronização), as telas não precisam saber.
class GameRepository(private val gameDao: GameDao) {

    fun observeGames(): Flow<List<Game>> =
        gameDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    fun observeGame(id: Long): Flow<Game?> =
        gameDao.observeById(id).map { it?.toDomain() }

    // Cria ou atualiza, dependendo de o id já existir. Devolve o id do jogo.
    suspend fun saveGame(game: Game): Long = gameDao.upsert(game.toEntity())

    suspend fun deleteGame(game: Game) = gameDao.delete(game.toEntity())
}
