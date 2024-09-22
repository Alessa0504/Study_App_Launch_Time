package com.example.study_app_launch_time

import android.app.Application
import com.example.matrixjar.config.DynamicConfigImplDemo
import com.example.matrixjar.listener.TestPluginListener
import com.tencent.matrix.Matrix
import com.tencent.matrix.util.MatrixLog


/**
 * @Description:
 * @author Jillian
 * @date 2024/7/23
 */
class MatrixApplication: Application() {
    val TAG: String = "Matrix.Application"

    override fun onCreate() {
        super.onCreate()

        MatrixLog.i(TAG, "Start Matrix configurations.")

        val builder = Matrix.Builder(this) // build matrix
        builder.pluginListener(TestPluginListener(this))  // add general pluginListener
        val dynamicConfig = DynamicConfigImplDemo() // dynamic config

        // Configure trace canary.
        val tracePlugin = dynamicConfig.configureTracePlugin(dynamicConfig, applicationContext)

//        val tracePlugin2 = TracePlugin(TraceConfig.Builder()
//            .enableEvilMethodTrace(true)
////            .enableSignalAnrTrace(true)
////            .enableAnrTrace(true)
//            .build())

        builder.plugin(tracePlugin)
        Matrix.init(builder.build())
        // Trace Plugin need call start() at the beginning.
        Matrix.with().startAllPlugins()
        MatrixLog.i(TAG, "Matrix configurations done.")
    }
}