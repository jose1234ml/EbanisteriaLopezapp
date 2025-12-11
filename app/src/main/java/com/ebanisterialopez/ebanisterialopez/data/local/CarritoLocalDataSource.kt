package com.ebanisterialopez.ebanisterialopez.data.datasource.local

import com.ebanisterialopez.ebanisterialopez.data.dto.CarritoDao
import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CarritoLocalDataSource @Inject constructor(
    private val carritoDao: CarritoDao
) {
    suspend fun getItems(): List<CarritoItemEntity> {
        return carritoDao.getCartFlow().first()
    }
}