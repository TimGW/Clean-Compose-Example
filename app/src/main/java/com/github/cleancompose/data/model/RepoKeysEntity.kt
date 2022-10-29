package com.github.cleancompose.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepoKeysEntity(
    @PrimaryKey val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)
