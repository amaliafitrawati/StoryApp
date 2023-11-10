package com.dicoding.storyapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.storyapp.DataDummy
import com.dicoding.storyapp.MainDispatcherRule
import com.dicoding.storyapp.data.database.StoryItem
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.getOrAwaitValue
import com.dicoding.storyapp.response.Story
import com.dicoding.storyapp.view.main.StoryPagingAdapter
import com.dicoding.storyapp.view.main.StoryPagingSource
import com.dicoding.storyapp.view.story.StoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepo: StoryRepository
    private var dummyToken = "TOKEN"

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dataDummy = DataDummy.generateResponseDummy()
        val data: PagingData<Story> = StoryPagingSource.snapshot(dataDummy)
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        Mockito.`when`(storyRepo.getStories(dummyToken)).thenReturn(expectedStory)

        val viewModel = StoryViewModel(storyRepo)
        val actualStory: PagingData<Story> = viewModel.getStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dataDummy.size, differ.snapshot().size)
        Assert.assertEquals(dataDummy[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        Mockito.`when`(storyRepo.getStories(dummyToken)).thenReturn(expectedStory)
        val viewModel = StoryViewModel(storyRepo)
        val actualQuote: PagingData<Story> = viewModel.getStories(dummyToken).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertEquals(0, differ.snapshot().size)
    }


    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}