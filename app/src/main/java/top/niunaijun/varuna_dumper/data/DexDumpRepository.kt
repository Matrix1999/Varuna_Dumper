package top.niunaijun.varuna_dumper.data

import android.content.pm.ApplicationInfo
import android.net.Uri
import android.webkit.URLUtil
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.niunaijun.varuna_dumper.VarunaDumperCore
import top.niunaijun.varuna_dumper.VarunaDumperCore.getPackageManager
import top.niunaijun.varuna_dumper.VarunaDexCore
import top.niunaijun.varuna_dumper.entity.pm.InstallResult
import top.niunaijun.varuna_dumper.utils.AbiUtils
import top.niunaijun.varuna_dumper.R
import top.niunaijun.varuna_dumper.app.App
import top.niunaijun.varuna_dumper.app.AppManager
import top.niunaijun.varuna_dumper.data.entity.AppInfo
import top.niunaijun.varuna_dumper.data.entity.DumpInfo
import java.io.File

/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/5/23 14:29
 */
class DexDumpRepository {

    private var dumpTaskId = 0

    fun getAppList(mAppListLiveData: MutableLiveData<List<AppInfo>>) {

        val installedApplications: List<ApplicationInfo> =
                getPackageManager().getInstalledApplications(0)
        val installedList = mutableListOf<AppInfo>()

        for (installedApplication in installedApplications) {
            val file = File(installedApplication.sourceDir)

            if ((installedApplication.flags and ApplicationInfo.FLAG_SYSTEM) != 0) continue

            if (!AbiUtils.isSupport(file)) continue


            val info = AppInfo(
                    installedApplication.loadLabel(getPackageManager()).toString(),
                    installedApplication.packageName,
                    installedApplication.loadIcon(getPackageManager())
            )
            installedList.add(info)
        }

        mAppListLiveData.postValue(installedList)
    }

    fun dumpDex(source: String, dexDumpLiveData: MutableLiveData<DumpInfo>) {
        dexDumpLiveData.postValue(DumpInfo(DumpInfo.LOADING))
        val result = if (URLUtil.isValidUrl(source)) {
            VarunaDexCore.get().dumpDex(Uri.parse(source))
        } else if (source.contains("/")) {
            VarunaDexCore.get().dumpDex(File(source))
        } else {
            VarunaDexCore.get().dumpDex(source)
        }

        if (result != null) {
            dumpTaskId++
            startCountdown(result, dexDumpLiveData)
        } else {
            dexDumpLiveData.postValue(DumpInfo(DumpInfo.TIMEOUT))
        }
    }


    fun dumpSuccess() {
        dumpTaskId++
    }

    private fun startCountdown(installResult: InstallResult, dexDumpLiveData: MutableLiveData<DumpInfo>) {
        GlobalScope.launch {
            val tempId = dumpTaskId
            while (VarunaDexCore.get().isRunning) {
                delay(20000)
                //10s
                if (!AppManager.mVarunaDumperLoader.isFixCodeItem()) {
                    break
                }
                //fixCodeItem 需要长时间运行，普通内存dump不需要
            }
            if (tempId == dumpTaskId) {
                if (VarunaDexCore.get().isExistDexFile(installResult.packageName)) {
                    dexDumpLiveData.postValue( DumpInfo(
                            DumpInfo.SUCCESS,
                            App.getContext().getString(R.string.dex_save, File(VarunaDumperCore.get().dexDumpDir, installResult.packageName).absolutePath)
                    ))
                } else {
                    dexDumpLiveData.postValue(DumpInfo(DumpInfo.TIMEOUT))
                }
            }
        }
    }
}