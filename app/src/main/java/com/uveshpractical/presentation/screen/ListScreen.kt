package com.uveshpractical.presentation.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.uveshpractical.R
import com.uveshpractical.presentation.viewmodel.CharacterListViewModel

@Composable
fun ListScreen(navigateToDetails: (Int) -> Unit) {
    val listViewModel = hiltViewModel<CharacterListViewModel>()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        ListScreenMainUi(
            listViewModel = listViewModel,
            navigateToDetails = navigateToDetails
        )

        CheckOffline()
    }
}

@Composable
fun ColumnScope.ListScreenMainUi(listViewModel: CharacterListViewModel, navigateToDetails: (Int) -> Unit) {
    val characters = listViewModel.characters.collectAsLazyPagingItems()

    val refreshState = characters.loadState.refresh

    Box(modifier = Modifier.weight(1f)) {

        // Always show list if we have data
        if (characters.itemCount > 0) {
            LazyColumn {
                items(
                    count = characters.itemCount,
                    key = {
                        characters[it]?.id ?: it
                    }
                ) { index ->
                    characters[index]?.let { item ->
                        CharacterItem(
                            imageUrl = item.image,
                            name = item.name,
                            species = item.species,
                            modifier = Modifier
                                .clickable { navigateToDetails(item.id) }
                        )
                    }
                }

                // Append state
                item {
                    PagingFooter(
                        loadState = characters.loadState.append,
                        onRetry = { characters.retry() }
                    )
                }
            }
        }

        // Show full screen error ONLY if no cache
        if (refreshState is LoadState.Error &&
            characters.itemCount == 0) {

            ErrorView(
                error = refreshState.error.message ?: stringResource(R.string.no_internet),
                onRetry = { characters.retry() }
            )
        }

        // Show loading ONLY if no cache
        if (refreshState is LoadState.Loading &&
            characters.itemCount == 0) {

            LoadingView()
        }
    }
}

@Composable
fun PagingFooter(
    loadState: LoadState,
    onRetry: () -> Unit
) {
    when (loadState) {

        is LoadState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(stringResource(R.string.failed_to_load_more))

                OutlinedButton(onClick = onRetry) {
                    Text(stringResource(R.string.retry))
                }
            }
        }

        else -> Unit
    }
}

@Composable
private fun CharacterItem(
    imageUrl: String?,
    name: String,
    species: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = species,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}