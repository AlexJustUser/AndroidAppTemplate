package com.softteco.template.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.softteco.template.ui.feature.bluetooth.BluetoothScreen
import com.softteco.template.ui.feature.details.DetailsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.padding(paddingValues = paddingValues)
    ) {
        bottomBarGraph(navController)
    }
}

fun NavGraphBuilder.bottomBarGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Bluetooth.route,
        route = Graph.Bluetooth.route
    ) {
        composable(Screen.Bluetooth.route) {
            BluetoothScreen(
                onDetailsViewClicked = { navController.navigate(Screen.DetailsView.route) }
            )
        }
        composable(Screen.DetailsView.route) {
            DetailsScreen(
                onBackClicked = { navController.navigateUp() }
            )
        }
    }
}
