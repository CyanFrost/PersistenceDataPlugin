package com.deadmen.bukkit.persistence.main;

public class ReloadCount {
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
	
}