#include <jni.h>
#include "basic_input.c"
#include <android/log.h>

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

