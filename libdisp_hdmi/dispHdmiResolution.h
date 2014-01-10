#ifndef __dispHdmiResolution_H_
#define __dispHdmiResolution_H_

#include <stdio.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/endian.h>
#include <drv_display.h>
#include <sys/mman.h> 
#include <fcntl.h> 
#include <utils/threads.h>
namespace android {
class DispHdmiResolution{
public:
	static DispHdmiResolution* createInstance();
	DispHdmiResolution();
	virtual	~DispHdmiResolution();
	int dispInit();
	int dispExit();
	status_t dispOpen();
	status_t dispClose();
	status_t setHdmiResolution(int);
private:
	bool checkInit(){
		return mCheckInitOK;
	}
	bool mCheckInitOK;
	int mDispHandle;
	int mScreenWidth;
	int mScreenHeight;
};
};
#endif //__dispHdmiResolution_H_
