package com.example.study_app_launch_time

import android.app.Application
import com.example.study_app_launch_time.config.DynamicConfigImplDemo
import com.example.study_app_launch_time.listener.TestPluginListener
import com.tencent.matrix.Matrix
import com.tencent.matrix.trace.TracePlugin
import com.tencent.matrix.trace.config.TraceConfig
import com.tencent.matrix.util.MatrixLog
import java.io.File


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
        val tracePlugin = configureTracePlugin(dynamicConfig)
        builder.plugin(tracePlugin)
        Matrix.init(builder.build())
        // Trace Plugin need call start() at the beginning.
        Matrix.with().startAllPlugins()
        MatrixLog.i(TAG, "Matrix configurations done.")
    }

    private fun configureTracePlugin(dynamicConfig: DynamicConfigImplDemo): TracePlugin {
        val fpsEnable: Boolean = dynamicConfig.isFPSEnable()
        val traceEnable: Boolean = dynamicConfig.isTraceEnable()
        val signalAnrTraceEnable: Boolean = dynamicConfig.isSignalAnrTraceEnable()

        val traceFileDir = File(applicationContext.filesDir, "matrix_trace")
        if (!traceFileDir.exists()) {
            if (traceFileDir.mkdirs()) {
                MatrixLog.e(TAG, "failed to create traceFileDir")
            }
        }

        val anrTraceFile = File(
            traceFileDir,
            "anr_trace"
        ) // path : /data/user/0/sample.tencent.matrix/files/matrix_trace/anr_trace
        val printTraceFile = File(
            traceFileDir,
            "print_trace"
        ) // path : /data/user/0/sample.tencent.matrix/files/matrix_trace/print_trace

        val traceConfig: TraceConfig = TraceConfig.Builder()
            .dynamicConfig(dynamicConfig)
            .enableFPS(fpsEnable)
            .enableEvilMethodTrace(traceEnable)
            .enableAnrTrace(traceEnable)
            .enableStartup(traceEnable)
            .enableIdleHandlerTrace(traceEnable) // Introduced in Matrix 2.0
            .enableSignalAnrTrace(signalAnrTraceEnable) // Introduced in Matrix 2.0
            .anrTracePath(anrTraceFile.absolutePath)
            .printTracePath(printTraceFile.absolutePath)
            .splashActivities("sample.tencent.matrix.SplashActivity;")
            .isDebug(true)
            .isDevEnv(false)
            .build()

        //Another way to use SignalAnrTracer separately
        //useSignalAnrTraceAlone(anrTraceFile.getAbsolutePath(), printTraceFile.getAbsolutePath());
        return TracePlugin(traceConfig)
    }
}