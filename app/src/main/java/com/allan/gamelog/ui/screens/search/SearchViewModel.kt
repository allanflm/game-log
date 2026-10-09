package com.allan.gamelog.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.allan.gamelog.GameLogApplication
import com.allan.gamelog.data.repository.CatalogRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(private val catalogRepository: CatalogRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeQueryChanges() }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    // debounce espera o usuário parar de digitar antes de buscar; sem isso, digitar "zelda"
    // dispararia 5 chamadas ao backend. collectLatest cancela a busca anterior se uma nova
    // consulta chegar, então uma resposta lenta nunca sobrescreve uma mais recente.
    @OptIn(FlowPreview::class)
    private suspend fun observeQueryChanges() {
        _uiState
            .map { it.query.trim() }
            .distinctUntilChanged()
            .debounce(DEBOUNCE_MILLIS)
            .collectLatest { query -> search(query) }
    }

    private suspend fun search(query: String) {
        if (query.isEmpty()) {
            _uiState.update { it.copy(results = emptyList(), isLoading = false, hasError = false) }
            return
        }
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        catalogRepository.search(query)
            .onSuccess { games ->
                _uiState.update { it.copy(results = games, isLoading = false) }
            }
            .onFailure {
                _uiState.update { it.copy(results = emptyList(), isLoading = false, hasError = true) }
            }
    }

    companion object {
        private const val DEBOUNCE_MILLIS = 400L

        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as GameLogApplication
                SearchViewModel(application.catalogRepository)
            }
        }
    }
}
