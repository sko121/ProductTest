#include <jni.h>
#include <utils/Log.h>
#include <utils/misc.h>
#include <utils/threads.h>
#include <dlfcn.h>
#include "BlockDevice.h"

#define F_LOG ALOGI("%s, line: %d", __FUNCTION__, __LINE__)
#define	DEBUG_INFO(fmt,args...) ALOGD("%s(%d) "fmt"", __FUNCTION__, __LINE__,##args);

using namespace android;

static int block_exit();
struct block_handle{
	struct BlockDevice *dev;
	int open_count;
	pthread_mutex_t lock;	
	block_handle():
		dev(NULL),open_count(0){
		lock=PTHREAD_MUTEX_INITIALIZER;	
	};
	~block_handle(){
		//F_LOG;
		block_exit();
	};
};

static struct block_handle g_block_handle;

static int block_exit(){
	int err=-1;
	struct BlockDevice *dev;
	struct block_handle *handle=&g_block_handle;	
	pthread_mutex_lock(&handle->lock);
	dev=handle->dev;
	handle->open_count--;
	DEBUG_INFO("open_count = %d",handle->open_count);
	if(handle->open_count==0){		
		if(dev){
			delete(dev);
		}
		handle->dev=NULL;
	}
	err=0;
	pthread_mutex_unlock(&handle->lock);
	return err;	
}

static jint initBlock_native(JNIEnv * env,jobject jobj)
{
	int err=-1;
	struct BlockDevice *dev;
	struct block_handle *handle=&g_block_handle;	

	pthread_mutex_lock(&handle->lock);	
	DEBUG_INFO("handle->open_count:%d",handle->open_count);
	if(handle->open_count>0){
		err=0;
		goto err_exit;
	}
	handle->open_count++;
	dev = new BlockDevice();	
	if(dev == NULL){
		DEBUG_INFO("block_open dev failed");
		handle->open_count--;
		return -1;
	}
	handle->dev = dev;
	DEBUG_INFO("block_open dev success");
err_exit:
	pthread_mutex_unlock(&handle->lock);
	return err;
}
#define MAXLINE 128
static jint getMemoryInfo_native(JNIEnv * env,jobject jobj)
{
	int result;
    FILE * memfile = NULL;
    char arr[MAXLINE+1];
    memfile = fopen("/proc/meminfo", "r");
    if(memfile == NULL){
        ALOGD("read /proc/meminfo err %d\n", errno);
        return -1;
    }
    while ((fgets(arr, MAXLINE, memfile)) != NULL){
        if(strstr(arr, "MemTotal")){
			sscanf(arr, "%*[^0-9]%d", &result);
			ALOGD("Total Memory: %d\n", result);
			break;
		}
    }
	fclose(memfile);
	return result / 1024;
}
static jint getNandflashInfo_native(JNIEnv * env,jobject jobj)
{
	int result = 0, temp, i = 0;
	FILE *diskfile = NULL;
	char arr[MAXLINE+1];
	char obj[24] = {0,};
	char * p, *pp;
    diskfile = fopen("/proc/partitions", "r");
    if(diskfile == NULL){
        ALOGD("read /proc/ err %d\n", errno);
        return -1;
    }
	while ((fgets(arr, MAXLINE, diskfile)) != NULL){
		if(i == 0 || i == 1){
			i++;
			continue;
		}
		if(i == 2){
			p = arr;
			while(*p != '\0'){
				if(*p <= 'z' && *p >= 'a')
					break;
				p++;
			}
			pp = obj;
			while(*p != '\0' && *p != 32){
				*pp++ = *p++;
			}
			*pp = '\0';
			ALOGD("obj:%s\n", obj);
			if(!strncmp(obj, "mmcblk0", 7)){
				sscanf(arr, "%*d%*d%d", &result);
				goto err;
			}else if(!strncmp(obj, "nand", 4))
				strcpy(obj, "nand");
			else
				strcpy(obj, "mmcblk0");
			i++;
		}
		if(strstr(arr, obj)){
			sscanf(arr, "%*d%*d%d", &temp);
			ALOGD("nand block: %d\n", temp);
			result += temp;
		}
	}
err:
	ALOGD("total block size: %d\n", result);
	fclose(diskfile);
	return result/1024;
/*	int err=-1;
	struct BlockDevice *dev;
	struct block_handle *handle=&g_block_handle;
	pthread_mutex_lock(&handle->lock);
	dev=handle->dev;
	if(!dev){
		ALOGE("DISP dones't init");
		goto err_exit;
	}
	err = dev->innerFlashSize;
	err = err / 1024;
err_exit:
	pthread_mutex_unlock(&handle->lock);
	return err;*/
}
static jint copyMemBlock_native(JNIEnv * env,jobject jobj, jint arg)
{
	int err=-1;
	struct BlockDevice *dev;
	struct block_handle *handle=&g_block_handle;	
	F_LOG;
	pthread_mutex_lock(&handle->lock);
	dev=handle->dev;
	if(!dev){
		ALOGE("DISP dones't init");
		goto err_exit;
	}		
	err = dev->CopyToMem();
err_exit:
	pthread_mutex_unlock(&handle->lock);
	return err;
}
static jint copyNandBlock_native(JNIEnv * env,jobject jobj, jint arg)
{
	int err=-1;
	struct BlockDevice *dev;
	struct block_handle *handle=&g_block_handle;	
	F_LOG;
	pthread_mutex_lock(&handle->lock);
	dev=handle->dev;
	if(!dev){
		ALOGE("DISP dones't init");
		goto err_exit;
	}		
	err = dev->CopyToNand();	
err_exit:
	pthread_mutex_unlock(&handle->lock);
	return err;
}
static JNINativeMethod method_table[] = {
    { "native_initBlock", "()I", (void*)initBlock_native },
	{ "native_copyMemBlock", "(I)I", (void*)copyMemBlock_native},
	{ "native_getMemoryInfo", "()I", (void*)getMemoryInfo_native},
	{ "native_copyNandBlock", "(I)I", (void*)copyNandBlock_native},
	{ "native_getNandflashInfo", "()I", (void*)getNandflashInfo_native},
};

static int register_com_softwinner_adversiting(JNIEnv *env)
{
        jclass clazz;
        //F_LOG;
        if ((clazz = env->FindClass("com/test/Block")) == NULL ){
                printf("register_com_test failed.");
                return -1;
        }
        return env->RegisterNatives(clazz, method_table,sizeof(method_table)/sizeof(method_table[0]));
}

extern "C"  jint JNI_OnLoad(JavaVM * vm, void * reserved){
        JNIEnv *env = NULL;
        if ((vm->GetEnv((void **)&env, JNI_VERSION_1_4) != JNI_OK) ||
        (register_com_softwinner_adversiting(env) < 0) ) {

                F_LOG;
                return -1;
        }
        F_LOG;
        return JNI_VERSION_1_4;
}

