package com.github.cleancompose.data.remote.jsonAdapter

import com.github.cleancompose.data.model.RepoEntity
import com.github.cleancompose.data.model.RepoJson
import com.squareup.moshi.FromJson

class RepoJsonAdapter {

    @FromJson
    fun fromJson(json: List<RepoJson>?): List<RepoEntity>? {
        if (json.isNullOrEmpty()) return null

        return json.map {
            RepoEntity(
                it.name,
                it.fullName,
                it.isPrivate,
                RepoEntity.Owner(
                    it.owner.login,
                    it.owner.avatarURL,
                ),
                it.htmlURL,
                it.description.orEmpty(),
                it.visibility,
                System.currentTimeMillis()
            )
        }
    }
}
