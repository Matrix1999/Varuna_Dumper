package top.niunaijun.varuna_dumper.fake.frameworks;

import android.content.Intent;
import android.os.RemoteException;

import top.niunaijun.varuna_dumper.VarunaDumperCore;
import top.niunaijun.varuna_dumper.core.system.ServiceManager;
import top.niunaijun.varuna_dumper.core.system.am.IBActivityManagerService;

/**
 * Created by Milk on 4/14/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class BActivityManager {
    private static final BActivityManager sActivityManager = new BActivityManager();
    private IBActivityManagerService mService;

    public static BActivityManager get() {
        return sActivityManager;
    }

    public void startActivity(Intent intent, int userId) {
        try {
            getService().startActivity(intent, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IBActivityManagerService getService() {
        if (mService != null && mService.asBinder().isBinderAlive()) {
            return mService;
        }
        mService = IBActivityManagerService.Stub.asInterface(VarunaDumperCore.get().getService(ServiceManager.ACTIVITY_MANAGER));
        return getService();
    }
}
