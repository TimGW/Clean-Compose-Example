package com.github.abnamro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.abnamro.data.model.RepoDetailsEntity

const val DATABASE_NAME = "abn-amro-database"
private const val DATABASE_VERSION = 1

@Database(
    entities = [RepoDetailsEntity::class],
    version = DATABASE_VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDetailsDao(): RepoDetailsDao
}
