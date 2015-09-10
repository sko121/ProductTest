package com.thtfit.test;

import android.util.Log;
import android.os.StatFs;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.util.Log;
import java.util.Random;
import android.widget.Toast;
import java.math.BigInteger;
import java.security.MessageDigest;


public class FileManager {
	private final static String LOG_TAG = "Advertising_Player";
	private static final int BUFFER = 2048;
	private long mDirSize = 0;
	private final Context mContext;

	public FileManager(Context context) {
		mContext = context;
	}

	public void george_test(String msg){
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
		Log.d(LOG_TAG, "debug:"+msg+BUFFER);
	}
	
	public int touchFile(String file, int size){
		File fd = new File(file);
		String path = fd.getParent();
		File Dir = new File(path);
	        byte[] buffer = new byte[BUFFER];
		FileOutputStream o_stream = null;
	        Random rnd = new Random();

		if(fd.exists()){
			if(fd.delete())
				Log.d(LOG_TAG, "file delete");
		}

		if(Dir.getUsableSpace() < size * 1024 * 1024 * 1.1)
			return -1;
		
		Dir.setWritable(true);
		try {
			fd.createNewFile();
			Log.d(LOG_TAG, "file touch success");	
		} catch (Exception e) { 
			e.printStackTrace();
			Log.d(LOG_TAG, "file touch failed");	
			return -2;
		}


		try {
                	o_stream = new FileOutputStream(fd);
			for(int i = 0; i < size * 512; i++) {
        			rnd.nextBytes(buffer);
				o_stream.write(buffer, 0, BUFFER); 
			}
			o_stream.close();
		}catch (IOException e){
                 	Log.e("error", e.getMessage());
			return -3;
		}

		return 0;
	}

	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
  		}
  		MessageDigest digest = null;
	  	FileInputStream in = null;
  		byte buffer[] = new byte[BUFFER];
  		int len;
  		try {
   			digest = MessageDigest.getInstance("MD5");
   			in = new FileInputStream(file);
   			while ((len = in.read(buffer, 0, BUFFER)) != -1) {
    				digest.update(buffer, 0, len);
   			}
   			in.close();
  		} catch (Exception e) {
   			e.printStackTrace();
   			return null;
  		}

  		BigInteger bigInt = new BigInteger(1, digest.digest());
  		return bigInt.toString(16);
 	}
	public boolean compareFile(String src, String dst) {
		String src_md5, dst_md5;
		File src_fd = new File(src);
		File dst_fd = new File(dst);
		src_md5 = getFileMD5(src_fd);	
		dst_md5 = getFileMD5(dst_fd);	
        Log.d(LOG_TAG, "SRC MD5"+src_md5);
        Log.d(LOG_TAG, "DST MD5"+dst_md5);
		if(dst_md5.equals(src_md5))
			return true;
		else
			return false;
	}

	public int copyToDirectory(String old, String newDir) {
                File old_file = new File(old);
                File temp_dir = new File(newDir);
		long usable = temp_dir.getUsableSpace();
                byte[] data = new byte[BUFFER];
                int read = 0;
                String copyName = mContext.getResources().getString(R.string.Copied);

		temp_dir.setWritable(true);
                if(!(old_file.isFile() && temp_dir.isDirectory() && temp_dir.canWrite())){
			Log.d(LOG_TAG, "argument error");
			return -2;
		}
		
		if(old_file.length() > usable){		
			Log.d(LOG_TAG, "obj dir haven't enough size too small to copy");
                        return -3;
		}

		String new_name = "";
		if(old_file.getParent().equals(temp_dir.getAbsolutePath())){
			new_name = newDir + "/" + copyName + old.substring(old.lastIndexOf("/") + 1, old.length());
			int n = 2;
			while(new File(new_name).exists()){
				new_name = newDir + "/" + copyName + n + "_" + old.substring(old.lastIndexOf("/") + 1, old.length());
				n++;
			}
		}else{
			new_name = newDir + old.substring(old.lastIndexOf("/"), old.length());
		}


		File cp_file = new File(new_name);
		try {
			BufferedOutputStream o_stream = new BufferedOutputStream(new FileOutputStream(cp_file));
			BufferedInputStream i_stream = new BufferedInputStream(new FileInputStream(old_file));

			while((read = i_stream.read(data, 0, BUFFER)) != -1)
				o_stream.write(data, 0, read);

			o_stream.flush();
			i_stream.close();
			o_stream.close();

		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", e.getMessage());
			return -1;
		} catch (IOException e) {
			Log.e("IOException", e.getMessage());
			return -1;
		}

		return 0;
	}

	private void get_dir_size(File path) {
		if(path.isFile()&& path.canRead()){
			mDirSize += path.length();
		}else{
			File[] list = path.listFiles();
			int len;
			
			if(list != null) {
				len = list.length;
				Log.d(LOG_TAG, "debug:"+list.length);
				
				for (int i = 0; i < len; i++) {
					if(list[i].isFile() && list[i].canRead()) {
						mDirSize += list[i].length();
						
					} else if(list[i].isDirectory() && list[i].canRead()) { 
						get_dir_size(list[i]);
					}
				}
			}
		}
	}

	public long getDirSize(String path) {
		get_dir_size(new File(path));
		Log.d(LOG_TAG, "debug:"+mDirSize);

		return mDirSize;
	}

	public void test() {
		//判断磁盘空间是否足够
		long totalSize = 88;
		StatFs statfs = new StatFs("/data");
		long availsize	 = statfs.getBlockSize() * ((long)statfs.getAvailableBlocks());
		Log.d(LOG_TAG, "debug:getAvailableBlocks"+statfs.getAvailableBlocks()+"BlockSize"+statfs.getBlockSize());
		//StatFs param = new StatFs("/data/device.info");
		//log.d(LOG_TAG, "debug:device.info = "+param);
		/*for(int i = 1; i < param.size(); i++){
			FileManager fm = new FileManager(mContext);
			totalSize += fm.getDirSize(param.get(i));
		}*/
	}
}
