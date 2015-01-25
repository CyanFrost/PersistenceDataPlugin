package com.deadmen.bukkit.persistence.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import org.bukkit.Bukkit;

public class UnsecureClassloader {

	private static Method defineClazz = null;
	public static Method getClassDefiner(){return defineClazz;}
	protected static Class<?> defineClass(ClassLoader cl, String name, byte[] bytes, int offset, int length) throws Exception {
		if(defineClazz == null) throw new NullPointerException("Method: \"defineClass\" Is Unset!");
		if(cl == null) throw new NullPointerException("ClassLoader Can Not Be Null!");
		if(name == null) throw new NullPointerException("ClassName Can Not Be Null!");
		if(name.length()<= 0) throw new NullPointerException("ClassName Can Not Be Null!");
		return (Class<?>) defineClazz.invoke(cl, name, bytes, offset, length);
	}

	public static Class<?> loadClass(String name, byte[] bytes) throws Exception {
		if(defineClazz == null){setMethod();}
		return defineClass(Bukkit.class.getClassLoader(),name, bytes,0,bytes.length);
	}

	private static void setMethod(){
		for(Method m2: Bukkit.class.getClassLoader().getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethods()){
			if(m2.toString().contains(".defineClass(java.lang.String,byte[],int,int)")){
				System.out.println("Found the method!\n"+m2);
				m2.setAccessible(true);
				defineClazz = m2;
			}
		}
	}


	public static void LoadPackage(File jar, String Package) throws Exception{
		JarInputStream jis = new JarInputStream(new FileInputStream(jar));
		JarEntry je;
		while((je = jis.getNextJarEntry())!= null){
			if(je.getName().replace(".", "/").startsWith(Package)){
				try {
					loadClass(je.getName().replace("/", ".").replace(".class", ""), finder(jar, je.getName()));
				}catch(Exception e) {e.printStackTrace();}
			}
		}
		jis.close();

	}
	public static void LoadPackages(File jar, String ... args) throws Exception{
		for(String s: args){
			LoadPackage(jar, s);
		}
	}

	public static byte[] finder(File f, String name) throws Exception {
		JarFile jar = new JarFile(f);  
		JarEntry entry = jar.getJarEntry(name);  
		InputStream is = jar.getInputStream(entry);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();  
		int nextValue = is.read();  
		while (-1 != nextValue) {  
			byteStream.write(nextValue);  
			nextValue = is.read();  
		}  

		byte[] classByte = byteStream.toByteArray();  
		jar.close();
		return classByte;
	}
}
