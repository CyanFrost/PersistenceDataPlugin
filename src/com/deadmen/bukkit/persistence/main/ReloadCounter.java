package com.deadmen.bukkit.persistence.main;


public class ReloadCounter extends Thread {
	
	public ReloadCounter(String name){
		this.setName(name);
	}
	
	private int reloadCount;
	public void setReloadCount(int i){
		synchronized(this){
			reloadCount = i;
		}
	}
	public void addReload(){
		synchronized(this){
			reloadCount = reloadCount + 1;
		}
	}
	public int getReloadCount(){
		synchronized(this){
			return reloadCount;
		}
	}
	
	public void run() {
		while(true){
			try{
				Thread.sleep(1000000);
			}catch(Exception e){break;}
		}
	}
	
	public void end(){
		this.interrupt();
	}
	
	public static  ReloadCounter retrievePersistentObject(String name){
		try{
			for(Thread t: Thread.getAllStackTraces().keySet()){
				if(t.getName().equals(name)) return (ReloadCounter)t;
			}
		}catch(Exception e){e.printStackTrace();}
		throw new NullPointerException("No Persistence Object named " + name + " exist.");
	}
}
