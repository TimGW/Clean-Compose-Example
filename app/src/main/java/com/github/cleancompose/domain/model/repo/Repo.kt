package com.github.cleancompose.domain.model.repo

/** Data class for Github repositories overview */
data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val isPrivate: Boolean,
    val owner: Owner,
    val htmlURL: String,
    val description: String,
    val visibility: String,
) {
    data class Owner(
        val login: String,
        val avatarURL: String,
    )
}