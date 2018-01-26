/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferay.benchmark.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author lily
 */
public class ReplaceKeyValue {
	public static void main(String args []) throws Exception{
		ReplaceKeyValue replaceKeyValue = new ReplaceKeyValue("portal.source","/home/storage/repo/portal-ee","/home/lily/repo/test/benchmark-configs/main/configs/2");
		replaceKeyValue.loadProps();
	}

	public ReplaceKeyValue(String key, String value, String baseDir) {
		_key = key;
		_value = value;
		_baseDir = baseDir;
	}
	
	
	public void loadProps() throws Exception{  
        Properties props = new SafeProperties();  
        InputStream in = null;  
		InputStream in1 = null; 
        try {  
			List<Path> filePaths = new ArrayList<>();
			_find(_baseDir, filePaths);
			
			for(Path filePath:filePaths){

				in = new FileInputStream(filePath.toFile());
				props.load(in);
				props.setProperty(_key, _value);
				OutputStream fos = new FileOutputStream(filePath.toFile());
				props.store(fos,"Update"+_key);
			}

              
        } catch (FileNotFoundException e) {  
           
        } catch (IOException e) {  
           
        } finally {  
            try {  
                if(null != in) {  
                    in.close();  
                }  
            } catch (IOException e) {  
                  
            }  
        }  
    }  
	
	private void _find(String baseDirectory, List<Path> filePaths) 
			throws IOException, Exception {
		File baseDir = new File(baseDirectory);

		if (!baseDir.exists() || !baseDir.isDirectory()) {
			throw new Exception("Base Dir does not eist!");
		}

		try {
			Files.walkFileTree(
				baseDir.toPath(),
				new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(
							Path path, BasicFileAttributes basicFileAttributes)
						throws IOException {

						Path fileNamePath = path.getFileName();

						String fileName = fileNamePath.toString();
						
						if (fileName.equals("benchmark-ext.properties")) {
							filePaths.add(path);
						}

						return FileVisitResult.CONTINUE;
					}

				});
		}
		catch (IOException ioe) {
			throw new IOException(ioe);
		}
	}	
	
	private String _key;
	private String _value;
	private String _baseDir;
}
