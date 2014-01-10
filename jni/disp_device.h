#ifndef __DISP_DEVICE_H_
#define __DISP_DEVICE_H_

#include <sys/types.h>
#include <hardware/hardware.h>

#define DISP_HARDWARE_MODULE_ID "disp"
namespace android {
struct disp_device_t {
        struct hw_device_t common;
        void *priv;
        int (*disp_cmd)(struct disp_device_t  *dev,int cmd, int arg);
        //int (*setDisplay)(struct disp_device_t  *dev, const sp<ANativeWindow>& window );
};

enum{
        DISP_CMD_INIT=0x000,
        DISP_CMD_OPEN=0x100,
        DISP_CMD_CLOSE=0x101,
        DISP_CMD_SET_RESOLUTION=0x102,
        DISP_CMD_SET_CHANNEL=0x106,
        DISP_CMD_EXIT=0x300,
};
};
#endif//__DISP_DEVICE_H_
