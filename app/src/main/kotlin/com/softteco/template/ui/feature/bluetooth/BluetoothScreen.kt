package com.softteco.template.ui.feature.bluetooth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.data.bluetooth.BluetoothDevice
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.TextSnackbarContainer

@Composable
fun BluetoothScreen(
    modifier: Modifier = Modifier,
    viewModel: BluetoothViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    //Mock data
    val bluetoothDevices = mutableListOf(
        BluetoothDevice("first", "00:00:00:00:01", 1),
        BluetoothDevice("second", "00:00:00:00:02", 2),
        BluetoothDevice("third", "00:00:00:00:03", 3)
    )

    ScreenContent(
        state,
        modifier,
        bluetoothDevices,
        onBackClicked
    )
}

@Composable
private fun ScreenContent(
    state: BluetoothViewModel.State,
    modifier: Modifier = Modifier,
    bluetoothDevices: MutableList<BluetoothDevice>,
    onBackClicked: () -> Unit = {}
) {
    TextSnackbarContainer(
        modifier = modifier,
        snackbarText = stringResource(state.snackBar.textId),
        showSnackbar = state.snackBar.show,
        onDismissSnackbar = state.dismissSnackBar,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CustomTopAppBar(
                stringResource(id = R.string.bluetooth),
                showBackIcon = true,
                modifier = Modifier.fillMaxWidth(),
                onBackClicked = onBackClicked
            )
            BluetoothDevicesList(bluetoothDevices)
        }
    }
}

@Composable
fun BluetoothDevicesList(bluetoothDevices: MutableList<BluetoothDevice>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(bluetoothDevices) { bluetoothDevice ->
            BluetoothDeviceCard(
                bluetoothDevice.name,
                bluetoothDevice.macAddress,
                bluetoothDevice.rssi
            )
        }
    }
}

@Composable
fun BluetoothDeviceCard(name: String, macAddress: String, rssi: Int) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_bluetooth),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = name
                )
                Text(
                    text = macAddress
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_signal_level),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = rssi.toString()
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Button(onClick = {}) {
                    Text(stringResource(id = R.string.connect))
                }
            }
        }
    }
}
