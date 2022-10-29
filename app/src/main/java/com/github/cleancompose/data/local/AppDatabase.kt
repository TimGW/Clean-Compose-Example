package com.github.cleancompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.cleancompose.data.model.RepoDetailsEntity

const val DATABASE_NAME = "database"
private const val DATABASE_VERSION = 1

@Database(
    entities = [RepoDetailsEntity::class],
    version = DATABASE_VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDetailsDao(): RepoDetailsDao
}
