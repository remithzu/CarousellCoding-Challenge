package robi.codingchallenge.carousellnews.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import robi.codingchallenge.carousellnews.MainApplication
import robi.codingchallenge.library.Network
import robi.codingchallenge.networks.NetworkState
import robi.codingchallenge.networks.data.News
import robi.codingchallenge.networks.repository.NetworkRepository
import javax.inject.Inject

@ViewModelScoped
class NewsViewModel @Inject constructor(application: Application, private val repository: NetworkRepository) : AndroidViewModel(application) {
    val compositeDisposable = CompositeDisposable()
    val _news = MutableLiveData<NetworkState<MutableList<News>>>()
    val news: LiveData<NetworkState<MutableList<News>>> = _news

    fun getNews() {
        val app = getApplication<MainApplication>()
        _news.postValue(NetworkState.Loading())

        viewModelScope.launch {
            try {
                if (Network.isConnect(app)) {
                    val disposable = repository.getNews()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { response ->
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        _news.postValue(NetworkState.Success(it))
                                    } ?: _news.postValue(NetworkState.Error(Throwable("No data")))
                                } else {
                                    _news.postValue(NetworkState.Error(Throwable("Error: ${response.code()}")))
                                }
                            },
                            { throwable ->
                                _news.postValue(NetworkState.Error(throwable))
                            }
                        )

                    compositeDisposable.add(disposable)
                } else {
                    _news.postValue(NetworkState.Error(Throwable("No internet connection")))
                }
            } catch (t: Throwable) {
                _news.postValue(NetworkState.Error(t))
            }
        }
    }

    public override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}