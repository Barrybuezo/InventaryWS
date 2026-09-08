package com.example.inventaryws.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class NavRoutes : NavKey {
    @Serializable
    data object Home : NavRoutes()

    @Serializable
    data object Create : NavRoutes()

    @Serializable
    data class Detail(val productId: String) : NavRoutes()

    @Serializable
    data class Edit(val productId: String) : NavRoutes()



}