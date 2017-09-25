#include <jni.h>
#include <utils/Log.h>
#include <sys/types.h>
#include <errno.h>

static jint getEarplugHead_native(JNIEnv * env,jobject jobj)
{
	int ret = -1;
	FILE *stateFile = NULL;
	char arr[36];
	stateFile = fopen("/sys/class/switch/h2w/state", "r");
    if(stateFile == NULL){
        ALOGD("read /proc/partitions err %d\n", errno);
        return ret;
    }
	if((fgets(arr, 35, stateFile)) != NULL){
		sscanf(arr, "%d", &ret);
		ALOGD("ret: %d\n", ret);
	}
	fclose(stateFile);
	return ret;
}
static JNINativeMethod method_table[] = {
	{ "native_getEarplugHead", "()I", (void*)getEarplugHead_native},
};

static int register_com_softwinner_adversiting(JNIEnv *env)
{
    jclass clazz;
    if ((clazz = env->FindClass("com/thtfit/test/EarplugActivity")) == NULL ){
        printf("register_com_thtfit_test failed.");
        return -1;
    }
    return env->RegisterNatives(clazz, method_table,sizeof(method_table)/sizeof(method_table[0]));
}

extern "C"  jint JNI_OnLoad(JavaVM * vm, void * reserved){
        JNIEnv *env = NULL;
        if ((vm->GetEnv((void **)&env, JNI_VERSION_1_4) != JNI_OK) ||
        (register_com_softwinner_adversiting(env) < 0) ) {
                return -1;
        }
        return JNI_VERSION_1_4;
}

