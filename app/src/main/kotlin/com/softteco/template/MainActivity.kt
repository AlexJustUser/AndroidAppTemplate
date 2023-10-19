package com.softteco.template

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.softteco.template.Constants.BLUETOOTH_MODULE
import com.softteco.template.ui.AppContent
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.utils.BluetoothHelper
import com.softteco.template.utils.initBluetooth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val resultBluetoothEnableLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    val resultLocationEnableLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    lateinit var bluetoothHelper: BluetoothHelper
    lateinit var bluetoothReceiver: BroadcastReceiver
    val broadcastFilter = IntentFilter(BLUETOOTH_MODULE)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initBluetooth()
        setContent {
            AppTheme {
                AppContent()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bluetoothHelper.registerReceiver()
        bluetoothHelper.provideBluetoothOperation()
    }

    override fun onPause() {
        super.onPause()
        bluetoothHelper.unregisterReceiver()
    }
}
