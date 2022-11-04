package com.github.cleancompose.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.cleancompose.TestUtil.createRepoDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailsDaoTest: DaoTest {
    private val repoDetailsDao = db.repoDetailsDao()

    @Test
    fun writeAndGetRepoDetails() = runTest {
        val repo = createRepoDetails()

        repoDetailsDao.insertRepoDetails(repo)
        repoDetailsDao.insertRepoDetails(repo.copy(fullName = "fullName2"))

        assertEquals(repo, repoDetailsDao.getRepoDetails(repo.fullName).first())
    }

    @Test
    fun writeAndGetRepoDetailsDistinct() = runTest {
        val repo = createRepoDetails()

        repoDetailsDao.insertRepoDetails(repo)
        repoDetailsDao.insertRepoDetails(repo.copy(fullName = "fullName2"))

        assertEquals(repo, repoDetailsDao.getRepoDetailsDistinct(repo.fullName).first())
    }
}