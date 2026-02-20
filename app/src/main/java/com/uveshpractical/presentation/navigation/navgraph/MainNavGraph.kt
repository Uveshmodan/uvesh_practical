package com.uveshpractical.presentation.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.uveshpractical.presentation.navigation.route.Route
import com.uveshpractical.presentation.screen.DetailScreen
import com.uveshpractical.presentation.screen.ListScreen

@Composable
fun MainNavGraph() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.ListScreen
    ) {
        composable<Route.ListScreen> {
            ListScreen {
                navController.navigate(Route.DetailsScreen(it))
            }
        }

        composable<Route.DetailsScreen> {
            val argument = it.toRoute<Route.DetailsScreen>()
            DetailScreen(argument.id)
        }
    }
}