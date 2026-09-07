package top.niunaijun.varuna_dumper.core.system.dump;

import top.niunaijun.varuna_dumper.entity.dump.DumpResult;

interface IBDumpMonitor {
    void onDump(in DumpResult result);
}