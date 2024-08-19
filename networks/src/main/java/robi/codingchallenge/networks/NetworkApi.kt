package robi.codingchallenge.networks

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import robi.codingchallenge.networks.data.News

interface NetworkApi {
    @GET("/carousell-interview-assets/android/carousell_news.json")
    fun carousellNews(): Single<Response<ArrayList<News>>>
}