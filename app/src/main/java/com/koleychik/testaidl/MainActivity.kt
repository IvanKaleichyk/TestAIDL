package com.koleychik.testaidl

import AsyncCallback
import Calculator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.koleychik.aidl.Sum

class MainActivity : AppCompatActivity() {

    private val tag = "MAIN_APP_TAG"

    private val edt1: EditText by lazy {
        findViewById(R.id.edt1)
    }
    private val edt2: EditText by lazy {
        findViewById(R.id.edt2)
    }
    private val btn: Button by lazy {
        findViewById(R.id.btn)
    }

    private var calculator: Calculator? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            calculator = Calculator.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            calculator = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            calculator?.sum(
                edt1.text.toString().trim().toInt(),
                edt2.text.toString().trim().toInt(),
                object : AsyncCallback.Stub() {
                    override fun onSuccessfull(sum: Sum) {
                        Log.d(tag, "sum = ${sum.result}")
                    }
                }
            ) ?: Log.d(tag, "calculator = null")
        }
    }

    private fun Context.showToast(test: String) {
        Toast.makeText(this, test, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        bindService(createExplicitIntent(), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }

    private fun createExplicitIntent(): Intent {
        val intent = Intent("com.koleychik.aidl.REMOTE_CONNECTION")
        val service = packageManager.queryIntentServices(intent, 0)
        if (service.isEmpty()) {
            throw IllegalStateException("Application service isn't installed")
        }
        return Intent().apply {
            val serviceInfo = service[0].serviceInfo
            val packageName = serviceInfo.packageName
            val className = serviceInfo.name
            component = ComponentName(packageName, className)
        }
    }
}