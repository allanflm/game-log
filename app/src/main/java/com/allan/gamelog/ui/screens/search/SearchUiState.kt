package com.allan.gamelog.ui.screens.search

import com.allan.gamelog.domain.CatalogGame

data class SearchUiState(
    val query: String = "",
    val results: List<CatalogGame> = emptyList(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
)
