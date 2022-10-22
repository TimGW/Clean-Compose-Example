package com.github.abnamro.data.remote.jsonAdapter

import com.github.abnamro.data.model.RepoDetailsEntity
import com.github.abnamro.data.model.RepoDetailsJson
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class RepoDetailsJsonAdapter {

    @FromJson
    fun fromJson(json: RepoDetailsJson): RepoDetailsEntity {
        return RepoDetailsEntity(
            json.name,
            json.fullName,
            json.isPrivate,
            RepoDetailsEntity.Owner(
                json.owner.login,
                json.owner.avatarURL,
            ),
            json.htmlURL,
            json.description.orEmpty(),
            json.visibility,
            System.currentTimeMillis()
        )
    }
}


