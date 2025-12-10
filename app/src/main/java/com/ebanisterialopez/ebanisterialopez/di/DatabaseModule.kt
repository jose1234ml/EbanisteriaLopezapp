package com.ebanisterialopez.ebanisterialopez.di

import android.content.Context
import androidx.room.Room
import com.ebanisterialopez.ebanisterialopez.data.local.AppDatabase
import com.ebanisterialopez.ebanisterialopez.data.dto.CarritoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ebanisteria_db"
        ).build()
    }
    @Provides
    fun provideCarritoDao(db: AppDatabase): CarritoDao = db.carritoDao()
}