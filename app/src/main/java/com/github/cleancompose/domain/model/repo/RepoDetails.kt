package com.github.cleancompose.domain.model.repo

/** Data class for Github repository details */
data class RepoDetails(
    val name: String,
    val fullName: String,
    val isPrivate: Boolean,
    val owner: Owner,
    val htmlURL: String,
    val description: String,
    val visibility: String,
    val modifiedAt: Long,
) {
    data class Owner(
        val login: String,
        val avatarURL: String,
    )
}
