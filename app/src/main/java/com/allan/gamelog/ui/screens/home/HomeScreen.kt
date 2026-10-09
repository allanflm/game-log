package com.allan.gamelog.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.allan.gamelog.R
import com.allan.gamelog.domain.CatalogGame
import com.allan.gamelog.ui.components.GameCover
import com.allan.gamelog.ui.theme.GamelogTheme

private const val GRID_COLUMNS = 3
private val ScreenPadding = 16.dp
private val GridSpacing = 8.dp
private val SectionSpacing = 12.dp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onRetryClick = viewModel::onRetryClick,
        modifier = modifier,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // O cabeçalho faz parte da grade (e não fica fixo em cima) para rolar junto com as capas.
    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMNS),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(ScreenPadding),
        horizontalArrangement = Arrangement.spacedBy(GridSpacing),
        verticalArrangement = Arrangement.spacedBy(GridSpacing),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Header()
        }
        when {
            uiState.isLoading -> item(span = { GridItemSpan(maxLineSpan) }) {
                CenteredMessage { CircularProgressIndicator() }
            }
            uiState.hasError -> item(span = { GridItemSpan(maxLineSpan) }) {
                CenteredMessage {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.load_error),
                            textAlign = TextAlign.Center,
                        )
                        Button(
                            onClick = onRetryClick,
                            modifier = Modifier.padding(top = SectionSpacing),
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }
            else -> items(uiState.popularGames, key = { it.id }) { game ->
                GameCover(title = game.title, coverUrl = game.coverUrl)
            }
        }
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = stringResource(R.string.home_popular_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = SectionSpacing),
        )
    }
}

@Composable
private fun CenteredMessage(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ScreenPadding * 2),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    GamelogTheme {
        HomeContent(
            uiState = HomeUiState(
                popularGames = List(6) { index ->
                    CatalogGame(
                        id = index.toLong(),
                        title = "Jogo ${index + 1}",
                        coverUrl = null,
                        releaseYear = 2024,
                        genres = emptyList(),
                    )
                },
                isLoading = false,
            ),
            onRetryClick = {},
        )
    }
}
