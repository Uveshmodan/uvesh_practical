package com.uveshpractical.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.uveshpractical.data.local.database.AppDatabase
import com.uveshpractical.data.local.entity.CharacterEntity
import com.uveshpractical.data.mediator.CharacterRemoteMediator
import com.uveshpractical.data.remote.api.CharacterApi
import com.uveshpractical.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: CharacterApi,
    private val database: AppDatabase
): CharacterRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(api, database),
            pagingSourceFactory = { database.characterDao().pagingSource() }
        ).flow
    }

    override fun getCharacterById(id: Int): Flow<CharacterEntity?> {
        return database.characterDao().getCharacterById(id)
    }

    override suspend fun refreshCharacter(id: Int) {
        val response = api.getCharacter(id)

        val entity = CharacterEntity(
            id = response.id,
            name = response.name,
            species = response.species,
            status = response.status,
            gender = response.gender,
            image = response.image,
            origin = response.origin.name
        )

        database.characterDao().insertAll(listOf(entity))
    }
}