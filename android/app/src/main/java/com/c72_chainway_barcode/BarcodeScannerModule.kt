package com.c72_chainway_barcode

import android.app.Activity
import android.widget.Toast
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.rscja.barcode.BarcodeDecoder
import com.rscja.barcode.BarcodeFactory
import com.rscja.barcode.BarcodeUtility
import com.rscja.deviceapi.entity.BarcodeEntity

class BarcodeScannerModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private val barcodeDecoder: BarcodeDecoder = BarcodeFactory.getInstance().barcodeDecoder

    override fun getName(): String {
        return "BarcodeScannerModule"
    }

    @ReactMethod
    fun openScanner() {
        val activity: Activity? = currentActivity
        activity?.let {
            barcodeDecoder.open(it)

            barcodeDecoder.setDecodeCallback { barcodeEntity ->
                if (barcodeEntity.resultCode == BarcodeDecoder.DECODE_SUCCESS) {
                    val barcodeData = barcodeEntity.barcodeData
                    //Toast.makeText(it, "Scanned: $barcodeData", Toast.LENGTH_LONG).show()

                    // Send scanned data to React Native
                    sendEvent("onBarcodeScanned", barcodeData)
                } else {
                    sendEvent("onBarcodeScanned", "Scan Failed")
                }
            }

            // Optional settings
            BarcodeUtility.getInstance().enablePlaySuccessSound(it, true)
            BarcodeUtility.getInstance().enableVibrate(it, true)
        }
    }

    @ReactMethod
    fun startScan() {
        barcodeDecoder.startScan()
    }

    @ReactMethod
    fun stopScan() {
        barcodeDecoder.stopScan()
    }

    @ReactMethod
    fun closeScanner() {
        barcodeDecoder.close()
    }

    @ReactMethod
    fun addListener(eventName: String) {
        // Required for NativeEventEmitter in RN 0.65+
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        // Required for NativeEventEmitter in RN 0.65+
    }

    private fun sendEvent(eventName: String, data: String) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, data)
    }
}
