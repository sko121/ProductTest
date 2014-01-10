LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
#LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_TAGS := eng

LOCAL_SRC_FILES:=               \
	dispHdmiResolution.cpp
LOCAL_C_INCLUDES:=	\
    $(TARGET_HARDWARE_INCLUDE)	 \
    $(TOP)/device/softwinner/common/hardware/include \
    $(TOP)/device/softwinner/fiber-common/hardware/include \
    $(TOP)/hardware/libhardware/include \
    $(TOP)/frameworks/base/native/include       \
    system/core/include/BaseLib

LOCAL_MODULE_PATH := $(TARGET_OUT_SHARED_LIBRARIES)/hw

LOCAL_MODULE:= libdisphdmi

include $(BUILD_SHARED_LIBRARY)
