package com.softteco.template.ui.feature.bluetooth

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.ui.components.SnackBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor() : ViewModel() {

    private val loading = MutableStateFlow(false)
    private var snackBarState = MutableStateFlow(SnackBarState())

    val state = combine(
        loading,
        snackBarState,
    ) { loading, snackBar ->
        State(
            loading = loading,
            snackBar = snackBar,
            dismissSnackBar = { snackBarState.value = SnackBarState() }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    @Immutable
    data class State(
        val loading: Boolean = false,
        val loginState: Boolean = false,
        val snackBar: SnackBarState = SnackBarState(),
        val dismissSnackBar: () -> Unit = {}
    )
}
