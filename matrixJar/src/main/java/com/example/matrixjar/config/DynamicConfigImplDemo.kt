package com.example.matrixjar.config

import android.content.Context
import android.util.Log
import com.tencent.matrix.trace.TracePlugin
import com.tencent.matrix.trace.config.TraceConfig
import com.tencent.matrix.util.MatrixLog
import com.tencent.mrs.plugin.IDynamicConfig
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @Description:
 * @author Jillian
 * @date 2024/7/23
 */
class DynamicConfigImplDemo : IDynamicConfig {
    companion object {
        private const val TAG = "Matrix.DynamicConfigImplDemo"
    }

    fun isFPSEnable(): Boolean {
        return true
    }

    fun isTraceEnable(): Boolean {
        return true
    }

    fun isSignalAnrTraceEnable(): Boolean {
        return true
    }

    fun isMatrixEnable(): Boolean {
        return true
    }

    override fun get(key: String, defStr: String): String {
        // TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.

        // for Activity leak detect
        if (IDynamicConfig.ExptEnum.clicfg_matrix_resource_detect_interval_millis.name == key ||
            IDynamicConfig.ExptEnum.clicfg_matrix_resource_detect_interval_millis_bg.name == key) {
            Log.d("DynamicConfig", "Matrix.ActivityRefWatcher: clicfg_matrix_resource_detect_interval_millis 10s")
            return TimeUnit.SECONDS.toMillis(5).toString()
        }

        if (IDynamicConfig.ExptEnum.clicfg_matrix_resource_max_detect_times.name == key) {
            Log.d("DynamicConfig", "Matrix.ActivityRefWatcher: clicfg_matrix_resource_max_detect_times 5")
            return "3"
        }

        return defStr
    }

    override fun get(key: String, defInt: Int): Int {
        // TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.

        if (MatrixEnum.clicfg_matrix_resource_max_detect_times.name == key) {
            MatrixLog.i(TAG, "key:$key, before change:$defInt, after change, value:2")
            return 2 // new value
        }

        if (MatrixEnum.clicfg_matrix_trace_fps_report_threshold.name == key) {
            return 10000
        }

        if (MatrixEnum.clicfg_matrix_trace_fps_time_slice.name == key) {
            return 12000
        }

        return defInt
    }

    override fun get(key: String, defLong: Long): Long {
        // TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.

        if (MatrixEnum.clicfg_matrix_trace_fps_report_threshold.name == key) {
            return 10000L
        }

        if (MatrixEnum.clicfg_matrix_resource_detect_interval_millis.name == key) {
            MatrixLog.i(TAG, "$key, before change:$defLong, after change, value:2000")
            return 2000
        }

        return defLong
    }

    override fun get(key: String, defBool: Boolean): Boolean {
        // TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.

        return defBool
    }

    override fun get(key: String, defFloat: Float): Float {
        // TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.

        return defFloat
    }

    fun configureTracePlugin(dynamicConfig: DynamicConfigImplDemo, applicationContext: Context): TracePlugin {
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