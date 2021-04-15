package com.koleychik.service

import Calculator
import android.app.Service
import android.content.Intent
import android.os.IBinder

class CalculatorService : Service() {
    override fun onBind(intent: Intent?): IBinder = object : Calculator.Stub() {
        override fun sum(first: Int, second: Int): Int {
            return first + second
        }
    }
}