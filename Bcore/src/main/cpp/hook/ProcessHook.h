//
// Created by Milk on 2021/6/6.
//

#ifndef VARUNA_DUMPER_PROCESSHOOK_H
#define VARUNA_DUMPER_PROCESSHOOK_H
#include "BaseHook.h"

class ProcessHook : public BaseHook {
public:
    static void init(JNIEnv *env);
};


#endif //VARUNA_DUMPER_PROCESSHOOK_H
