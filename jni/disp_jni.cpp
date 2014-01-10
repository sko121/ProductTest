#include <jni.h>
#include <utils/Log.h>
#include <utils/misc.h>
#include <utils/threads.h>
#include <dlfcn.h>
#include "disp_device.h"

#define	LOCAL_LIBRARY_PATH	"/data/data/com.test/lib"
#define	LOCAL_LIBRARY_FULL_NAME		LOCAL_LIBRARY_PATH"/libdisphdmi.so"

#define F_LOG ALOGI("%s, line: %d", __FUNCTION__, __LINE__)
#define	DEBUG_INFO(fmt,args...) ALOGD("%s(%d) "fmt"", __FUNCTION__, __LINE__,##args);

using namespace android;

static int disp_exit();
struct disp_handle{
	const struct hw_module_t  *hdmi;
	struct disp_device_t *dev;
	int open_count;
	pthread_mutex_t lock;	
	pthread_attr_t attr;
	disp_handle():
		hdmi(NULL),dev(NULL),open_count(0){
		lock=PTHREAD_MUTEX_INITIALIZER;	
	};
	~disp_handle(){
		//F_LOG;
		disp_exit();
	};
};
static struct disp_handle g_disp_handle ;

static int disp_exit(){
	int err=-1;
	struct disp_device_t *dev;
	struct disp_handle *handle=&g_disp_handle;	
	pthread_mutex_lock(&handle->lock);
	dev=handle->dev;
	handle->open_count--;
	DEBUG_INFO("open_count = %d",handle->open_count);
	if(handle->open_count==0){		
		if(dev){
			dev->disp_cmd(dev, DISP_CMD_EXIT, 0);
			err=dev->common.close((struct hw_device_t *)dev);
		}
		handle->dev=NULL;
		handle->hdmi=NULL;
	}
	err=0;
	pthread_mutex_unlock(&handle->lock);
	return err;	
}
static int load_local_module(const char *id,const struct hw_module_t **pHmi){
	int err=0;
	int status = 0;
	void *handle;
	struct hw_module_t *hmi=NULL;
	const char *sym = HAL_MODULE_INFO_SYM_AS_STR;
	char path[PATH_MAX];

	handle = dlopen(id, RTLD_NOW);
	if (handle == NULL) {
		char const *err_str = dlerror();
		ALOGE("load: module=%s\n", err_str?err_str:"unknown");
		status = -EINVAL;
		goto done;
	}

	/* Get the address of the struct hal_module_info. */
	hmi = (struct hw_module_t *)dlsym(handle, sym);
	if (hmi == NULL) {
		ALOGE("load: couldn't find symbol %s", sym);
		status = -EINVAL;
		goto done;
	}
	hmi->dso = handle;
done:
	if(status!=0){
		hmi=NULL;
		if(handle!=NULL){
			dlclose(handle);			 
			handle = NULL;
		}
	}
	DEBUG_INFO("status:%d",status);
	*pHmi = hmi;
	return status;
}

static jint initDisp_native(JNIEnv * env,jobject jobj)
{
	int err=-1;
	const struct hw_module_t  *hdmi;
	struct disp_device_t *dev;
	struct disp_handle *handle=&g_disp_handle;

	pthread_mutex_lock(&handle->lock);	
	DEBUG_INFO("handle->open_count:%d",handle->open_count);
	if(handle->open_count>0){
		err=0;
		goto err_exit;
	}
	handle->open_count++;
	DEBUG_INFO();
	if(!handle->hdmi){
		err=load_local_module(LOCAL_LIBRARY_FULL_NAME,&hdmi);
		if(err){
			err=hw_get_module_by_class(DISP_HARDWARE_MODULE_ID,NULL,&hdmi);
		}
		if(err!=0){
			DEBUG_INFO("disp_open module failed");
			handle->open_count--;
			goto err_exit;
		}
		handle->hdmi=hdmi;
	}
	DEBUG_INFO();
	hdmi=handle->hdmi;
	if(!handle->dev){
		err=hdmi->methods->open(hdmi,DISP_HARDWARE_MODULE_ID,(struct hw_device_t **)&dev);
		if(err!=0){
			DEBUG_INFO("disp_open dev failed");
			dlclose(hdmi->dso);
			handle->hdmi=NULL;		
			handle->open_count--;
			goto err_exit;
		}
		handle->dev=dev;
	}	
	DEBUG_INFO("disp_open dev success");
	err=dev->disp_cmd(dev,DISP_CMD_INIT, 0);
err_exit:
	pthread_mutex_unlock(&handle->lock);
	return err;
	return 0;
}
static jint startDisp_native(JNIEnv * env,jobject jobj)
{
	int err=-1;
	struct disp_device_t *dev;
	struct disp_handle *handle=&g_disp_handle;	
	F_LOG;
	pthread_mutex_lock(&handle->lock);
	DEBUG_INFO();
	dev=handle->dev;
	if(!dev){
		ALOGE("TVD dones't init");
		goto err_exit;
	}	
	err=dev->disp_cmd(dev,DISP_CMD_OPEN, 0);
	DEBUG_INFO();
err_exit:
	pthread_mutex_unlock(&handle->lock);
	return err;	
}
static jint  closeDisp_native(JNIEnv * env,jobject jobj)
{
	int err=-1;	
	struct disp_device_t *dev;
	struct disp_handle *handle=&g_disp_handle;	
	pthread_mutex_lock(&handle->lock);
	dev=handle->dev;	
	DEBUG_INFO("open_count = %d",handle->open_count);		
	if(dev){
		err = dev->disp_cmd(dev, DISP_CMD_CLOSE, 0);			
	}	
	pthread_mutex_unlock(&handle->lock);
	
	return err;
}

static jint setHdmiResolution_native(JNIEnv * env,jobject jobj,jint resolution)
{
	int err=-1;
	struct disp_device_t *dev;
	struct disp_handle *handle=&g_disp_handle;	
	F_LOG;
	pthread_mutex_lock(&handle->lock);
	dev=handle->dev;
	if(!dev){
		ALOGE("DISP dones't init");
		goto err_exit;
	}		
	err=dev->disp_cmd(dev,DISP_CMD_SET_RESOLUTION, resolution);	
err_exit:
	pthread_mutex_unlock(&handle->lock);
	return err;
}

static JNINativeMethod method_table[] = {
        { "native_initDisp", "()I", (void*)initDisp_native },
	{ "native_startDisp", "()I", (void*)startDisp_native },
        { "native_closeDisp", "()I", (void*)closeDisp_native },
        { "native_setHdmiResolution", "(I)I", (void*)setHdmiResolution_native },
};

static int register_com_softwinner_adversiting(JNIEnv *env)
{
        jclass clazz;
        //F_LOG;
        if ((clazz = env->FindClass("com/test/Hdmi")) == NULL ){

                printf("register_com_test failed.");
                return -1;
        }
        //F_LOG;
        return env->RegisterNatives(clazz, method_table,sizeof(method_table)/sizeof(method_table[0]));
}

extern "C"  jint JNI_OnLoad(JavaVM * vm, void * reserved){
        JNIEnv *env = NULL;

        //printf("JNI_OnLoad");
        if ((vm->GetEnv((void **)&env, JNI_VERSION_1_4) != JNI_OK) ||
        (register_com_softwinner_adversiting(env) < 0) ) {

                F_LOG;
                return -1;
        }
        F_LOG;
        return JNI_VERSION_1_4;
}

