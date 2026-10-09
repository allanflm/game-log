package com.allan.gamelog.data.repository

import com.allan.gamelog.data.remote.GameApi
import com.allan.gamelog.data.remote.toDomain
import com.allan.gamelog.domain.CatalogGame
import kotlinx.coroutines.CancellationException

// Acesso ao catálogo de jogos (backend). Devolve Result porque a rede falha o tempo todo
// (sem internet, backend fora do ar), e a tela precisa mostrar isso em vez de quebrar.
class CatalogRepository(private val gameApi: GameApi) {

    suspend fun search(query: String): Result<List<CatalogGame>> =
        safeCall { gameApi.search(query).map { it.toDomain() } }

    suspend fun popular(): Result<List<CatalogGame>> =
        safeCall { gameApi.popular().map { it.toDomain() } }

    // runCatching captura qualquer exceção, inclusive CancellationException, que é como
    // as coroutines avisam que foram canceladas (ex.: o usuário saiu da tela). Se ela for
    // engolida, a coroutine não cancela direito. Por isso a relançamos.
    private suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}
