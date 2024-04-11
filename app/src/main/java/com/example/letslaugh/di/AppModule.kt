package com.example.letslaugh.di

import android.content.Context
import androidx.room.Room
import com.example.letslaugh.common.Constants
import com.example.letslaugh.data.local.database.JokesDatabase
import com.example.letslaugh.data.remote.JokesApi
import com.example.letslaugh.data.remote.StripeApi
import com.example.letslaugh.data.repository.JokeRepositoryImpl
import com.example.letslaugh.data.repository.StripeApiImpl
import com.example.letslaugh.domain.repository.JokeRepository
import com.example.letslaugh.domain.repository.JokeRoomDao
import com.example.letslaugh.domain.repository.StripeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJokeApi(): JokesApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JokesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStripeApi(): StripeApi {
        return Retrofit.Builder().baseUrl(Constants.STRIPE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(StripeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStripeRepository(api: StripeApi) : StripeRepository {
        return StripeApiImpl(api)
    }

    @Provides
    @Singleton
    fun provideJokeRepository(api: JokesApi): JokeRepository {
        return JokeRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideJokeRoomDatabase(@ApplicationContext context: Context): JokesDatabase {
        return Room.databaseBuilder(
            context,
            JokesDatabase::class.java, JokesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideJokeRoomDao(jokesDatabase: JokesDatabase): JokeRoomDao {
        return jokesDatabase.jokeRoomDao()
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
}