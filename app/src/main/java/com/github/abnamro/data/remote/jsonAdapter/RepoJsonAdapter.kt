package com.github.abnamro.data.remote.jsonAdapter

import com.github.abnamro.data.model.RepoJson
import com.github.abnamro.domain.model.repo.Repo
import com.squareup.moshi.FromJson

class RepoJsonAdapter {

    @FromJson
    fun fromJson(json: List<RepoJson>?): List<Repo>? {
        if (json.isNullOrEmpty()) return null

        return json.map {
            Repo(
                it.id,
                it.name,
                it.fullName,
                it.isPrivate,
                Repo.Owner(
                    it.owner.login,
                    it.owner.avatarURL,
                ),
                it.htmlURL,
                it.description.orEmpty(),
                it.visibility
            )
        }
    }
}
