package com.github.cleancompose


import com.github.cleancompose.data.model.RepoDetailsEntity

object TestUtil {

    fun createDetailsOwner(
        login: String,
        avatarURL: String,
    ) = RepoDetailsEntity.Owner(login = login, avatarURL = avatarURL)

    fun createRepoDetailsEntity(
        name: String = "name",
        fullName: String = "fullName",
        isPrivate: Boolean = false,
        owner: RepoDetailsEntity.Owner = createDetailsOwner("login", "avatarUrl"),
        htmlURL: String = "htmlURL",
        description: String = "description",
        visibility: String = "visibility",
        modifiedAt: Long = 0L,
    ) = RepoDetailsEntity(
        name = name,
        fullName = fullName,
        isPrivate = isPrivate,
        owner = owner,
        htmlURL = htmlURL,
        description = description,
        visibility = visibility,
        modifiedAt = modifiedAt,
    )

    fun createRepoDetails(
        name: String = "name",
        fullName: String = "fullName",
        isPrivate: Boolean = false,
        owner: RepoDetailsEntity.Owner = createDetailsOwner("login", "avatarUrl"),
        htmlURL: String = "htmlURL",
        description: String = "description",
        visibility: String = "visibility",
        modifiedAt: Long = 0L,
    ) = createRepoDetailsEntity().toDetails()
}
