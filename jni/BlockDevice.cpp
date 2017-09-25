#include "BlockDevice.h"
#include <utils/Log.h>
#include <sys/types.h>
#include <unistd.h>
#include "md5.h"

using namespace android;
using namespace std;

BlockDevice::BlockDevice():
	mBlockSize(0),
	mMemFree(0)
{
	memset(&mBlock, 0, sizeof(struct Block));
	memset(&nBlock, 0, sizeof(struct NonvolatileBlock));
}

BlockDevice::~BlockDevice()
{
	if(mBlock.buf){
		free(mBlock.buf);
		mBlock.buf= NULL;
	}
	if(nBlock.fd > 0){
		remove(nBlock.Path);
		close(nBlock.fd);
		nBlock.fd = -1;
	}
}

int BlockDevice::InitBlock()
{
	int ret;
	int length = mBlock.Size;
	int *temp = (int *)mBlock.buf;
	/*check if block has init*/
	if(NULL != mBlock.buf){
		ALOGD("block buffer have init\n");
		return 0;
	}
	/*malloc memory block*/
	mBlockSize = BLOCKSIZE;
	mBlock.buf = malloc(mBlockSize);
	if(NULL == mBlock.buf){
		ALOGD("no mem request\n");
		return -ENOMEM;
	}
	mBlock.Size = mBlockSize;
	/*init memory block*/
	srand(time(NULL));
	while(length > 0){
		*temp++ = rand();
		length -= 4;	
	}
	/*get memory block md5 value*/
	MD5 md5(mBlock.buf, mBlock.Size);
	md5.toString((char *)mBlock.md5val);
	ALOGD("src mem md5: %s\n", mBlock.md5val);
	return 0;
}

int BlockDevice::CompareMd5(char src[32], char dst[32])
{
	int i = 0;
	while(i < 32 && src[i] == dst[i]){
		i++;
	}
	ALOGD("final: %d\n", i);
	return i == 32 ? 0 : -1;
}

int BlockDevice::CopyToMem()
{
	int ret;
	struct Block block;
		
	ALOGD("%s\n", __func__);	
	/*init src memory block*/
	ret = InitBlock();
	if(ret < 0){
		ALOGE("init Block failed");
		return ret;
	}
	/*init dst block struct*/
	memset(&block, 0, sizeof(struct Block));
	if(NULL == block.buf){
		block.buf = malloc(mBlockSize);
		if(NULL == block.buf){
			printf("no mem request\n");
			return -ENOMEM;
		}
		block.Size = mBlockSize;
	}
	/*cp to dst block data*/
	memcpy(block.buf, mBlock.buf, block.Size);
	/*get dst block md5 value*/
	MD5 md5(block.buf, block.Size);
	md5.toString((char *)block.md5val);
	ALOGD("dst mem md5: %s\n", block.md5val);
	/*free dst block memory*/	
	free(block.buf);
	block.buf = NULL;
	/*compare memory md5 value and disk md5 value*/
	return CompareMd5(block.md5val, mBlock.md5val);	
}
/*
bool BlockDevice::CopyToDisk(struct NonvolatileBlock * nblock)
{
	InitBlock();

	nblock->fd = fopen(nblock->Path, "w+");
	if(nblock->fd == NULL){
		ALOGD("open file err\n");
		return -ENOMEM;
	}

	int size = fwrite(mBlock.buf, mBlock.Size, 1, nblock->fd);
	fflush(nblock->fd);
	ALOGD("write size: %d\n", size);
	
	MD5 md5(nblock->fd);
	md5.toString((char *)nblock->md5val);
	
	ALOGD("md5: %s\n", nblock->md5val);
	//free(nblock->buf);
	//nblock->buf = NULL;
	return CompareMd5(nblock->md5val, mBlock.md5val);	
}*/
int BlockDevice::CopyToDisk(struct NonvolatileBlock * nblock)
{
	int ret;
	/*init src memory block*/
	ret = InitBlock();
	if(ret < 0){
		ALOGD("Init Block failed\n");
		return -ENOMEM;
	}
	/*open disk file*/
	nblock->fd = open(nblock->Path, O_CREAT|O_RDWR|O_TRUNC, S_IRWXG|S_IRWXO);
	if(nblock->fd < 0){
		ALOGE("open err: %s\n", strerror(errno));
		return -EACCES;
	}
	/*write memory block data to disk*/
	int size = write(nblock->fd, mBlock.buf, mBlock.Size);
	if(size != mBlock.Size){
		ALOGE("write to disk err: %d %s\n", errno, strerror(errno));
		return -ENOSPC;
	}
	ALOGD("write size: %d\n", size);
	/*get disk block data md5 value*/
	MD5 md5(nblock->fd);
	md5.toString((char *)nblock->md5val);
	ALOGD("dst nand md5: %s\n", nblock->md5val);
	/*compare memory md5 value and disk md5 value*/
	return CompareMd5(nblock->md5val, mBlock.md5val);	
}
int getDiskFreeSpace(struct NonvolatileBlock * block)
{
	struct statfs diskInfo;
	
	statfs(block->Path, &diskInfo);
	unsigned long long blocksize = diskInfo.f_bsize;
	unsigned long long totalsize = blocksize * diskInfo.f_blocks; 
	ALOGD("Total_size = %llu B = %llu KB = %llu MB = %llu GB\n", 
		totalsize, totalsize>>10, totalsize>>20, totalsize>>30);
	
	unsigned long long freeDisk = diskInfo.f_bfree * blocksize;
	unsigned long long availableDisk = diskInfo.f_bavail * blocksize;
	ALOGD("Disk_free = %llu MB = %llu GB\nDisk_available = %llu MB = %llu GB\n",
		freeDisk>>20, freeDisk>>30, availableDisk>>20, availableDisk>>30);

	block->availSize = (unsigned int)(availableDisk >> 10);
	
	return block->availSize;
}

int BlockDevice::CopyToNand()
{
	unsigned int avail;
	struct NonvolatileBlock * block;
	block = &nBlock;

	strcpy(block->Path, NANDPATH);
	strcat(block->Path, FILENAME);
	ALOGD("nand path : %s\n", block->Path);

	//avail = getDiskFreeSpace(block);
	//if(avail < mBlockSize / 1024){
	//	return -ENOSPC;
	//}
	return CopyToDisk(block);
}
