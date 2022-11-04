package com.github.cleancompose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.cleancompose.data.model.RepoKeysEntity

@Dao
interface RepoKeysDao {

    @Query("SELECT * FROM RepoKeysEntity WHERE fullName = :fullName LIMIT 1")
    suspend fun getRemoteKeys(fullName: String): RepoKeysEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<RepoKeysEntity>)

    @Query("DELETE FROM RepoKeysEntity")
    suspend fun deleteAllRemoteKeys()
}