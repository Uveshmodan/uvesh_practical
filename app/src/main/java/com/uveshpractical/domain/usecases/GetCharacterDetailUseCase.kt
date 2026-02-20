package com.uveshpractical.domain.usecases

import com.uveshpractical.data.local.entity.CharacterEntity
import com.uveshpractical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterDetailUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(id: Int): Flow<CharacterEntity?> {
        return repository.getCharacterById(id)
    }
}