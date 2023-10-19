package com.softteco.template.ui.feature.bluetooth.sensor

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.ui.components.SnackBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.nordicsemi.android.support.v18.scanner.ScanResult
import javax.inject.Inject

@HiltViewModel
class BluetoothSensorViewModel @Inject constructor() : ViewModel() {

    private var snackBarState = MutableStateFlow(SnackBarState())
    private var bluetoothDevice = MutableStateFlow<ScanResult?>(null)

    val state = combine(
        snackBarState,
        bluetoothDevice
    ) { snackBar, bluetoothDevice ->
        State(
            bluetoothDevice = bluetoothDevice,
            snackBar = snackBar,
            dismissSnackBar = { snackBarState.value = SnackBarState() }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    @SuppressLint("MissingPermission")
    fun addBluetoothDevice(scanResult: ScanResult) {
        viewModelScope.launch {
            bluetoothDevice.value = scanResult
        }
    }

    @Immutable
    data class State(
        val bluetoothDevice: ScanResult? = null,
        val snackBar: SnackBarState = SnackBarState(),
        val dismissSnackBar: () -> Unit = {}
    )
}
