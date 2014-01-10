#include "dispHdmiResolution.h"

#include "../jni/disp_device.h"

namespace android {

DispHdmiResolution::DispHdmiResolution():
	mCheckInitOK(false),
	mDispHandle(0),
	mScreenWidth(0),
	mScreenHeight(0)
{
	mCheckInitOK = true;
}
DispHdmiResolution::~DispHdmiResolution()
{

}
DispHdmiResolution* DispHdmiResolution::createInstance()
{
	DispHdmiResolution * hardware = new DispHdmiResolution();
    	return (hardware->checkInit()) ? hardware : NULL;
}

status_t DispHdmiResolution::dispInit()
{
	status_t error = OK;
	// open disp driver
	mDispHandle = open("/dev/disp", O_RDWR);
	if (mDispHandle <= 0)
	{
		printf("open display driver failed, %s", strerror(errno));
		error = UNKNOWN_ERROR;
		goto ERROR;
	}
	printf("george: dispInit init mDispHandle\n");
	return 0;	
ERROR:
	return error;
}
status_t DispHdmiResolution::dispExit()
{
        // open disp driver
        if (mDispHandle > 0){
		close(mDispHandle);
		mDispHandle = 0;
        }
	printf("george: dispExit close mDispHandle\n");
        return 0;
}
status_t DispHdmiResolution::dispOpen()
{
	status_t error = OK;
	unsigned long buf[4] = {0,};
	if(mDispHandle <= 0){
                printf("disp driver haven't open");
		return -1;
        }
	printf("george: dispOpen\n");
	error = ioctl(mDispHandle, DISP_CMD_HDMI_ON, buf);
        return error;
}
status_t DispHdmiResolution::dispClose()
{
	status_t error = OK;
	unsigned long buf[4] = {0,};
	if(mDispHandle <= 0){
		printf("disp driver haven't open");
		return -1;
	}
	printf("george: dispClose\n");
	error = ioctl(mDispHandle, DISP_CMD_HDMI_OFF, buf);
        return error;
}
status_t DispHdmiResolution::setHdmiResolution(int solution)
{
	status_t err = OK;
	unsigned long buf[4] = {0,};
	buf[0] = 0;
	buf[1] = solution;
	printf("george: etHdmiResolutionn");
	err = ioctl(mDispHandle, DISP_CMD_HDMI_SET_MODE, buf);	
	return err; 
}
struct  disp_hdmi_device_t{
	struct disp_device_t disp_dev;
	DispHdmiResolution *hw;
};

static int close_disp(struct hw_device_t *dev)
{
	struct disp_hdmi_device_t *disp_hdmi_dev;
	if(!dev)
		return  -EINVAL;
	disp_hdmi_dev = (struct disp_hdmi_device_t *)dev;
	delete disp_hdmi_dev->hw;
	free(dev);
	printf("george: %s\n", __func__);
	return 0;
}
static int disp_cmd(struct disp_device_t *dev,int cmd, int arg){
	int err=-2;
	struct disp_hdmi_device_t *hdmidev;

	hdmidev=(struct disp_hdmi_device_t *)dev;
	if((NULL==hdmidev)||(NULL==hdmidev->hw))
		return -1;
	switch(cmd){	
		case DISP_CMD_INIT:
			err = hdmidev->hw->dispInit();
			break;
		case DISP_CMD_EXIT:
			err = hdmidev->hw->dispExit();
			break;
		case DISP_CMD_OPEN:
			err = hdmidev->hw->dispOpen();
			break;
		case DISP_CMD_CLOSE:
			err = hdmidev->hw->dispClose();
			break;
		case DISP_CMD_SET_RESOLUTION:
			err = hdmidev->hw->setHdmiResolution(arg);
			break;
		default:
			printf("disp_hdmi_cmd  invalid cmd\n");
			break;
	}
	return err;
}
static int load_disp_module(const struct hw_module_t* module, char const* name,
        struct hw_device_t** device)
{
	int err;
	int fd=-1;
	struct disp_hdmi_device_t *dev;
	
	dev = (struct disp_hdmi_device_t *)malloc(sizeof(struct disp_hdmi_device_t));
	printf("george: %s\n", __func__);
	memset(dev, 0, sizeof(struct disp_hdmi_device_t));  	
	dev->disp_dev.common.tag = HARDWARE_DEVICE_TAG;
	dev->disp_dev.common.version = 0;
	dev->disp_dev.common.module = (struct hw_module_t*)module;
	dev->disp_dev.common.close = (int (*)(struct hw_device_t*))close_disp;
	dev->disp_dev.disp_cmd=disp_cmd;	
	//dev->disp_dev.setDisplay=setDisplay;
	dev->hw=DispHdmiResolution::createInstance();
	if(dev->hw==NULL){
		goto error;
	}
	*device = (struct hw_device_t*)dev;
	return 0;
error:
	free(dev);

	return -EINVAL;
}
static hw_module_methods_t disp_module_methods = {
	open : load_disp_module,
};
extern "C" hw_module_t HAL_MODULE_INFO_SYM = {
    tag: HARDWARE_MODULE_TAG,
    version_major:  1,
    version_minor: 0,
    id:  DISP_HARDWARE_MODULE_ID,
    name:  "disp hdmi Module",
    author:  "George Bai.",
    methods: &disp_module_methods,
    dso : 0,
    reserved : { 0, },
};
}













