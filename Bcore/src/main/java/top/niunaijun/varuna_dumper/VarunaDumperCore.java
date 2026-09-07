package top.niunaijun.varuna_dumper;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;

import top.niunaijun.varuna_dumper.app.configuration.ClientConfiguration;
import top.niunaijun.varuna_dumper.fake.delegate.ContentProviderDelegate;
import top.niunaijun.varuna_dumper.fake.frameworks.BDumpManager;
import top.niunaijun.varuna_dumper.proxy.ProxyManifest;
import top.niunaijun.varuna_dumper.app.configuration.AppLifecycleCallback;
import top.niunaijun.varuna_dumper.fake.hook.HookManager;
import top.niunaijun.varuna_dumper.entity.pm.InstallOption;
import top.niunaijun.varuna_dumper.entity.pm.InstallResult;
import top.niunaijun.varuna_dumper.core.system.DaemonService;
import top.niunaijun.varuna_dumper.utils.FileUtils;
import top.niunaijun.varuna_dumper.utils.ShellUtils;
import top.niunaijun.varuna_dumper.utils.compat.BuildCompat;
import top.niunaijun.varuna_dumper.utils.compat.BundleCompat;
import top.niunaijun.varuna_dumper.utils.provider.ProviderCall;
import top.niunaijun.varuna_dumper.fake.frameworks.BActivityManager;
import top.niunaijun.varuna_dumper.fake.frameworks.BPackageManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.weishu.reflection.Reflection;
import reflection.android.app.ActivityThread;
import top.niunaijun.varuna_dumper.fake.frameworks.BStorageManager;
import top.niunaijun.varuna_dumper.core.system.ServiceManager;

/**
 * Created by Milk on 3/30/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
@SuppressLint("StaticFieldLeak")
public class VarunaDumperCore extends ClientConfiguration {
    public static final String TAG = "VarunaDumperCore";
    public static final int USER_ID = 0;

    private static final VarunaDumperCore sVarunaDumperCore = new VarunaDumperCore();
    private static Context sContext;
    private ProcessType mProcessType;
    private final Map<String, IBinder> mServices = new HashMap<>();
    private ClientConfiguration mClientConfiguration;
    private AppLifecycleCallback mAppLifecycleCallback = AppLifecycleCallback.EMPTY;

    public static VarunaDumperCore get() {
        return sVarunaDumperCore;
    }

    public static PackageManager getPackageManager() {
        return sContext.getPackageManager();
    }

    public static String getHostPkg() {
        return get().getHostPackageName();
    }

    public static Context getContext() {
        return sContext;
    }

    public void doAttachBaseContext(Context context, ClientConfiguration clientConfiguration) {
        if (clientConfiguration == null) {
            throw new IllegalArgumentException("ClientConfiguration is null!");
        }
        Reflection.unseal(context);
        sContext = context;
        mClientConfiguration = clientConfiguration;
        mClientConfiguration.init();
        String processName = getProcessName(getContext());
        if (processName.equals(VarunaDumperCore.getHostPkg())) {
            mProcessType = ProcessType.Main;
            startLogcat();
        } else if (processName.endsWith(getContext().getString(R.string.varuna_dumper_service_name))) {
            mProcessType = ProcessType.Server;
        } else {
            mProcessType = ProcessType.BAppClient;
        }
        if (VarunaDumperCore.get().isVirtualProcess()) {
            if (processName.endsWith("p0")) {
//                android.os.Debug.waitForDebugger();
            }
//            android.os.Debug.waitForDebugger();
        }
        if (isServerProcess()) {
//            Intent intent = new Intent();
//            intent.setClass(getContext(), DaemonService.class);
//            if (BuildCompat.isOreo()) {
//                getContext().startForegroundService(intent);
//            } else {
//                getContext().startService(intent);
//            }
        }
        HookManager.get().init();
    }

    public void doCreate() {
        if (isVirtualProcess()) {
            ContentProviderDelegate.init();
        }
        if (!isServerProcess()) {
            initService();
        }
    }

    private void initService() {
        get().getService(ServiceManager.ACTIVITY_MANAGER);
        get().getService(ServiceManager.PACKAGE_MANAGER);
        get().getService(ServiceManager.STORAGE_MANAGER);
        get().getService(ServiceManager.DUMP_MANAGER);
    }

    public static Object mainThread() {
        return ActivityThread.currentActivityThread.call();
    }

    public void startActivity(Intent intent, int userId) {
        getBActivityManager().startActivity(intent, userId);
    }

    public static BPackageManager getBPackageManager() {
        return BPackageManager.get();
    }

    public static BActivityManager getBActivityManager() {
        return BActivityManager.get();
    }

    public static BStorageManager getBStorageManager() {
        return BStorageManager.get();
    }

    public static BDumpManager getBDumpManager() {
        return BDumpManager.get();
    }

    public boolean launchApk(String packageName) {
        Intent launchIntentForPackage = getBPackageManager().getLaunchIntentForPackage(packageName, USER_ID);
        if (launchIntentForPackage == null) {
            return false;
        }
        startActivity(launchIntentForPackage, USER_ID);
        return true;
    }

    public boolean isInstalled(String packageName) {
        return getBPackageManager().isInstalled(packageName, USER_ID);
    }

    public void uninstallPackage(String packageName) {
        getBPackageManager().uninstallPackageAsUser(packageName, USER_ID);
    }

    public InstallResult installPackage(String packageName) {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0);
            return getBPackageManager().installPackageAsUser(packageInfo.applicationInfo.sourceDir, InstallOption.installBySystem(), USER_ID);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return new InstallResult().installError(e.getMessage());
        }
    }

    public InstallResult installPackage(File apk) {
        return getBPackageManager().installPackageAsUser(apk.getAbsolutePath(), InstallOption.installByStorage(), USER_ID);
    }

    public InstallResult installPackage(Uri apk) {
        return getBPackageManager().installPackageAsUser(apk.toString(), InstallOption.installByStorage().makeUriFile(), USER_ID);
    }

    public AppLifecycleCallback getAppLifecycleCallback() {
        return mAppLifecycleCallback;
    }

    public void setAppLifecycleCallback(AppLifecycleCallback appLifecycleCallback) {
        if (appLifecycleCallback == null) {
            throw new IllegalArgumentException("AppLifecycleCallback is null!");
        }
        mAppLifecycleCallback = appLifecycleCallback;
    }

    public IBinder getService(String name) {
        IBinder binder = mServices.get(name);
        if (binder != null && binder.isBinderAlive()) {
            return binder;
        }
        Bundle bundle = new Bundle();
        bundle.putString("_VM_|_server_name_", name);
        Bundle vm = ProviderCall.callSafely(ProxyManifest.getBindProvider(), "VM", null, bundle);
        assert vm != null;
        binder = BundleCompat.getBinder(vm, "_VM_|_server_");
        mServices.put(name, binder);
        return binder;
    }

    private enum ProcessType {
        Server,
        BAppClient,
        Main,
    }

    public boolean isVirtualProcess() {
        return mProcessType == ProcessType.BAppClient;
    }

    public boolean isMainProcess() {
        return mProcessType == ProcessType.Main;
    }

    public boolean isServerProcess() {
        return mProcessType == ProcessType.Server;
    }

    @Override
    public String getHostPackageName() {
        return mClientConfiguration.getHostPackageName();
    }

    @Override
    public String getDexDumpDir() {
        return mClientConfiguration.getDexDumpDir();
    }

    @Override
    public boolean isFixCodeItem() {
        return mClientConfiguration.isFixCodeItem();
    }

    @Override
    public boolean isEnableHookDump() {
        return mClientConfiguration.isEnableHookDump();
    }

    private void startLogcat() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getContext().getPackageName() + "_logcat.txt");
        FileUtils.deleteDir(file);
        ShellUtils.execCommand("logcat -c", false);
        ShellUtils.execCommand("logcat >> " + file.getAbsolutePath() + " &", false);
    }

    private static String getProcessName(Context context) {
        int pid = Process.myPid();
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            if (info.pid == pid) {
                processName = info.processName;
                break;
            }
        }
        if (processName == null) {
            throw new RuntimeException("processName = null");
        }
        return processName;
    }

    public static boolean is64Bit() {
        if (BuildCompat.isM()) {
            return Process.is64Bit();
        }
        String[] supportedAbis = Build.SUPPORTED_ABIS;
        if (supportedAbis != null) {
            for (String abi : supportedAbis) {
                if ("arm64-v8a".equals(abi)) {
                    return true;
                }
            }
        }
        return "arm64-v8a".equals(Build.CPU_ABI);
    }
}
