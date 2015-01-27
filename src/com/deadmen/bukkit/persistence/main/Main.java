package com.deadmen.bukkit.persistence.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static final String rcn = "RCT";

	private static ReloadCount rc;

	public void onDisable() {
		try{
			new ReloadCounter(rcn,rc).start();
		}catch(Exception e) {e.printStackTrace();}
	}

	public void onEnable() {
		if(rc != null) {Bukkit.broadcastMessage("Server has Reloaded "+ChatColor.GREEN+rc.getReloadCount()+ChatColor.RESET+" times!");}
	}

	public void onLoad() {
		if(!UnsecureClassloader.classDefined("com.deadmen.bukkit.persistence.main.ReloadCounter")) {
			try{
				System.out.println(UnsecureClassloader.loadClasses(this.getFile(), "com.deadmen.bukkit.persistence.main.ReloadCounter","com.deadmen.bukkit.persistence.main.ReloadCount"));
				rc = new ReloadCount();
			}catch(Exception e) {e.printStackTrace();}
		}else {
			ReloadCounter r;
			if((r = ReloadCounter.retrievePersistentObject(rcn))!= null) {
				rc = r.getReloadCount();
				rc.addReload();
				r.end();
			}else {
				rc = new ReloadCount();
			}
		}
	}

	public void defineClasses() {
		if(!UnsecureClassloader.classDefined("com.deadmen.bukkit.persistence.main.ReloadCounter")) {
			try{
				System.out.println(UnsecureClassloader.loadClasses(this.getFile(), "com.deadmen.bukkit.persistence.main.ReloadCounter","com.deadmen.bukkit.persistence.main.ReloadCount"));
			}catch(Exception e) {e.printStackTrace();}
		}
	}


}
