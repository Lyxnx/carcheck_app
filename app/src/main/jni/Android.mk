LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := carcheck
LOCAL_SRC_FILES := carcheck.c

include $(BUILD_SHARED_LIBRARY)