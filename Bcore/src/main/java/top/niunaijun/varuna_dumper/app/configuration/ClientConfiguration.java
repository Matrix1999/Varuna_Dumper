package top.niunaijun.varuna_dumper.app.configuration;

import java.io.File;

import top.niunaijun.varuna_dumper.VarunaDumperCore;
import top.niunaijun.varuna_dumper.utils.FileUtils;

/**
 * Created by Milk on 5/4/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public abstract class ClientConfiguration {
    private File mExternalDir;

    public final void init() {
        mExternalDir = VarunaDumperCore.getContext().getExternalCacheDir().getParentFile();
    }

    public abstract String getHostPackageName();

    public String getDexDumpDir() {
        File dump = new File(mExternalDir, "dump");
        FileUtils.mkdirs(dump);
        return dump.getAbsolutePath();
    }

    public boolean isFixCodeItem() {
        return false;
    }

    public boolean isEnableHookDump() {
        return true;
    }
}
