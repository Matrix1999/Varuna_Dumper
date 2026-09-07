package top.niunaijun.varuna_dumper.utils.compat;

import android.os.Build;

public class BuildCompat {

    public static int getPreviewSDKInt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return Build.VERSION.PREVIEW_SDK_INT;
            } catch (Throwable e) {
                // ignore
            }
        }
        return 0;
    }

    private static boolean isAtLeast(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel
                || (Build.VERSION.SDK_INT == apiLevel - 1 && getPreviewSDKInt() > 0);
    }

    // Android 16 / API 36.
    public static boolean isBaklava() {
        return isAtLeast(36);
    }

    // Android 15 / API 35.
    public static boolean isV() {
        return isAtLeast(35);
    }

    // Android 14 / API 34.
    public static boolean isU() {
        return isAtLeast(34);
    }

    // Android 13 / API 33.
    public static boolean isTiramisu() {
        return isAtLeast(33);
    }

    // 12
    public static boolean isS() {
        return isAtLeast(31);
    }

    // 11
    public static boolean isR() {
        return isAtLeast(30);
    }

    // 10
    public static boolean isQ() {
        return isAtLeast(29);
    }

    // 9
    public static boolean isPie() {
        return isAtLeast(Build.VERSION_CODES.P);
    }

    // 8
    public static boolean isOreo() {
        return isAtLeast(Build.VERSION_CODES.O);
    }

    // 7
    public static boolean isN() {
        return isAtLeast(Build.VERSION_CODES.N);
    }

    // 7.1
    public static boolean isN_MR1() {
        return isAtLeast(Build.VERSION_CODES.N_MR1);
    }

    // 6
    public static boolean isM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    // 5
    public static boolean isL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    // 5
    public static boolean isL_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean isSamsung() {
        return "samsung".equalsIgnoreCase(Build.BRAND) || "samsung".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isEMUI() {
        if (Build.DISPLAY.toUpperCase().startsWith("EMUI")) {
            return true;
        }
        String property = SystemPropertiesCompat.get("ro.build.version.emui");
        return property != null && property.contains("EmotionUI");
    }

    public static boolean isMIUI() {
        return SystemPropertiesCompat.getInt("ro.miui.ui.version.code", 0) > 0;
    }

    public static boolean isFlyme() {
        return Build.DISPLAY.toLowerCase().contains("flyme");
    }

    public static boolean isColorOS() {
        return SystemPropertiesCompat.isExist("ro.build.version.opporom")
                || SystemPropertiesCompat.isExist("ro.rom.different.version");
    }

    public static boolean is360UI() {
        String property = SystemPropertiesCompat.get("ro.build.uiversion");
        return property != null && property.toUpperCase().contains("360UI");
    }

    public static boolean isLetv() {
        return Build.MANUFACTURER.equalsIgnoreCase("Letv");
    }

    public static boolean isVivo() {
        return SystemPropertiesCompat.isExist("ro.vivo.os.build.display.id");
    }


    private static ROMType sRomType;

    public static ROMType getROMType() {
        if (sRomType == null) {
            if (isEMUI()) {
                sRomType = ROMType.EMUI;
            } else if (isMIUI()) {
                sRomType = ROMType.MIUI;
            } else if (isFlyme()) {
                sRomType = ROMType.FLYME;
            } else if (isColorOS()) {
                sRomType = ROMType.COLOR_OS;
            } else if (is360UI()) {
                sRomType = ROMType._360;
            } else if (isLetv()) {
                sRomType = ROMType.LETV;
            } else if (isVivo()) {
                sRomType = ROMType.VIVO;
            } else if (isSamsung()) {
                sRomType = ROMType.SAMSUNG;
            } else {
                sRomType = ROMType.OTHER;
            }
        }
        return sRomType;
    }

    public enum ROMType {
        EMUI,
        MIUI,
        FLYME,
        COLOR_OS,
        LETV,
        VIVO,
        _360,
        SAMSUNG,
        OTHER
    }
}