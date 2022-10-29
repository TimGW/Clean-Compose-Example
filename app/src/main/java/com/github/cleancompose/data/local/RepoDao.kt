package com.github.cleancompose.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.cleancompose.data.model.RepoEntity

@Dao
interface RepoDao {

    @Query("SELECT * FROM RepoEntity")
    fun getAllRepos(): PagingSource<Int, RepoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRepos(repos: List<RepoEntity>)

    @Query("DELETE FROM RepoEntity")
    suspend fun deleteAllRepos()

    @Query("SELECT modified_at FROM RepoEntity ORDER BY modified_at DESC LIMIT 1")
    suspend fun lastUpdated(): Long?
}