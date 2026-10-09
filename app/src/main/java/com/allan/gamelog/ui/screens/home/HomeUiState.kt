package com.allan.gamelog.ui.screens.home

import com.allan.gamelog.domain.CatalogGame

data class HomeUiState(
    val popularGames: List<CatalogGame> = emptyList(),
    val isLoading: Boolean = true,
    // Só um boolean: o texto da mensagem fica na tela (strings.xml), não no ViewModel.
    val hasError: Boolean = false,
)
