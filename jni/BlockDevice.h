#ifndef __BlockDevice_H_
#define __BlockDevice_H_
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/endian.h>
#include <utils/threads.h>
#include <hardware/hardware.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/statfs.h>
#include <mntent.h>
#include <fcntl.h>
#include <errno.h>

#define BLOCKSIZE 20*1024*1024

#define NANDPATH "/mnt/sdcard/"
#define SDPATH "/mnt/extsd/"
#define FILENAME "TestFile"

namespace android{

struct Block{
	void * buf;
	char md5val[32];
	int Size;
};

struct NonvolatileBlock{
	char Path[128];
	int fd;
	FILE * file;
	char md5val[32];
	unsigned int availSize;
	int Size;
};

class BlockDevice{
public:
	BlockDevice();
	virtual ~BlockDevice();		
	int innerFlashSize;
	int memorySize;
	
	int getMemoryInfo();
	int CopyToMem();
	int getNandflashInfo();
	int CopyToNand();
private:
	int InitBlock();
	int CompareMd5(char src[32], char dst[32]);
	int CopyToDisk(struct NonvolatileBlock * nblock);
	int getFreeMemory();
	int getBlockSize();
private:
	struct Block mBlock;
	struct NonvolatileBlock nBlock;
	unsigned int mBlockSize;	//bytes
	unsigned int mMemFree;		//kb
};
};
#endif //__BlockDevice_H_

