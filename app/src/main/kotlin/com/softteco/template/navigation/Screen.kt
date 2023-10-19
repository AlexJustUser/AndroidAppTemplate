package com.softteco.template.navigation

sealed class Screen(val route: String) {
    object Bluetooth : Screen("bluetooth")

    object Chart : Screen("chart")
}
