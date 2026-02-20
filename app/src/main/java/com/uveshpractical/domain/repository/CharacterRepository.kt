package com.uveshpractical.domain.repository

import androidx.paging.PagingData
import com.uveshpractical.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharacters(): Flow<PagingData<CharacterEntity>>

    fun getCharacterById(id: Int): Flow<CharacterEntity?>

    suspend fun refreshCharacter(id: Int)
}

