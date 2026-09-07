package top.niunaijun.varuna_dumper.utils;

import android.os.Process;

import java.util.Arrays;
import java.util.HashSet;

import top.niunaijun.varuna_dumper.app.BActivityThread;
import top.niunaijun.varuna_dumper.VarunaDumperCore;

public class MethodParameterUtils {


	public static String replaceFirstAppPkg(Object[] args) {
		if (args == null) {
			return null;
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof String) {
				String value = (String) args[i];
				if (VarunaDumperCore.get().isInstalled(value)) {
				    args[i] = VarunaDumperCore.getHostPkg();
					return value;
				}
			}
		}
		return null;
	}

	public static void replaceLastUserId(Object[] args){
		int index = ArrayUtils.indexOfLast(args, Integer.class);
		if (index != -1) {
			int uid = (int) args[index];
			if (uid == BActivityThread.getUid()) {
				args[index] = Process.myUid();
			}
		}
	}

	public static Class<?>[] getAllInterface(Class clazz){
		HashSet<Class<?>> classes = new HashSet<>();
		getAllInterfaces(clazz,classes);
		Class<?>[] result=new Class[classes.size()];
		classes.toArray(result);
		return result;
	}


	public static void getAllInterfaces(Class clazz, HashSet<Class<?>> interfaceCollection) {
		Class<?>[] classes = clazz.getInterfaces();
		if (classes.length != 0) {
			interfaceCollection.addAll(Arrays.asList(classes));
		}
		if (clazz.getSuperclass() != Object.class) {
			getAllInterfaces(clazz.getSuperclass(), interfaceCollection);
		}
	}
}
