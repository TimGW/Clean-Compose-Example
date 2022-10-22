package com.github.abnamro.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.abnamro.data.model.RepoDetailsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface RepoDetailsDao {

    @Query("SELECT * FROM RepoDetailsEntity WHERE full_name = :query LIMIT 1")
    fun getRepoDetails(query: String): Flow<RepoDetailsEntity?>

    /**
     * SQLite database triggers only allow notifications at table level, not at row level.
     * distinctUntilChanged ensures that you only get notified when the row has changed
     */
    fun getRepoDetailsDistinct(query: String) =
        getRepoDetails(query).distinctUntilChanged()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepoDetails(repoDetailsEntity: RepoDetailsEntity)
}
