package com.thtfit.test;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.Gravity;

import java.io.File;

public class GetSdcardMemory 
 {   
     Context context;
     String path;
     long getAvailaleSize = 0;

     public GetSdcardMemory(Context context,String path){
     	this.context = context;
        this.path = path;
	 }	
  
        public long getSDTotalSize() {  
		    
            StatFs stat = new StatFs(path);
             
		    long blockSize = stat.getBlockSize();  
		    long totalBlocks = stat.getBlockCount(); 

            getAvailaleSize = blockSize * totalBlocks ;
            getAvailaleSize = getAvailaleSize/(1024 *1024 ); 
		    return getAvailaleSize;  
    }  
         public long getSDAvailableSize() {  

            StatFs stat = new StatFs(path); 
            
		    long blockSize = stat.getBlockSize();  
		    long availableBlocks = stat.getAvailableBlocks(); 
             
            getAvailaleSize = blockSize * availableBlocks ;
            getAvailaleSize = getAvailaleSize/(1024 *1024 ); 
		    return getAvailaleSize;  
         }  
  	     public long getRomTotalSize() {  
		    File path = Environment.getDataDirectory();  
		    StatFs stat = new StatFs(path.getPath());  
		    long blockSize = stat.getBlockSize();  
		    long totalBlocks = stat.getBlockCount(); 
 
		    getAvailaleSize = blockSize * totalBlocks ;
            getAvailaleSize = getAvailaleSize/(1024 *1024 ); 
		    return getAvailaleSize; 
        }  
        public long getRomAvailableSize() {  
		    File path = Environment.getDataDirectory();  
		    StatFs stat = new StatFs(path.getPath());  
		    long blockSize = stat.getBlockSize();  
		    long availableBlocks = stat.getAvailableBlocks();  
           
            getAvailaleSize = blockSize * availableBlocks ;
            getAvailaleSize = getAvailaleSize/(1024 *1024 ); 
		    return getAvailaleSize;
		   
    }  
 }






