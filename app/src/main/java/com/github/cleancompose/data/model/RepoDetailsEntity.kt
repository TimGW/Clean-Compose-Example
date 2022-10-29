package com.github.cleancompose.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.cleancompose.domain.model.repo.RepoDetails

@Entity
data class RepoDetailsEntity(
    val name: String,
    @PrimaryKey @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "is_private") val isPrivate: Boolean,
    @Embedded val owner: Owner,
    @ColumnInfo(name = "html_url") val htmlURL: String,
    val description: String,
    val visibility: String,
    @ColumnInfo(name = "modified_at") val modifiedAt: Long,
) {
    data class Owner(
        @ColumnInfo(name = "login") val login: String,
        @ColumnInfo(name = "avatar_url") val avatarURL: String,
    )

    companion object {
        fun from(repoDetails: RepoDetails) = with(repoDetails) {
            RepoDetailsEntity(
                name,
                fullName,
                isPrivate,
                Owner(
                    owner.login,
                    owner.avatarURL,
                ),
                htmlURL,
                description,
                visibility,
                modifiedAt
            )
        }
    }

    fun toDetails() = RepoDetails(
        name,
        fullName,
        isPrivate,
        RepoDetails.Owner(
            owner.login,
            owner.avatarURL,
        ),
        htmlURL,
        description,
        visibility,
        modifiedAt
    )
}