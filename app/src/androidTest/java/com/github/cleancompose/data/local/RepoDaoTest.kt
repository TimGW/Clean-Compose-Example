package com.github.cleancompose.data.local

import androidx.paging.PagingSource
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.cleancompose.TestUtil
import com.github.cleancompose.TestUtil.createRepo
import com.github.cleancompose.data.model.RepoEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class RepoDaoTest: DaoTest {
    private val repoDao: RepoDao = db.repoDao()

    @Test
    fun writeAndDeleteRepos() = runTest {
        val size = 3
        val repos: List<RepoEntity> = TestUtil.createRepos(size)

        repoDao.addRepos(repos)

        val fetchBeforeDelete = repoDao.getAllRepos()
        val dataBeforeDelete =
            fetchBeforeDelete.load(PagingSource.LoadParams.Refresh(null, size, false))
        val actualBeforeDelete = (dataBeforeDelete as PagingSource.LoadResult.Page).data

        assertEquals(repos, actualBeforeDelete)

        repoDao.deleteAllRepos()

        val fetchAfterDelete = repoDao.getAllRepos()
        val dataAfterDelete =
            fetchAfterDelete.load(PagingSource.LoadParams.Refresh(null, size, false))
        val actualAfterDelete = (dataAfterDelete as PagingSource.LoadResult.Page).data

        assertTrue(actualAfterDelete.isEmpty())
    }

    @Test
    fun overwriteRepos() = runTest {
        val size = 3
        val repos: List<RepoEntity> = TestUtil.createRepos(size)

        repoDao.addRepos(repos)
        // add same list again
        repoDao.addRepos(repos)

        val fetch = repoDao.getAllRepos()
        val data = fetch.load(PagingSource.LoadParams.Refresh(null, size, false))
        val actual = (data as PagingSource.LoadResult.Page).data

        assertEquals(repos, actual)
    }

    @Test
    fun lastUpdatedRepo() = runTest {
        val repos: List<RepoEntity> = TestUtil.createRepos(10, createRepo().copy(modifiedAt = 1))

        repoDao.addRepos(repos)

        assertEquals(10L, repoDao.lastUpdated())
    }
}