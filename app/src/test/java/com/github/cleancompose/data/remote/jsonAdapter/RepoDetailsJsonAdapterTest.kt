package com.github.cleancompose.data.remote.jsonAdapter

import android.annotation.SuppressLint
import com.github.cleancompose.data.di.AppModule
import com.github.cleancompose.data.model.RepoDetailsEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.function.ThrowingRunnable
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@SuppressLint("CheckResult")
@RunWith(MockitoJUnitRunner::class)
class RepoDetailsJsonAdapterTest {

    private lateinit var jsonAdapter: JsonAdapter<RepoDetailsEntity>

    @Before
    fun beforeTest() {
        val moshi = AppModule.provideMoshi()
        jsonAdapter = moshi.adapter(RepoDetailsEntity::class.java)
    }

    @Test
    fun testFromJsonInvalidJson() {
        val throwing = ThrowingRunnable {
            jsonAdapter.fromJson("invalid json")
        }
        assertThrows(JsonEncodingException::class.java, throwing)
    }

    @Test
    fun testFromJsonEmptyJson() {
        val throwing = ThrowingRunnable {
            jsonAdapter.fromJson("""
                 {
                 }   
                """
            )
        }
        assertThrows(JsonDataException::class.java, throwing)
    }

    @Test
    fun testFromJsonValid() {
        val entity: RepoDetailsEntity? = jsonAdapter.fromJson(
            """
         {
            "name": "airflow",
            "full_name": "abnamrocoesd/airflow",
            "private": false,
            "owner": {
                "login": "abnamrocoesd",
                "avatar_url": "https://avatars.githubusercontent.com/u/15876397?v=4"
            },
            "html_url": "https://github.com/abnamrocoesd/airflow",
            "description": "Apache Airflow - A platform to programmatically author, schedule, and monitor workflows",
            "visibility": "public"
        }
        """.trimIndent()
        )

        assertNotNull(entity)

        assertEquals("airflow", entity?.name)
        assertEquals("abnamrocoesd/airflow", entity?.fullName)
        assertEquals(false, entity?.isPrivate)
        assertEquals("abnamrocoesd", entity?.owner?.login)
        assertEquals(
            "https://avatars.githubusercontent.com/u/15876397?v=4",
            entity?.owner?.avatarURL
        )
        assertEquals("https://github.com/abnamrocoesd/airflow", entity?.htmlURL)
        assertEquals(
            "Apache Airflow - A platform to programmatically author, schedule, and monitor workflows",
            entity?.description
        )
        assertEquals("public", entity?.visibility)
    }
}