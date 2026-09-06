package com.example.inventaryws.core.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.example.inventaryws.R

@Composable
fun IWSAsyncImage(
    data: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String?,
    crossFadeDurationMillis: Int = 500,
    contentScale: ContentScale = ContentScale.Fit
) {
    AsyncImage(
        model = ImageRequest
            .Builder(context = LocalContext.current)
            .data(data)
            .crossfade(enable = true)
            .crossfade(crossFadeDurationMillis)
            .error(drawableResId = R.drawable.ic_launcher_foreground)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}