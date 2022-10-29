package com.github.cleancompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.cleancompose.data.model.RepoDetailsEntity
import com.github.cleancompose.data.model.RepoEntity
import com.github.cleancompose.data.model.RepoKeysEntity

const val DATABASE_NAME = "database"
private const val DATABASE_VERSION = 1

@Database(
    entities = [RepoDetailsEntity::class, RepoEntity::class, RepoKeysEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDetailsDao(): RepoDetailsDao
    abstract fun repoDao(): RepoDao
    abstract fun repoKeysDao(): RepoKeysDao
}
