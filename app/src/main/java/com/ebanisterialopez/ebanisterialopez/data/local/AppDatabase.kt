package com.ebanisterialopez.ebanisterialopez.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ebanisterialopez.ebanisterialopez.data.dto.CarritoDao

@Database(entities = [CarritoItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carritoDao(): CarritoDao
}