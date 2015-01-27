package com.deadmen.bukkit.persistence.main;


public class ReloadCounter extends Thread {
	
	public ReloadCounter(String name){
		this.setName(name);
	}
	
	public ReloadCounter(String name, ReloadCount rc){
		this.setName(name);
		this.rc = rc;
	}
	
	private ReloadCount rc;
	public ReloadCount getReloadCount(){return rc;}
	
	public void run() {
		while(!this.isInterrupted()){
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
		return null;
		//throw new NullPointerException("No Persistence Object named " + name + " exist.");
	}
	
}
