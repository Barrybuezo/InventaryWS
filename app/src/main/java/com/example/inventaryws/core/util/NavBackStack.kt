package com.example.inventaryws.core.util

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateTo(route: NavKey){ //Pone el plato hasta arriba(la pantalla)
    add(route)
}

fun NavBackStack<NavKey>.backTo(route: NavKey){//Ir a un plato quitando todos los que estén encima
    if(isEmpty()) return
    if (route !in this) return

    while (isNotEmpty() && last() != route){
        removeLastOrNull()
    }
}

fun NavBackStack<NavKey>.back(){//Quita el plato de hasta arriba
    if(isEmpty()) return
    removeLastOrNull()
}