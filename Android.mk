# This Android.mk is empty, and >> does not build subdirectories <<.
# Individual packages in experimental/ must be built directly with "mmm".

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_STATIC_JAVA_LIBRARIES := zxing

#LOCAL_MODULE_TAGS := eng
LOCAL_MODULE_TAGS := user

LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_PACKAGE_NAME := ProductTest
LOCAL_CERTIFICATE := shared
LOCAL_JNI_SHARED_LIBRARIES := libdisp_jni  libblock_jni libthtfit_audio_jni


include $(BUILD_PACKAGE)


include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := zxing:libs/core.jar
include $(BUILD_MULTI_PREBUILT)


include $(CLEAR_VARS)
include $(LOCAL_PATH)/jni/Android.mk
include $(call all-makefiles-under,$(LOCAL_PATH)/*)

