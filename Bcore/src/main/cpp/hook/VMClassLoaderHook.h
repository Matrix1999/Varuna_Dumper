//
// Created by Milk on 2021/5/5.
//

#ifndef VARUNA_DUMPER_VMCLASSLOADERHOOK_H
#define VARUNA_DUMPER_VMCLASSLOADERHOOK_H


#include "BaseHook.h"
#include <jni.h>

class VMClassLoaderHook : public BaseHook {
public:
    static void hideXposed();
    static void init(JNIEnv *env);
};


#endif //VARUNA_DUMPER_VMCLASSLOADERHOOK_H
