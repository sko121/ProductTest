# This Android.mk is empty, and >> does not build subdirectories <<.
# Individual packages in experimental/ must be built directly with "mmm".

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := eng

LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_PACKAGE_NAME := ProductTest
LOCAL_CERTIFICATE := shared
LOCAL_JNI_SHARED_LIBRARIES := libdisp_jni libdisphdmi libblock_jni libaudio_jni

include $(BUILD_PACKAGE)

include $(LOCAL_PATH)/jni/Android.mk
include $(call all-makefiles-under,$(LOCAL_PATH))
