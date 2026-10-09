package com.allan.gamelog.ui.screens.gamelist

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.allan.gamelog.R
import com.allan.gamelog.domain.Game
import com.allan.gamelog.domain.GameStatus
import com.allan.gamelog.ui.theme.GamelogTheme

private val ScreenPadding = 16.dp
private val ItemSpacing = 12.dp
private val CardPadding = 16.dp

// Parte "com estado": pega o ViewModel e observa o uiState. Fica separada do
// GameListContent para que o conteúdo possa ser testado e visualizado no Preview
// sem precisar de ViewModel nem de banco.
@Composable
fun GameListScreen(
    modifier: Modifier = Modifier,
    viewModel: GameListViewModel = viewModel(factory = GameListViewModel.Factory),
) {
    // collectAsStateWithLifecycle transforma o StateFlow num State do Compose: quando
    // o valor muda, a tela redesenha. Para de ouvir quando o app vai pro segundo plano.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GameListContent(uiState = uiState, modifier = modifier)
}

@Composable
private fun GameListContent(uiState: GameListUiState, modifier: Modifier = Modifier) {
    when {
        uiState.isLoading -> CenteredBox(modifier) { CircularProgressIndicator() }
        uiState.games.isEmpty() -> CenteredBox(modifier) {
            Text(text = stringResource(R.string.game_list_empty))
        }
        else -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(ItemSpacing),
        ) {
            // key = id ajuda o Compose a saber qual card é qual quando a lista muda.
            items(uiState.games, key = { it.id }) { game -> GameCard(game) }
        }
    }
}

@Composable
private fun CenteredBox(modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        content()
    }
}

@Composable
private fun GameCard(game: Game, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(CardPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = game.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = stringResource(
                        R.string.game_platform_status,
                        game.platform,
                        stringResource(game.status.labelRes()),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Text(
                text = game.rating
                    ?.let { stringResource(R.string.game_rating, it) }
                    ?: stringResource(R.string.game_not_rated),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@StringRes
private fun GameStatus.labelRes(): Int = when (this) {
    GameStatus.BACKLOG -> R.string.status_backlog
    GameStatus.PLAYING -> R.string.status_playing
    GameStatus.COMPLETED -> R.string.status_completed
    GameStatus.DROPPED -> R.string.status_dropped
}

@Preview(showBackground = true)
@Composable
private fun GameListContentPreview() {
    GamelogTheme {
        GameListContent(
            uiState = GameListUiState(
                games = listOf(
                    Game(id = 1, title = "Hollow Knight", platform = "PC", status = GameStatus.COMPLETED, rating = 5f),
                    Game(id = 2, title = "Elden Ring", platform = "PS5", status = GameStatus.PLAYING),
                ),
                isLoading = false,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameListEmptyPreview() {
    GamelogTheme {
        GameListContent(uiState = GameListUiState(isLoading = false))
    }
}
