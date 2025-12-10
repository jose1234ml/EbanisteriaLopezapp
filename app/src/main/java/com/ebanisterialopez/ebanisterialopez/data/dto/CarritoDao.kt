package com.ebanisterialopez.ebanisterialopez.data.dto

import androidx.room.*
import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    @Query("SELECT * FROM carrito_items")
    fun getCartFlow(): Flow<List<CarritoItemEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: CarritoItemEntity)
    @Update
    suspend fun updateItem(item: CarritoItemEntity)
    @Delete
    suspend fun removeItem(item: CarritoItemEntity)
    @Query("DELETE FROM carrito_items")
    suspend fun clearCart()
}