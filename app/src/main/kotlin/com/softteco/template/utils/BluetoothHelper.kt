package com.softteco.template.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.provider.Settings
import com.softteco.template.BuildConfig
import com.softteco.template.MainActivity
import com.softteco.template.model.DataLYWSD03MMC
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import java.util.UUID

class BluetoothHelper(private val activity: MainActivity) {

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var locationManager: LocationManager
    private lateinit var bluetoothPermissionChecker: BluetoothPermissionChecker
    var onConnect: (() -> Unit)? = null
    var onScanResult: ((scanResult: ScanResult) -> Unit)? = null
    var onDeviceResult: ((dataLYWSD03MMC: DataLYWSD03MMC) -> Unit)? = null
    private val startIndexOfTemperature = 0
    private val endIndexOfTemperature = 2
    private val indexOfHumidity = 2
    private val startIndexOfBattery = 3
    private val endIndexOfBattery = 5
    private val divisionValueOfValues = 100

    private val scanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(
            callbackType: Int,
            scanResult: ScanResult
        ) {
            super.onScanResult(callbackType, scanResult)
            scanResult.device.name?.let {
                onScanResult?.invoke(scanResult)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun initBluetooth() {
        activity.bluetoothReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.STATE_OFF
                )) {
                    BluetoothAdapter.STATE_ON -> {
                        provideBluetoothOperation()
                    }

                    BluetoothAdapter.STATE_OFF -> {
                        stopScan()
                    }
                }
            }
        }
        registerReceiver()
        bluetoothManager =
            activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        locationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        bluetoothPermissionChecker =
            BluetoothPermissionChecker(activity, bluetoothAdapter, locationManager)
    }

    private fun registerReceiver() {
        activity.registerReceiver(
            activity.bluetoothReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )
    }

    fun unregisterReceiver() {
        activity.unregisterReceiver(activity.bluetoothReceiver)
    }

    private fun startScan() {
        stopScan()
        BluetoothLeScannerCompat.getScanner().startScan(scanCallback)
    }

    fun stopScan() {
        BluetoothLeScannerCompat.getScanner().stopScan(scanCallback)
    }

    fun provideBluetoothOperation() {
        if (bluetoothPermissionChecker.checkBluetoothSupport()) {
            when (bluetoothPermissionChecker.checkEnableDeviceModules()) {
                PermissionType.LOCATION_TURNED_OFF -> {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    activity.resultLocationEnableLauncher.launch(intent)
                }

                PermissionType.BLUETOOTH_TURNED_OFF -> {
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    activity.resultBluetoothEnableLauncher.launch(intent)
                }

                PermissionType.BLUETOOTH_AND_LOCATION_TURNED_ON -> {
                    makeBluetoothOperation()
                }
            }
        }
    }

    private fun makeBluetoothOperation() {
        if (bluetoothPermissionChecker.hasPermissions()) {
            startScan()
        }
    }

    private val mGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices()
                onConnect?.invoke()
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                gatt?.let { gatt ->
                    gatt.getService(UUID.fromString(BuildConfig.BLUETOOTH_SERVICE_UUID_VALUE))
                        .getCharacteristic(UUID.fromString(BuildConfig.BLUETOOTH_CHARACTERISTIC_UUID_VALUE))
                        .let { characteristic ->
                            setCharacteristicNotification(gatt, characteristic, true)
                        }
                }
            }
        }

        @SuppressLint("MissingPermission")
        fun setCharacteristicNotification(
            bluetoothGatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            enable: Boolean
        ): Boolean {
            bluetoothGatt.setCharacteristicNotification(characteristic, enable)
            val descriptor =
                characteristic.getDescriptor(UUID.fromString(BuildConfig.BLUETOOTH_DESCRIPTOR_UUID_VALUE))
            descriptor.value =
                if (enable) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else byteArrayOf(
                    0x00,
                    0x00
                )
            return bluetoothGatt.writeDescriptor(descriptor)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            characteristic.value.let {
                val temp = characteristicByteConversation(
                    characteristic.value,
                    startIndexOfTemperature,
                    endIndexOfTemperature
                ) / divisionValueOfValues
                val hum = characteristic.value[indexOfHumidity].toInt()
                val bat = characteristicByteConversation(
                    characteristic.value,
                    startIndexOfBattery,
                    endIndexOfBattery
                )

                onDeviceResult?.invoke(DataLYWSD03MMC(temp, hum, bat))
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToBluetoothDevice(scanResult: ScanResult) {
        scanResult.device.connectGatt(
            activity.applicationContext,
            false,
            mGattCallback,
            BluetoothDevice.TRANSPORT_LE
        )
    }
}
