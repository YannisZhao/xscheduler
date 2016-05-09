/** 
 * Copyright (c) 2016, zhaoyjun0222@gmail.com All Rights Reserved.  
 */

package org.yannis.xscheduler.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

	public static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException {
		String pkgDirName = packageName.replace('.', '/');
		List<Class<?>> classes = new ArrayList<Class<?>>();
		File pkgDir = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL resource = loader.getResource(pkgDirName);
		if (resource == null){
			return null;
		}
		pkgDir = new File(resource.getFile());
		if (pkgDir.exists()) {
			String[] files = pkgDir.list();
			for (int i = 0; i < files.length; i++) {
				if (files[i].endsWith(".class")) {
					classes.add(Class.forName(packageName + '.' + files[i].substring(0, files[i].length() - 6)));
				}
			}
		} else{
			return null;
		}
		return classes;
	}
	
	public static List<Class<?>> getClassesWithInterface(String packageName, Class<?> interfaceClazz) throws ClassNotFoundException {
		
		if(interfaceClazz == null || !interfaceClazz.isInterface()){
			return null;
		}
		
		List<Class<?>> classes = new ArrayList<Class<?>>();
		
		List<Class<?>> list = getClasses(packageName);
		
		if(list!=null){
			for(Class<?> clazz : list){
				if(interfaceClazz.isAssignableFrom(clazz)){
					classes.add(clazz);
				}
			}
		}
		
		return classes;
	}

	public static List<String> getClassNamesWithInterface(String packageName, Class<?> interfaceClazz) throws ClassNotFoundException {
		if(interfaceClazz == null || !interfaceClazz.isInterface()){
			return null;
		}
		
		List<String> classNames = new ArrayList<String>();
		
		List<Class<?>> list = getClasses(packageName);
		
		if(list!=null){
			for(Class<?> clazz : list){
				if(interfaceClazz.isAssignableFrom(clazz)){
					classNames.add(clazz.getCanonicalName());
				}
			}
		}
		
		return classNames;
	}

}
