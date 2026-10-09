package com.allan.gamelog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

private const val COVER_ASPECT_RATIO = 3f / 4f
private val CoverCornerRadius = 6.dp
private val PlaceholderPadding = 8.dp

// Capa do jogo. O AsyncImage do Coil baixa a imagem em segundo plano e guarda em cache.
// Se o jogo não tem capa (ou ela ainda está carregando), o fundo cinza com o título aparece.
@Composable
fun GameCover(
    title: String,
    coverUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(COVER_ASPECT_RATIO)
            .clip(RoundedCornerShape(CoverCornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        if (coverUrl == null) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(PlaceholderPadding),
            )
        } else {
            AsyncImage(
                model = coverUrl,
                // A capa já é identificada pelo título; descrevê-la de novo seria ruído para
                // leitores de tela, então o título vai como descrição.
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        }
    }
}
