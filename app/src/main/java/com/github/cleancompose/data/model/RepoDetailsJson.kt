package com.github.cleancompose.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailsJson(
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

    fun toEntity() = RepoDetailsEntity(
        name,
        fullName,
        isPrivate,
        RepoDetailsEntity.Owner(
            owner.login,
            owner.avatarURL,
        ),
        htmlURL,
        description.orEmpty(),
        visibility,
        System.currentTimeMillis()
    )
}
