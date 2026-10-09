package com.allan.gamelog.ui.screens.gamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.allan.gamelog.GameLogApplication
import com.allan.gamelog.data.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GameListViewModel(gameRepository: GameRepository) : ViewModel() {

    // Transforma o Flow do banco em StateFlow. Cada vez que a tabela muda, o Room
    // emite uma lista nova e o uiState atualiza sozinho.
    val uiState: StateFlow<GameListUiState> = gameRepository.observeGames()
        .map { games -> GameListUiState(games = games, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            // Continua ouvindo o banco por mais um tempo depois que a tela some. Assim,
            // girar o celular não reinicia a consulta.
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = GameListUiState(),
        )

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5_000L

        // O Android só sabe criar ViewModels sem parâmetros. Como o nosso recebe o
        // repository, ensinamos a criá-lo aqui. O Hilt vai tornar isto desnecessário.
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as GameLogApplication
                GameListViewModel(application.gameRepository)
            }
        }
    }
}
