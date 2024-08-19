package robi.codingchallenge.carousellnews.ui

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import retrofit2.Response
import robi.codingchallenge.library.Network
import robi.codingchallenge.networks.NetworkApi
import robi.codingchallenge.networks.NetworkState
import robi.codingchallenge.networks.data.News
import robi.codingchallenge.networks.repository.NetworkRepository

@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // For LiveData to work synchronously

    @Mock
    private lateinit var api: NetworkApi

    @Mock
    lateinit var repository: NetworkRepository

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var observer: Observer<NetworkState<MutableList<News>>>

    @InjectMocks
    lateinit var newsViewModel: NewsViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        newsViewModel = NewsViewModel(application, repository)
    }

    @Test
    fun getNewsShouldPostNetworkStateLoadingAndThenSuccess() {
        // Given
        val newsList = mutableListOf<News>()
        val response = Response.success(newsList as ArrayList<News>)
        whenever(repository.getNews()).thenReturn(Single.just(response))
        whenever(Network.isConnect(any())).thenReturn(true)

        // Observing the LiveData
        newsViewModel.news.observeForever(observer)

        // When
        newsViewModel.getNews()

        // Then
        verify(observer).onChanged(NetworkState.Loading())
        verify(observer).onChanged(NetworkState.Success(newsList))
        verify(repository).getNews()
    }

    @Test
    fun getNewsShouldPostNetworkStateLoadingAndThenError() {
        // Given
        val throwable = Throwable("Error occurred")
        whenever(repository.getNews()).thenReturn(Single.error(throwable))
        whenever(Network.isConnect(any())).thenReturn(true)

        // Observing the LiveData
        newsViewModel.news.observeForever(observer)

        // When
        newsViewModel.getNews()

        // Then
        verify(observer).onChanged(NetworkState.Loading())
        verify(observer).onChanged(NetworkState.Error(throwable))
        verify(repository).getNews()
    }

    @Test
    fun getNewsShouldPostNetworkStateErrorWhenNoInternetConnection() {
        // Given
        whenever(Network.isConnect(any())).thenReturn(false)

        // Observing the LiveData
        newsViewModel.news.observeForever(observer)

        // When
        newsViewModel.getNews()

        // Then
        verify(observer).onChanged(NetworkState.Loading())
        verify(observer).onChanged(NetworkState.Error(Throwable("No internet connection")))
        verify(repository, never()).getNews()
    }

    @Test
    fun onClearedShouldClearDisposables() {
        // Given
        val disposable = mock(Disposable::class.java)
        newsViewModel.compositeDisposable.add(disposable)

        // When
        newsViewModel.onCleared()

        // Then
        verify(disposable).dispose()
        Assert.assertTrue(newsViewModel.compositeDisposable.isDisposed)
    }
}

