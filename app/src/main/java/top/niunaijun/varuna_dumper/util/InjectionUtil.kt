package top.niunaijun.varuna_dumper.util

import top.niunaijun.varuna_dumper.data.DexDumpRepository
import top.niunaijun.varuna_dumper.view.main.MainFactory


/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/4/29 22:38
 */
object InjectionUtil {

    private val dexDumpRepository = DexDumpRepository()


    fun getMainFactory() : MainFactory {
        return MainFactory(dexDumpRepository)
    }

}