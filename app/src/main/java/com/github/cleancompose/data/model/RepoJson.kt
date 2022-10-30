package com.github.cleancompose.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoJson(
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("private") val isPrivate: Boolean,
    @SerialName("owner") val owner: OwnerJson,
    @SerialName("html_url") val htmlURL: String,
    @SerialName("description") val description: String?,
    @SerialName("visibility") val visibility: String,
) {
    @Serializable
    data class OwnerJson(
        @SerialName("login") val login: String,
        @SerialName("avatar_url") val avatarURL: String,
    )

    fun toEntity() = RepoEntity(
        name,
        fullName,
        isPrivate,
        RepoEntity.Owner(
            owner.login,
            owner.avatarURL,
        ),
        htmlURL,
        description.orEmpty(),
        visibility,
        System.currentTimeMillis()
    )
}


