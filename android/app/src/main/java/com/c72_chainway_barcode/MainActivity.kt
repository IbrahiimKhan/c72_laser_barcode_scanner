package com.c72_chainway_barcode

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import android.view.KeyEvent
import com.github.kevinejohn.keyevent.KeyEventModule

class MainActivity : ReactActivity() {

    override fun getMainComponentName(): String = "c72_chainway_barcode"

    override fun createReactActivityDelegate(): ReactActivityDelegate =
        DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        KeyEventModule.getInstance().onKeyDownEvent(keyCode, event)
        return super.onKeyDown(keyCode, event)
    }
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
    KeyEventModule.getInstance().onKeyUpEvent(keyCode, event)
    return super.onKeyUp(keyCode, event)
}

}
