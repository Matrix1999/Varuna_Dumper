package top.niunaijun.varuna_dumper.view.main

import androidx.lifecycle.MutableLiveData
import top.niunaijun.varuna_dumper.data.DexDumpRepository
import top.niunaijun.varuna_dumper.data.entity.AppInfo
import top.niunaijun.varuna_dumper.data.entity.DumpInfo
import top.niunaijun.varuna_dumper.view.base.BaseViewModel

/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/5/23 14:29
 */
class MainViewModel(private val repo: DexDumpRepository) : BaseViewModel() {

    val mAppListLiveData = MutableLiveData<List<AppInfo>>()

    val mDexDumpLiveData = MutableLiveData<DumpInfo>()


    fun getAppList() {
        launchOnUI {
            repo.getAppList(mAppListLiveData)
        }
    }

    fun startDexDump(source: String) {
        launchOnUI {
            repo.dumpDex(source, mDexDumpLiveData)
        }
    }

    fun dexDumpSuccess() {
        launchOnUI {
            repo.dumpSuccess()
        }
    }

}