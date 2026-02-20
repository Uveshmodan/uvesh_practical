package com.uveshpractical.domain.usecases

import com.uveshpractical.domain.repository.CharacterRepository
import javax.inject.Inject

class RefreshCharacterUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.refreshCharacter(id)
    }
}