package com.softteco.template.model

import androidx.compose.runtime.Immutable

@Immutable
data class DataLYWSD03MMC(
    val temperature: Double = 0.0,
    val humidity: Int = 0,
    val battery: Double = 0.0
)