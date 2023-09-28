package com.softteco.template.ui.feature.bluetooth

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.MainActivity
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.theme.Dimens.PaddingDefault
import com.softteco.template.ui.theme.Dimens.PaddingSmall
import com.softteco.template.utils.BluetoothHelper
import no.nordicsemi.android.support.v18.scanner.ScanResult

private lateinit var bluetoothHelper: BluetoothHelper
private var bluetoothDevices = hashMapOf<String, ScanResult>()

@Composable
fun BluetoothScreen(
    viewModel: BluetoothViewModel = hiltViewModel(),
    onDetailsViewClicked: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    bluetoothHelper = BluetoothHelper(LocalContext.current as MainActivity, viewModel)
    bluetoothHelper.initBluetooth()
    bluetoothHelper.startScan()

    ScreenContent(state, onDetailsViewClicked)
}

@Composable
private fun ScreenContent(
    state: BluetoothViewModel.State,
    onDetailsViewClicked: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTopAppBar(
            stringResource(id = R.string.bluetooth),
            showBackIcon = false,
            modifier = Modifier.fillMaxWidth()
        )
        BluetoothDevicesList(state, onDetailsViewClicked)
    }
}

@Composable
fun BluetoothDevicesList(
    state: BluetoothViewModel.State,
    onDetailsViewClicked: () -> Unit = {}
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
                        it.second,
                        onDetailsViewClicked
                    )
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceCard(
    scanResult: ScanResult,
    onDetailsViewClicked: () -> Unit = {}
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
            Column(Modifier.padding(PaddingSmall)) {
                Text(text = scanResult.device.name)
                Text(text = scanResult.device.address)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingSmall),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Button(onClick = {
                    bluetoothHelper.connectToBluetoothDevice(scanResult)
                }) {
                    Text(stringResource(id = R.string.view))
                }
            }
        }
    }
}
