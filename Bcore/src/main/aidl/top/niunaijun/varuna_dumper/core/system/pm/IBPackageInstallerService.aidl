// IBPackageInstallerService.aidl
package top.niunaijun.varuna_dumper.core.system.pm;

import top.niunaijun.varuna_dumper.core.system.pm.BPackageSettings;
import top.niunaijun.varuna_dumper.entity.pm.InstallOption;

// Declare any non-default types here with import statements

interface IBPackageInstallerService {
    int installPackageAsUser(in BPackageSettings file, int userId);
    int uninstallPackageAsUser(in BPackageSettings file, boolean removeApp, int userId);
    int updatePackage(in BPackageSettings file);
}
