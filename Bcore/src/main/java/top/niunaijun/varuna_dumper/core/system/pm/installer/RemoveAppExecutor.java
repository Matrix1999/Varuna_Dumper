package top.niunaijun.varuna_dumper.core.system.pm.installer;

import top.niunaijun.varuna_dumper.core.env.BEnvironment;
import top.niunaijun.varuna_dumper.entity.pm.InstallOption;
import top.niunaijun.varuna_dumper.core.system.pm.BPackageSettings;
import top.niunaijun.varuna_dumper.utils.FileUtils;

/**
 * Created by Milk on 4/27/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class RemoveAppExecutor implements Executor {
    @Override
    public int exec(BPackageSettings ps, InstallOption option, int userId) {
        FileUtils.deleteDir(BEnvironment.getAppDir(ps.pkg.packageName));
        return 0;
    }
}
