package robi.codingchallenge.carousellnews.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import robi.codingchallenge.carousellnews.ui.NewsViewModel
import robi.codingchallenge.networks.repository.NetworkRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideNewsViewModel(application: Application, repository: NetworkRepository) =
        NewsViewModel(application, repository)
}