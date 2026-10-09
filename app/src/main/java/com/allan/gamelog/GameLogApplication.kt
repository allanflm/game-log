package com.allan.gamelog

import android.app.Application
import com.allan.gamelog.data.local.GameLogDatabase
import com.allan.gamelog.data.repository.GameRepository

// O Android cria esta classe uma vez só, quando o processo do app começa. Por isso
// ela guarda o banco e o repository: queremos uma única instância de cada.
// Isto é uma injeção de dependência feita "na mão". Na etapa 2, o Hilt assume.
class GameLogApplication : Application() {

    // lazy: o banco só é aberto na primeira vez que alguém usar.
    private val database by lazy { GameLogDatabase.create(this) }

    val gameRepository by lazy { GameRepository(database.gameDao()) }
}
