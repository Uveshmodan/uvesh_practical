package com.uveshpractical.domain.usecases

import androidx.paging.PagingData
import com.uveshpractical.data.local.entity.CharacterEntity
import com.uveshpractical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<PagingData<CharacterEntity>> {
        return repository.getCharacters()
    }
}