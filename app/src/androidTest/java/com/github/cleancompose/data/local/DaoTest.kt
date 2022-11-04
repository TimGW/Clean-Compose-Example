package com.github.cleancompose.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.cleancompose.TestUtil
import org.junit.After

interface DaoTest {
    @After
    fun teardown() {
        db.close()
    }
}

val DaoTest.db: AppDatabase
    get() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }
