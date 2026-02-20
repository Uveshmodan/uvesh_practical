package com.uveshpractical.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.uveshpractical.R
import com.uveshpractical.data.local.entity.CharacterEntity
import com.uveshpractical.presentation.viewmodel.CharacterDetailViewModel
import com.uveshpractical.utils.UiState

@Composable
fun DetailScreen(id: Int) {
    val context = LocalContext.current
    val detailsViewModel = hiltViewModel<CharacterDetailViewModel>()
    val detailState by detailsViewModel.characterDetails.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        detailsViewModel.loadCharacter(context = context, id = id)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        when (detailState) {
            is UiState.Loading -> LoadingView()

            is UiState.Success -> DetailScreenSuccess(character = (detailState as UiState.Success<CharacterEntity>).data)

            is UiState.Error -> {
                ErrorView(
                    error = (detailState as UiState.Error).msg,
                    onRetry = { detailsViewModel.loadCharacter(id, context) }
                )
            }
        }

        CheckOffline()
    }
}

@Composable
private fun ColumnScope.DetailScreenSuccess(character: CharacterEntity) {

    Box(
        modifier = Modifier.weight(1f).fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // Image
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            // Name
            Text(
                text = character.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            DetailCard(character)
        }
    }
}

@Composable
private fun DetailCard(character: CharacterEntity) {

    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            DetailRow(stringResource(R.string.status), character.status)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow(stringResource(R.string.species), character.species)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow(stringResource(R.string.gender), character.gender)
            Spacer(modifier = Modifier.height(8.dp))

            DetailRow(stringResource(R.string.origin), character.origin)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row {
        Text(
            text = label,
            fontWeight = FontWeight.Bold
        )
        Text(text = value)
    }
}