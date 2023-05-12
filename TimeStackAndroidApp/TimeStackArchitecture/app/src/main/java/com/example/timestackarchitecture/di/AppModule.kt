package com.example.timestackarchitecture.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.timestackarchitecture.casualmode.data.StackDatabase
import com.example.timestackarchitecture.casualmode.data.StackRepository
import com.example.timestackarchitecture.casualmode.data.StackRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStackDatabase(app: Application) : StackDatabase {
        return Room.databaseBuilder(
            app,
            StackDatabase::class.java,
            "converter_data_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideStackRepository(db : StackDatabase) : StackRepository {
        return StackRepositoryImpl(db.stackDAO)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

}