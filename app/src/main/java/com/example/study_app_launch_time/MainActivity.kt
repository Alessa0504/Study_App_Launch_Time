package com.example.study_app_launch_time

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        A()
    }


    private fun A() {
        B()
        H()
        L()
        SystemClock.sleep(800)
    }
    private fun B() {
        C()
        G()
        SystemClock.sleep(200)
    }
    private fun C() {
        D()
        E()
        F()
        SystemClock.sleep(100)
    }
    private fun D() {
        SystemClock.sleep(20)
    }
    private fun E() {
        SystemClock.sleep(20)
    }
    private fun F() {
        SystemClock.sleep(20)
    }
    private fun G() {
        SystemClock.sleep(20)
    }
    private fun H() {
        SystemClock.sleep(20)
        I()
        J()
        K()
    }
    private fun I() {
        SystemClock.sleep(20)
    }
    private fun J() {
        SystemClock.sleep(6)
    }
    private fun K() {
        SystemClock.sleep(10)
    }
    private fun L() {
        SystemClock.sleep(10000)
    }
}