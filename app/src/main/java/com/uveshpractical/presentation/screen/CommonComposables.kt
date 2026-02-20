package com.uveshpractical.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uveshpractical.R
import com.uveshpractical.presentation.util.isInternetAvailable


@Composable
fun LoadingView() {
    CircularProgressIndicator()
}


@Composable
fun ErrorView(error: String, onRetry: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(20.dp),
            text = error,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onRetry
        ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}


@Composable
fun CheckOffline() {
    val content = LocalContext.current

    val offline = remember {
        content.isInternetAvailable()
    }
    if (!offline) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(0.8f))
                .padding(5.dp)
        ) {
            Text(stringResource(R.string.you_are_offline))
        }
    }
}
