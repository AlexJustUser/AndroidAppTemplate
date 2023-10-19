package com.softteco.template.ui.feature.bluetooth.sensor

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.MainActivity
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.theme.Dimens.PaddingDefault
import com.softteco.template.ui.theme.Dimens.PaddingSmall
import no.nordicsemi.android.support.v18.scanner.ScanResult

private var bluetoothDevices = hashMapOf<String, ScanResult>()
private lateinit var activity: MainActivity

@Composable
fun BluetoothScreen(
    viewModel: BluetoothSensorViewModel = hiltViewModel(),
    onConnect: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    activity = LocalContext.current as MainActivity

    activity.bluetoothHelper.onScanResult = {
        viewModel.addBluetoothDevice(it)
    }

    activity.bluetoothHelper.onConnect = {
        activity.runOnUiThread {
            onConnect.invoke()
        }
    }

    ScreenContent(state)
}

@Composable
private fun ScreenContent(
    state: BluetoothSensorViewModel.State
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTopAppBar(
            stringResource(id = R.string.bluetooth),
            showBackIcon = false,
            modifier = Modifier.fillMaxWidth()
        )

        BluetoothDevicesList(state)
    }
}

@Composable
fun BluetoothDevicesList(
    state: BluetoothSensorViewModel.State
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(PaddingDefault)
    ) {
        state.bluetoothDevice?.let {
            bluetoothDevices[it.device.address] = it

            bluetoothDevices.toList().asReversed().forEach {
                item {
                    BluetoothDeviceCard(
                        it.second
                    )
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceCard(
    scanResult: ScanResult
) {
    Card(
        modifier = Modifier
            .padding(PaddingDefault)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.ic_bluetooth),
                contentDescription = "ic_bluetooth",
                modifier = Modifier
                    .padding(PaddingSmall),
                contentScale = ContentScale.Fit
            )
            Column(
                Modifier
                    .weight(1F, true)
                    .padding(PaddingSmall)
            ) {
                Text(
                    text = scanResult.device.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = scanResult.device.address,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.5F, false)
                    .fillMaxWidth()
                    .padding(PaddingSmall),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Button(
                    onClick = {
                        activity.bluetoothHelper.connectToBluetoothDevice(scanResult)
                    }) {
                    Text(
                        stringResource(id = R.string.view),
                        maxLines = 1
                    )
                }
            }
        }
    }
}
