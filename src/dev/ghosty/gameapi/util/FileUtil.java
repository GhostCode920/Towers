package dev.ghosty.gameapi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class FileUtil {
	
	public static void copy(File source, File destination) throws IOException {
		// if it is a folder, we copy all files that are in
		if(source.isDirectory()) {
			if(!destination.exists())
				destination.mkdir();
			String[] files = source.list();
			if(files == null) return;
			for(String file : files) {
				File newSource = new File(source,file);
				File newDest = new File(destination,file);
				copy(newSource, newDest);
			}
		}else {
			InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(destination);
			byte[] buffer = new byte[1024];
			int length;
			while((length=in.read(buffer))>0)
				out.write(buffer,0,length);
			in.close();
			out.close();
		}
	}
	
	public static void delete(File file) {
		if(!file.exists()) return;
		// if it is a folder, we first delete all files it contains
		if(file.isDirectory()) {
			String[] files = file.list();
			if(files == null) return;
			for(String f : files)
				delete(new File(file,f));
		}else
			file.delete();
	}

}
