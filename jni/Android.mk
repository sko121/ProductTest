LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := eng
#LOCAL_CFLAGS = -std=gnu++0x
LOCAL_SRC_FILES:=               \
    disp_jni.cpp

LOCAL_C_INCLUDES:=      \
        $(TOP)/device/softwinner/common/hardware/include \
	$(TOP)/device/softwinner/fiber-common/hardware/include \
        $(TOP)/frameworks/base/include \
        $(JNI_H_INCLUDE)


LOCAL_SHARED_LIBRARIES:=liblog libdl libhardware#libutils libbinder libui libcutils   libdisphdmi 

ifeq ($(DLOPEN_LIBSECCAMERA),1)
LOCAL_SHARED_LIBRARIES+= libdl
endif
LOCAL_LDLIBS := -ldl -llog
LOCAL_PRELINK_MODULE := false

LOCAL_MODULE:= libdisp_jni
include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := eng
#LOCAL_CFLAGS = -std=gnu++0x
LOCAL_SRC_FILES:=               \
    block_jni.cpp BlockDevice.cpp md5.cpp

LOCAL_C_INCLUDES:=      \
        $(TOP)/device/softwinner/common/hardware/include \
	$(TOP)/device/softwinner/fiber-common/hardware/include \
        $(TOP)/frameworks/base/include \
        $(JNI_H_INCLUDE)


LOCAL_SHARED_LIBRARIES:=liblog libdl libhardware#libutils libbinder libui libcutils   libdisphdmi 

ifeq ($(DLOPEN_LIBSECCAMERA),1)
LOCAL_SHARED_LIBRARIES+= libdl
endif
LOCAL_LDLIBS := -ldl -llog
LOCAL_PRELINK_MODULE := false

LOCAL_MODULE:= libblock_jni
include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := eng
#LOCAL_CFLAGS = -std=gnu++0x
LOCAL_SRC_FILES:=               \
    audio_jni.cpp 

LOCAL_C_INCLUDES:=      \
        $(TOP)/device/softwinner/common/hardware/include \
	$(TOP)/device/softwinner/fiber-common/hardware/include \
        $(TOP)/frameworks/base/include \
        $(JNI_H_INCLUDE)


LOCAL_SHARED_LIBRARIES:=liblog libdl libhardware#libutils libbinder libui libcutils   libdisphdmi 

ifeq ($(DLOPEN_LIBSECCAMERA),1)
LOCAL_SHARED_LIBRARIES+= libdl
endif
LOCAL_LDLIBS := -ldl -llog
LOCAL_PRELINK_MODULE := false

LOCAL_MODULE:= libaudio_jni
include $(BUILD_SHARED_LIBRARY)

