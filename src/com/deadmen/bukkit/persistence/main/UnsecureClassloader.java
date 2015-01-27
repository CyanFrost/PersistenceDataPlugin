package com.deadmen.bukkit.persistence.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;

public class UnsecureClassloader {

	private static Method defineClazz = null;

	public static Method getClassDefiner(){return defineClazz;}

	protected static Class<?> defineClass(ClassLoader cl, String name, byte[] bytes, int offset, int length) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(defineClazz == null) throw new NullPointerException("Method: \"defineClass\" Is Unset!"); //No Null
		if(cl == null) throw new NullPointerException("ClassLoader Can Not Be Null!"); //No Null
		if(name == null) throw new NullPointerException("ClassName Can Not Be Null!"); //No Null
		if(name.length()<= 0) throw new NullPointerException("ClassName Can Not Be Null!"); //No Null
		return (Class<?>) defineClazz.invoke(cl, name, bytes, offset, length);
	}

	public static Class<?> loadClass(String name, byte[] bytes) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		if(defineClazz == null) {setMethod();}
		return defineClass(Bukkit.class.getClassLoader(),name, bytes,0,bytes.length);
	}

	private static void setMethod(){
		for(Method m2: Bukkit.class.getClassLoader().getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethods()){
			if(m2.toString().contains(".defineClass(java.lang.String,byte[],int,int)")){
				//	System.out.println("Found the method!\n"+m2);
				m2.setAccessible(true);
				defineClazz = m2;
			}
		}
	}

	public static List<Class<?>> loadPackages(File jar, String... packages) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		return loadPackages(jar,  new ArrayList<String>(Arrays.asList(packages)));
	}

	public static List<Class<?>> loadPackages(File jar, List<String> packages) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		List<Class<?>> c = new ArrayList<Class<?>>();
		for(String s: packages){
			c.addAll(loadPackage(jar, s));
		}
		return c;
	}

	public static List<Class<?>> loadPackage(File jar, String Package) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		List<Class<?>> c = new ArrayList<Class<?>>();
		JarFile jarFile = new JarFile(jar);
		final Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			final JarEntry entry = entries.nextElement();
			if ( entry.getName().endsWith(".class")) {
				if(entry.getName().replace("/", ".").replace(".class", "").startsWith(Package)){
					c.add(loadClass(entry.getName().replace("/",".").replace(".class", ""), processBetter(jarFile.getInputStream(jarFile.getJarEntry(entry.getName())))));	
				}
			}
		}
		jarFile.close();
		return c;
	}

	public static List<Class<?>> loadClasses(File jar, String... clazzes) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		return loadClasses(jar,  new ArrayList<String>(Arrays.asList(clazzes)));
	}

	public static boolean classesDefined(String ... clazzes){
		try{
			for(String s: clazzes){
				Class.forName(s, false, Bukkit.class.getClassLoader());
			}
		}catch(Exception e){
			if(e instanceof ClassNotFoundException){
				return false;
			}
		}
		return true;
	}

	public static List<Class<?>> loadClasses(File jar, List<String> clazzes) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException  {
		List<Class<?>> c = new ArrayList<Class<?>>();
		JarFile jarFile = new JarFile(jar);
		final Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			final JarEntry entry = entries.nextElement();
			if (entry.getName().endsWith(".class")) {
				if(clazzes.contains(entry.getName().replace("/", ".").replace(".class", ""))) {
					c.add(loadClass(entry.getName().replace("/",".").replace(".class", ""), processBetter(jarFile.getInputStream(jarFile.getJarEntry(entry.getName())))));		
					clazzes.remove(entry.getName().replace("/", ".").replace(".class", ""));
				}
			}
		}
		jarFile.close();
		return c;
	}

	public static boolean classesDefined(List<String> clazzes){
		try{
			for(String s: clazzes){
				Class.forName(s, false, Bukkit.class.getClassLoader());
			}
		}catch(Exception e){
			if(e instanceof ClassNotFoundException){
				return false;
			}
		}
		return true;
	}

	public static Class<?> loadClass(File jar, String clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		Class<?> c = null;
		JarFile jarFile = new JarFile(jar);
		final Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			final JarEntry entry = entries.nextElement();
			if ( entry.getName().endsWith(".class")) {
				if(entry.getName().replace("/", ".").replace(".class", "").equals(clazz)) {
					c = loadClass(entry.getName().replace("/",".").replace(".class", ""), processBetter(jarFile.getInputStream(jarFile.getJarEntry(entry.getName()))));		
				}
			}
		}
		jarFile.close();
		return c;
	}

	public static boolean classDefined(String clazz){
		try{
			Class.forName("com.deadmen.bukkit.persistence.main.ReloadCounter", false, Bukkit.class.getClassLoader());
		}catch(Exception e){
			if(e instanceof ClassNotFoundException){
				return false;
			}
		}
		return true;
	}

	private static byte[] processBetter(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while((nRead = is.read(data, 0, data.length)) != -1) {buffer.write(data, 0, nRead);}
		buffer.flush();
		return buffer.toByteArray();
	}

}