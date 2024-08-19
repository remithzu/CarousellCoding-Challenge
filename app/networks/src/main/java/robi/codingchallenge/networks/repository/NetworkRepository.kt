package robi.codingchallenge.networks.repository

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import robi.codingchallenge.networks.NetworkApi
import robi.codingchallenge.networks.data.News

abstract class NetworkRepository(private val api: NetworkApi) {
    fun getNews(): Single<Response<ArrayList<News>>> {
        return api.carousellNews()
    }
}