#include <jni.h>
#include "basic_input.c"
#include <android/log.h>

#define  LOG_TAG    "DroidSynergy-JNI"
#define  ALOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

jint
Java_com_blacksoil_droidsynergy_input_SimpleInput_initializeNative
(JNIEnv *env, jobject this){
	// TODO: check if this returns -1
	int code = InitUinput();
	return code;
}


void
Java_com_blacksoil_droidsynergy_input_SimpleInput_relativeMouseMoveNative
(JNIEnv *env, jobject this, jint dx, jint dy){
	MouseXSyn(dx);
	MouseYSyn(dy);
}


void
Java_com_blacksoil_droidsynergy_input_SimpleInput_leftMouseDownNative
(JNIEnv *env, jobject this){
	MouseLeftDown();
}

void
Java_com_blacksoil_droidsynergy_input_SimpleInput_leftMouseUpNative
(JNIEnv *env, jobject this){
	MouseLeftUp();
}

void
Java_com_blacksoil_droidsynergy_input_SimpleInput_mouseWheelNative
(JNIEnv *env, jobject this, jint x, jint y){
	MouseWheel(x, y);
}

void
Java_com_blacksoil_droidsynergy_input_SimpleInput_mouseAbsNative
(JNIEnv *env, jobject this, jint x, jint y){
  ALOGD("Mouse abs=%d,%d\n", x, y);
	MouseAbs(x, y);
}
