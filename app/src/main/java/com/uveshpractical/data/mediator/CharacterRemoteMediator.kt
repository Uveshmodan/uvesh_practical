package com.uveshpractical.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.uveshpractical.data.local.database.AppDatabase
import com.uveshpractical.data.local.entity.CharacterEntity
import com.uveshpractical.data.local.entity.RemoteKeys
import com.uveshpractical.data.remote.api.CharacterApi
import kotlinx.coroutines.delay
import retrofit2.HttpException
import kotlin.Int

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val api: CharacterApi,
    private val database: AppDatabase
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {

        val page = when (loadType) {

            LoadType.REFRESH -> {

                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {

                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: return MediatorResult.Success(endOfPaginationReached = true)

                val prevKey = remoteKeys.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)

                prevKey
            }

            LoadType.APPEND -> {

                val remoteKeys = getRemoteKeyForLastItem(state) ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )

                val nextKey = remoteKeys.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)

                nextKey
            }
        }

        try {

            val response = api.getCharacters(page)

            val endOfPaginationReached = response.info.next == null

            val nextKey = response.info.next
                ?.substringAfter("page=")
                ?.toIntOrNull()
            database.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.characterDao().clearAll()
                }

                val keys = response.results.map {
                    RemoteKeys(
                        id = it.id,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = nextKey
                    )
                }

                database.remoteKeysDao().insertAll(keys)

                val entities = response.results.map {
                    CharacterEntity(
                        id = it.id,
                        name = it.name,
                        species = it.species,
                        status = it.status,
                        gender = it.gender,
                        image = it.image,
                        origin = it.origin.name,
                    )
                }

                database.characterDao().insertAll(entities)
            }

            return MediatorResult.Success(endOfPaginationReached)

        } catch (e: HttpException) {

            if (e.code() == 429) {
                // Delay and retry once
                delay(2000)
                return load(loadType, state)
            }

            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, CharacterEntity>
    ): RemoteKeys? {

        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character ->
                database.remoteKeysDao().remoteKeysById(character.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, CharacterEntity>
    ): RemoteKeys? {

        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { character ->
                database.remoteKeysDao().remoteKeysById(character.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CharacterEntity>
    ): RemoteKeys? {

        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().remoteKeysById(id)
            }
        }
    }
}
