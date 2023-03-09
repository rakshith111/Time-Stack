package com.example.timestackarchitecture.di

import android.app.Application
import androidx.room.Room
import com.example.timestackarchitecture.data.StackDatabase
import com.example.timestackarchitecture.data.StackRepository
import com.example.timestackarchitecture.data.StackRepositoryImpl
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

}