package com.github.abnamro.data.model

import com.squareup.moshi.Json

/** Moshi data class @field is required for moshi to work with kotlin.
 * Implementing the official moshi-kotlin dependency is too large imo */
data class RepoJson(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "full_name") val fullName: String,
    @field:Json(name = "private") val isPrivate: Boolean,
    @field:Json(name = "owner") val owner: OwnerJson,
    @field:Json(name = "html_url") val htmlURL: String,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "visibility") val visibility: String,
) {
    data class OwnerJson(
        @field:Json(name = "login") val login: String,
        @field:Json(name = "avatar_url") val avatarURL: String,
    )
}


