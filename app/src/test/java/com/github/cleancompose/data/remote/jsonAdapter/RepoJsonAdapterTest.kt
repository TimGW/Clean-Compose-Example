package com.github.cleancompose.data.remote.jsonAdapter

import android.annotation.SuppressLint
import com.github.cleancompose.data.di.AppModule
import com.github.cleancompose.domain.model.repo.Repo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Types
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.function.ThrowingRunnable
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Type


@SuppressLint("CheckResult")
@RunWith(MockitoJUnitRunner::class)
class RepoJsonAdapterTest {
    private val type: Type = Types.newParameterizedType(List::class.java, Repo::class.java)
    private lateinit var jsonAdapter: JsonAdapter<List<Repo>?>

    @Before
    fun beforeTest() {
        val moshi = AppModule.provideMoshi()
        jsonAdapter = moshi.adapter(type)
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
            jsonAdapter.fromJson(
                """
                 {
                 }   
                """
            )
        }
        assertThrows(JsonDataException::class.java, throwing)
    }

    @Test
    fun testFromJsonValidEmpty() {
        val repo: Repo? = jsonAdapter.fromJson(
            """
            [
            ]
        """.trimIndent()
        )?.firstOrNull()

        assertNull(repo)
    }

    @Test
    fun testFromJsonValid() {
        val repo: Repo? = jsonAdapter.fromJson(
            """
         [
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
        ]
        """.trimIndent()
        )?.firstOrNull()

        assertNotNull(repo)

        assertEquals("airflow", repo?.name)
        assertEquals("abnamrocoesd/airflow", repo?.fullName)
        assertEquals(false, repo?.isPrivate)
        assertEquals("abnamrocoesd", repo?.owner?.login)
        assertEquals(
            "https://avatars.githubusercontent.com/u/15876397?v=4",
            repo?.owner?.avatarURL
        )
        assertEquals("https://github.com/abnamrocoesd/airflow", repo?.htmlURL)
        assertEquals(
            "Apache Airflow - A platform to programmatically author, schedule, and monitor workflows",
            repo?.description
        )
        assertEquals("public", repo?.visibility)
    }
}