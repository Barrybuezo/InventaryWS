package com.example.inventaryws.core.firebase.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ProductDto(
    @DocumentId
    val documentId : String? = "",
    val name: String?,
    val category: String?,
    val amount: Double?,
    val stock: Int?,
    val minimumStock: Int?,
    val urlImg: String?,
    @ServerTimestamp
    val createdAd: Date? = null //No lo necesitamos en el modelo porque no se muestra a la UI
)
