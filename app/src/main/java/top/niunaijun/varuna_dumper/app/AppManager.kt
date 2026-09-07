package top.niunaijun.varuna_dumper.app

import android.content.Context

object AppManager {
    @JvmStatic
    val mVarunaDumperLoader by lazy {
        VarunaDumperLoader()
    }

    fun doAttachBaseContext(context: Context) {
        try {
            mVarunaDumperLoader.attachBaseContext(context)
            mVarunaDumperLoader.addLifecycleCallback()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun doOnCreate(context: Context) {
        mVarunaDumperLoader.doOnCreate(context)
        initThirdService(context)
    }

    private fun initThirdService(context: Context) {}
}