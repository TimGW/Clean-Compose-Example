package com.github.cleancompose


import com.github.cleancompose.data.model.RepoDetailsEntity
import com.github.cleancompose.data.model.RepoEntity
import com.github.cleancompose.data.model.RepoKeysEntity

object TestUtil {

    fun createRepos(count: Int, repo: RepoEntity = createRepo()): List<RepoEntity> {
        return (0 until count).map {
            createRepo(
                name = repo.name + it,
                fullName = repo.fullName + it,
                isPrivate = repo.isPrivate,
                owner = repo.owner,
                htmlURL = repo.htmlURL + it,
                description = repo.description + it,
                visibility = repo.visibility + it,
                modifiedAt = repo.modifiedAt + it,
            )
        }
    }

    fun createOwner(
        login: String,
        avatarURL: String,
    ) = RepoEntity.Owner(login = login, avatarURL = avatarURL)

    fun createRepo(
        name: String = "name",
        fullName: String = "fullName",
        isPrivate: Boolean = false,
        owner: RepoEntity.Owner = createOwner("login", "avatarUrl"),
        htmlURL: String = "htmlURL",
        description: String = "description",
        visibility: String = "visibility",
        modifiedAt: Long = 0L,
    ) = RepoEntity(
        name = name,
        fullName = fullName,
        isPrivate = isPrivate,
        owner = owner,
        htmlURL = htmlURL,
        description = description,
        visibility = visibility,
        modifiedAt = modifiedAt,
    )

    fun createDetailsOwner(
        login: String,
        avatarURL: String,
    ) = RepoDetailsEntity.Owner(login = login, avatarURL = avatarURL)

    fun createRepoDetails(
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

    fun createRepoKeys(count: Int, keys: RepoKeysEntity = createRepoKeys()): List<RepoKeysEntity> {
        return (0 until count).map {
            createRepoKeys(
                fullName = keys.fullName + it,
                prevPage = keys.prevPage?.plus(it),
                nextPage = keys.nextPage?.plus(it),
            )
        }
    }


    fun createRepoKeys(
        fullName: String = "1234",
        prevPage: Int? = 0,
        nextPage: Int? = 1
    ) = RepoKeysEntity(
        fullName = fullName,
        prevPage = prevPage,
        nextPage = nextPage
    )
}
