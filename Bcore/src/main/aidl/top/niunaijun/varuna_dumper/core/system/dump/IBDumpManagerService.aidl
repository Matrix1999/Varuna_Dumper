// IBDumpService.aidl
package top.niunaijun.varuna_dumper.core.system.dump;

import android.os.IBinder;
import top.niunaijun.varuna_dumper.entity.dump.DumpResult;

interface IBDumpManagerService {
    void registerMonitor(IBinder monitor);
    void unregisterMonitor(IBinder monitor);
    void noticeMonitor(in DumpResult result);
}