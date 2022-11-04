package com.github.cleancompose.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.cleancompose.TestUtil.createRepoKeys
import com.github.cleancompose.data.model.RepoKeysEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class RepoKeysDaoTest: DaoTest {
    private val repoKeysDao = db.repoKeysDao()

    @Test
    fun writeAndGetRepoKeys() = runTest {
        val keys: List<RepoKeysEntity> = createRepoKeys(3)

        repoKeysDao.addAllRemoteKeys(keys)

        assertEquals(keys.firstOrNull(), repoKeysDao.getRemoteKeys(keys.first().fullName))
    }

    @Test
    fun deleteRepoKeys() = runTest {
        val keys: List<RepoKeysEntity> = createRepoKeys(3)

        repoKeysDao.addAllRemoteKeys(keys)

        repoKeysDao.deleteAllRemoteKeys()

        assertNull(repoKeysDao.getRemoteKeys(keys.first().fullName))
    }
}