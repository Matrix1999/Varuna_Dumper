package top.niunaijun.varuna_dumper.core.system;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import top.niunaijun.varuna_dumper.core.env.BEnvironment;
import top.niunaijun.varuna_dumper.VarunaDumperCore;
import top.niunaijun.varuna_dumper.core.env.AppSystemEnv;
import top.niunaijun.varuna_dumper.entity.pm.InstallOption;
import top.niunaijun.varuna_dumper.core.system.am.BActivityManagerService;
import top.niunaijun.varuna_dumper.core.system.os.BStorageManagerService;
import top.niunaijun.varuna_dumper.core.system.pm.BPackageInstallerService;
import top.niunaijun.varuna_dumper.core.system.pm.BPackageManagerService;
import top.niunaijun.varuna_dumper.core.system.user.BUserHandle;
import top.niunaijun.varuna_dumper.core.system.user.BUserManagerService;
import top.niunaijun.varuna_dumper.utils.FileUtils;

import static top.niunaijun.varuna_dumper.core.env.BEnvironment.EMPTY_JAR;
import static top.niunaijun.varuna_dumper.core.env.BEnvironment.JUNIT_JAR;
import static top.niunaijun.varuna_dumper.core.env.BEnvironment.VM_JAR;

/**
 * Created by Milk on 4/22/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class VarunaDumperSystem {
    private static VarunaDumperSystem sVarunaDumperSystem;

    public static VarunaDumperSystem getSystem() {
        if (sVarunaDumperSystem == null) {
            synchronized (VarunaDumperSystem.class) {
                if (sVarunaDumperSystem == null) {
                    sVarunaDumperSystem = new VarunaDumperSystem();
                }
            }
        }
        return sVarunaDumperSystem;
    }

    public void startup() {
        BEnvironment.load();

        BPackageManagerService.get().systemReady();
        BUserManagerService.get().systemReady();
        BActivityManagerService.get().systemReady();
        BStorageManagerService.get().systemReady();
        BPackageInstallerService.get().systemReady();

        List<String> preInstallPackages = AppSystemEnv.getPreInstallPackages();
        for (String preInstallPackage : preInstallPackages) {
            try {
                if (!BPackageManagerService.get().isInstalled(preInstallPackage, BUserHandle.USER_ALL)) {
                    PackageInfo packageInfo = VarunaDumperCore.getPackageManager().getPackageInfo(preInstallPackage, 0);
                    BPackageManagerService.get().installPackageAsUser(packageInfo.applicationInfo.sourceDir, InstallOption.installBySystem(), BUserHandle.USER_ALL);
                }
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        initJarEnv();
    }

    private void initJarEnv() {
        try {
            InputStream junit = VarunaDumperCore.getContext().getAssets().open("junit.jar");
            FileUtils.copyFile(junit, JUNIT_JAR);

            InputStream empty = VarunaDumperCore.getContext().getAssets().open("empty.jar");
            FileUtils.copyFile(empty, EMPTY_JAR);

            InputStream vm = VarunaDumperCore.getContext().getAssets().open("vm.jar");
            FileUtils.copyFile(vm, VM_JAR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
