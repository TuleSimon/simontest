package com.simon.secondtest

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.simon.secondtest.data.datastore.DataStoreSource
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DATASTORE_NAME: String = "test_datastore"
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DataStoreTest {

    private val testContext: Context =
        InstrumentationRegistry.getInstrumentation().targetContext

    private val testCoroutineDispatcher =  StandardTestDispatcher()
    private val testCoroutineScope = CoroutineScope(testCoroutineDispatcher + Job())

    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = testCoroutineScope,
            produceFile =
            { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) })


    private val repository: DataStoreSource =
        DataStoreSource(testDataStore)

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Test
    fun repository_testFetchInitialPreferences() {
        testCoroutineScope.launch {
            repository.getSelectedBrand().collect{
                assertEquals(it,"jj")
            }
        }
    }

}