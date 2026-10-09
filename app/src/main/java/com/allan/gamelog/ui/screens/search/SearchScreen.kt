package com.allan.gamelog.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.allan.gamelog.R
import com.allan.gamelog.domain.CatalogGame
import com.allan.gamelog.ui.components.GameCover
import com.allan.gamelog.ui.theme.GamelogTheme

private val ScreenPadding = 16.dp
private val ItemSpacing = 12.dp
private val CoverWidth = 56.dp
private val TextSpacing = 4.dp

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SearchContent(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        modifier = modifier,
    )
}

@Composable
private fun SearchContent(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().padding(horizontal = ScreenPadding)) {
        OutlinedTextField(
            value = uiState.query,
            onValueChange = onQueryChange,
            placeholder = { Text(stringResource(R.string.search_hint)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = ItemSpacing),
        )
        when {
            uiState.isLoading -> Message { CircularProgressIndicator() }
            uiState.hasError -> Message { Text(stringResource(R.string.load_error)) }
            uiState.query.isBlank() -> Message { Text(stringResource(R.string.search_prompt)) }
            uiState.results.isEmpty() -> Message { Text(stringResource(R.string.search_no_results)) }
            else -> LazyColumn(
                contentPadding = PaddingValues(bottom = ScreenPadding),
                verticalArrangement = Arrangement.spacedBy(ItemSpacing),
            ) {
                items(uiState.results, key = { it.id }) { game -> SearchResultRow(game) }
            }
        }
    }
}

@Composable
private fun SearchResultRow(game: CatalogGame) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ItemSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GameCover(title = game.title, coverUrl = game.coverUrl, modifier = Modifier.width(CoverWidth))
        Column(verticalArrangement = Arrangement.spacedBy(TextSpacing)) {
            Text(
                text = game.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            val details = game.details()
            if (details.isNotEmpty()) {
                Text(
                    text = details,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// Junta ano e gêneros ("2017 · Aventura"), pulando o que o IGDB não informou.
@Composable
private fun CatalogGame.details(): String {
    val parts = listOfNotNull(
        releaseYear?.toString(),
        genres.joinToString(", ").ifEmpty { null },
    )
    return parts.joinToString(stringResource(R.string.meta_separator))
}

@Composable
private fun Message(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ScreenPadding * 2),
        contentAlignment = Alignment.TopCenter,
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchContentPreview() {
    GamelogTheme {
        SearchContent(
            uiState = SearchUiState(
                query = "zelda",
                results = listOf(
                    CatalogGame(1, "The Legend of Zelda: Breath of the Wild", null, 2017, listOf("Adventure")),
                    CatalogGame(2, "Zelda II: The Adventure of Link", null, 1987, emptyList()),
                ),
            ),
            onQueryChange = {},
        )
    }
}
