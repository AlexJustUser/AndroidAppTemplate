package com.softteco.template.navigation

sealed class Graph(val route: String) {
    object Bluetooth : Graph("bluetooth_graph")
}
