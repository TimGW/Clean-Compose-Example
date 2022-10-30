package com.github.cleancompose.domain.model.repo

import kotlinx.serialization.Serializable

/** Data class for Github repositories overview */
@Serializable
data class Repo(
    val name: String,
    @Serializable(UrlSerializer::class)
    val fullName: String,
    val isPrivate: Boolean,
    val owner: Owner,
    @Serializable(UrlSerializer::class)
    val htmlURL: String,
    val description: String,
    val visibility: String,
    val modifiedAt: Long,
) {
    @Serializable
    data class Owner(
        val login: String,
        @Serializable(UrlSerializer::class)
        val avatarURL: String,
    )

    fun toDetails() = RepoDetails(
        name,
        fullName,
        isPrivate,
        RepoDetails.Owner(
            owner.login,
            owner.avatarURL
        ),
        htmlURL,
        description,
        visibility,
        modifiedAt,
    )
}