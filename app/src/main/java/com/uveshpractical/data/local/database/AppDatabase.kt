package com.uveshpractical.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uveshpractical.data.local.dao.CharacterDao
import com.uveshpractical.data.local.dao.RemoteKeysDao
import com.uveshpractical.data.local.entity.CharacterEntity
import com.uveshpractical.data.local.entity.RemoteKeys

@Database(
    entities = [CharacterEntity::class, RemoteKeys::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}