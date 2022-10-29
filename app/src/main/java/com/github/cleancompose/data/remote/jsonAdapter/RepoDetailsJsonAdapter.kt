package com.github.cleancompose.data.remote.jsonAdapter

import com.github.cleancompose.data.model.RepoDetailsEntity
import com.github.cleancompose.data.model.RepoDetailsJson
import com.squareup.moshi.FromJson

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


