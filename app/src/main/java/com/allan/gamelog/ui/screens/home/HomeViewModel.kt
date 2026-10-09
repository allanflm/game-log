package com.allan.gamelog.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.allan.gamelog.GameLogApplication
import com.allan.gamelog.data.repository.CatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val catalogRepository: CatalogRepository) : ViewModel() {

    // MutableStateFlow é privado: só o ViewModel altera o estado. A tela recebe a versão
    // somente leitura (asStateFlow), que é o que garante o fluxo de dados em uma direção.
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadPopular()
    }

    fun onRetryClick() = loadPopular()

    private fun loadPopular() {
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        // viewModelScope: coroutine que é cancelada sozinha quando o ViewModel morre.
        viewModelScope.launch {
            catalogRepository.popular()
                .onSuccess { games ->
                    _uiState.update { it.copy(popularGames = games, isLoading = false) }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, hasError = true) }
                }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as GameLogApplication
                HomeViewModel(application.catalogRepository)
            }
        }
    }
}
