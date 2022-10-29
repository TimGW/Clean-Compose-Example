package com.github.cleancompose.data.remote.jsonAdapter

import com.github.cleancompose.data.model.RepoJson
import com.github.cleancompose.domain.model.repo.Repo
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
