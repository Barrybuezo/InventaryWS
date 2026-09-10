package com.example.inventaryws.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.inventaryws.core.util.back
import com.example.inventaryws.core.util.navigateTo
import com.example.inventaryws.create.ui.CreateScreen
import com.example.inventaryws.detail.ui.DetailScreen
import com.example.inventaryws.edit.ui.EditScreen
import com.example.inventaryws.home.ui.HomeScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    val rootBackStack = rememberNavBackStack(NavRoutes.Home)

    NavDisplay(
        backStack = rootBackStack,
        modifier = modifier,
        onBack = { rootBackStack.back()},
        entryProvider = entryProvider {

            entry<NavRoutes.Home> {
                HomeScreen(
                    onProductClick = { documentId ->
                        rootBackStack.navigateTo(NavRoutes.Detail(documentId))
                    },
                    onAddClick = {
                        rootBackStack.navigateTo(NavRoutes.Create)
                    }
                )
            }

            entry<NavRoutes.Create> {
                CreateScreen()
            }

            entry<NavRoutes.Detail> { key ->
                DetailScreen(
                    productId = key.productId,
                    navigateToHome = {
                    rootBackStack.removeLastOrNull()
                },
                    navigateToEditScreen = { documentId ->
                        rootBackStack.add(NavRoutes.Edit(documentId))
                    }
                )
            }

            entry<NavRoutes.Edit> { key ->
                EditScreen(productId = key.productId,
                    navigateToDetail = {
                        rootBackStack.removeLastOrNull()
                    }
                )
            }



        }
    )
}