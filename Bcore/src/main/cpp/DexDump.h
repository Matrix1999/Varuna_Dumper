//
// Created by Milk on 2021/5/16.
//

#ifndef VARUNA_DUMPER_DEXDUMP_H
#define VARUNA_DUMPER_DEXDUMP_H
#include <jni.h>

#define HOOK_FUN(ret, func, ...) \
  ret (*orig_##func)(__VA_ARGS__); \
  ret new_##func(__VA_ARGS__)

class DexDump {
public:
    static void hookDumpDex(JNIEnv *env, jstring dir);
    static void cookieDumpDex(JNIEnv *env, jlong cookie, jstring dir, jboolean fix);
};


#endif //VARUNA_DUMPER_DEXDUMP_H
