package top.niunaijun.varuna_dumper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Process;

import java.io.File;
import java.util.List;

import top.niunaijun.varuna_dumper.app.configuration.ClientConfiguration;
import top.niunaijun.varuna_dumper.core.system.dump.IBDumpMonitor;
import top.niunaijun.varuna_dumper.entity.pm.InstallResult;
import top.niunaijun.varuna_dumper.proxy.ProxyManifest;

/**
 * Created by Milk on 2021/5/22.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class VarunaDexCore {
    public static final String TAG = "VarunaDumperCore";

    private static final VarunaDexCore sVarunaDexCore = new VarunaDexCore();

    public static VarunaDexCore get() {
        return sVarunaDexCore;
    }

    public void doAttachBaseContext(Context context, ClientConfiguration clientConfiguration) {
        VarunaDumperCore.get().doAttachBaseContext(context, clientConfiguration);
    }

    public void doCreate() {
        VarunaDumperCore.get().doCreate();
        // uninstall all pckage
        if (VarunaDumperCore.get().isMainProcess()) {
            List<PackageInfo> installedPackages =
                    VarunaDumperCore.getBPackageManager().getInstalledPackages(0, VarunaDumperCore.USER_ID);
            for (PackageInfo installedPackage : installedPackages) {
                VarunaDumperCore.get().uninstallPackage(installedPackage.packageName);
            }
        }
    }

    public InstallResult dumpDex(String packageName) {
        InstallResult installResult = VarunaDumperCore.get().installPackage(packageName);
        if (installResult.success) {
            boolean b = VarunaDumperCore.get().launchApk(packageName);
            if (!b) {
                VarunaDumperCore.get().uninstallPackage(installResult.packageName);
                return null;
            }
            return installResult;
        } else {
            return null;
        }
    }

    public InstallResult dumpDex(File file) {
        InstallResult installResult = VarunaDumperCore.get().installPackage(file);
        if (installResult.success) {
            boolean b = VarunaDumperCore.get().launchApk(installResult.packageName);
            if (!b) {
                VarunaDumperCore.get().uninstallPackage(installResult.packageName);
                return null;
            }
            return installResult;
        } else {
            return null;
        }
    }

    public InstallResult dumpDex(Uri file) {
        InstallResult installResult = VarunaDumperCore.get().installPackage(file);
        if (installResult.success) {
            boolean b = VarunaDumperCore.get().launchApk(installResult.packageName);
            if (!b) {
                VarunaDumperCore.get().uninstallPackage(installResult.packageName);
                return null;
            }
            return installResult;
        } else {
            return null;
        }
    }

    public void registerDumpMonitor(IBDumpMonitor monitor) {
        VarunaDumperCore.getBDumpManager().registerMonitor(monitor.asBinder());
    }

    public void unregisterDumpMonitor(IBDumpMonitor monitor) {
        VarunaDumperCore.getBDumpManager().unregisterMonitor(monitor.asBinder());
    }

    public boolean isRunning() {
        ActivityManager am = (ActivityManager) VarunaDumperCore.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            for (int i = 0; i < ProxyManifest.FREE_COUNT; i++) {
                if (info.processName.endsWith("p" + i)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isExistDexFile(String packageName) {
        File[] files = new File(VarunaDumperCore.get().getDexDumpDir(), packageName).listFiles();
        return files != null && files.length > 0;
    }
}
