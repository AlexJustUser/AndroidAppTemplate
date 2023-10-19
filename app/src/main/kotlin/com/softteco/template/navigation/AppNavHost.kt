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
import com.softteco.template.ui.feature.bluetooth.sensor.BluetoothScreen
import com.softteco.template.ui.feature.chart.ChartScreen

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
        bluetoothGraph(navController)
    }
}

fun NavGraphBuilder.bluetoothGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Bluetooth.route,
        route = Graph.Bluetooth.route
    ) {
        composable(Screen.Bluetooth.route) {
            BluetoothScreen(
                onConnect = {
                    navController.navigate(Screen.Chart.route) }
            )
        }
        composable(Screen.Chart.route) {
            ChartScreen(
                onBackClicked = { navController.navigateUp() }
            )
        }
    }
}
