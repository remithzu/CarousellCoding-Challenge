package robi.codingchallenge.networks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import robi.codingchallenge.networks.BuildConfig
import robi.codingchallenge.networks.NetworkApi
import robi.codingchallenge.networks.repository.NetworkRepository
import robi.codingchallenge.networks.repository.NetworkRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkApi() : NetworkApi {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.HOST)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NetworkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(api: NetworkApi): NetworkRepository {
        return NetworkRepositoryImpl(api)
    }
}