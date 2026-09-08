package com.example.inventaryws.home.domain.model

import com.example.inventaryws.core.domain.model.Product

enum class StockStatus {
    NORMAL,   // verde
    WARNING,  // amarillo - cerca de quedarse bajo
    LOW       // rojo - ya está bajo
}

fun Product.stockStatus(): StockStatus {
    val warningThreshold = minimumStock * 1.1 // 10% por encima del mínimo

    return when {
        stock <= minimumStock -> StockStatus.LOW
        stock <= warningThreshold -> StockStatus.WARNING
        else -> StockStatus.NORMAL
    }
}