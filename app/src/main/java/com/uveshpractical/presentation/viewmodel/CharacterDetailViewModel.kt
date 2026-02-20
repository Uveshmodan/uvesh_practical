package com.uveshpractical.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uveshpractical.data.local.entity.CharacterEntity
import com.uveshpractical.domain.usecases.GetCharacterDetailUseCase
import com.uveshpractical.domain.usecases.RefreshCharacterUseCase
import com.uveshpractical.presentation.util.isInternetAvailable
import com.uveshpractical.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharactersDetailUseCase: GetCharacterDetailUseCase,
    private val refreshCharactersDetailUseCase: RefreshCharacterUseCase,
) : ViewModel() {

    private val _characterDetails = MutableStateFlow<UiState<CharacterEntity>>(UiState.Loading)
    val characterDetails = _characterDetails.asStateFlow()


    fun loadCharacter(id: Int, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            getCharactersDetailUseCase(id).collect { localData ->
                if (localData != null) {
                    _characterDetails.value = UiState.Success(localData)
                }
            }
        }

        // Fetch from API only if internet available
        if (context.isInternetAvailable()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    refreshCharactersDetailUseCase(id)
                } catch (e: Exception) {
                    // ignore if already cached
                }
            }
        }
    }
}