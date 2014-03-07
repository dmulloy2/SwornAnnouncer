package com.squidfruit.swornannouncer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;




public class SwornAnnouncer extends JavaPlugin {
	
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> messagesforstaff = new ArrayList<String>();
	
	SwornAnnouncer plugin;
	
	private String message;
	private String message2;
	private int index = 0;
	private int index2 = 0;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerJoin(this), this);
		
		getCommand("sareload").setExecutor(new Cmd(this));
		
		int delay = getConfig().getInt("delay", 2);
		int delayforstaff = getConfig().getInt("delayforstaff", 2);
		
		new AnnouncerTask()
				.runTaskTimer(this, delay * 20 * 20, delay * 60 * 20);
		
		new AnnouncerTask2()
		.runTaskTimer(this, delay * 40 * 20, delayforstaff * 60 * 20);

		getLogger().info(getDescription().getFullName() + " has been enabled");
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);

		getLogger().info(getDescription().getFullName() + " has been disabled");
	}

	public class AnnouncerTask extends BukkitRunnable {
		@Override
		public void run() {
			
			if (getConfig().getBoolean("random"))
			{
			
				List<String> messages = getConfig().getStringList("messages");
				message = messages.get(new Random().nextInt(messages.size()));
				for (Player player : getServer().getOnlinePlayers()) {
					if (!player.hasPermission("swornannouncer.default")){
						return;
					}
					if (message.contains("%NEWLN")){
						message.split("%NEWLN");
				}
				String[] split = message.split("%NEWLN");
				String prefix = getConfig().getString("prefix");
				for (int i = 0; i < split.length; i++) {
					   String s = split[i]; 
					   if (i == 0) s = prefix + s;
					   s = ChatColor.translateAlternateColorCodes('&', s);
					   player.sendMessage(s);

					   
				}
			}
				
			}else {
			List<String> messages = getConfig().getStringList("messages");
			 message = messages.get(index);
			for (Player player : getServer().getOnlinePlayers()) {
				if (!player.hasPermission("swornannouncer.default")){
					return;
				}
				if (message.contains("%NEWLN")){
						message.split("%NEWLN");
				}
				String[] split = message.split("%NEWLN");
				String prefix = getConfig().getString("prefix");
				for (int i = 0; i < split.length; i++) {
					   String s = split[i]; 
					   if (i == 0) s = prefix + s;
					   s = ChatColor.translateAlternateColorCodes('&', s);
					   player.sendMessage(s);
					  }
				}
			

			   index++;
			   if (index >= messages.size())
			    index = 0;
			
		}
		}
	}
	
	
	
		public class AnnouncerTask2 extends BukkitRunnable {
			@Override
			public void run() {
				
				if (getConfig().getBoolean("randomstaffmessages"))
				{
			
				List<String> messagesforstaff = getConfig().getStringList("messagesforstaff");
				message = messagesforstaff.get(new Random().nextInt(messagesforstaff.size()));
				for (Player player : getServer().getOnlinePlayers()) {
					if (!player.hasPermission("swornannouncer.staff")){
						return;
					}
					if (message2.contains("%NEWLN")){
						message2.split("%NEWLN");
				}
				String[] split = message2.split("%NEWLN");
				String prefix = getConfig().getString("prefix");
				for (int i = 0; i < split.length; i++) {
					   String s = split[i]; 
					   if (i == 0) s = prefix + s;
					   s = ChatColor.translateAlternateColorCodes('&', s);
					   player.sendMessage(s);
					   
					   
					   
				}
			}
				
			} else {
			List<String> messagesforstaff = getConfig().getStringList("messagesforstaff");
			 message2 = messagesforstaff.get(index2);
			for (Player player : getServer().getOnlinePlayers()) {
				if (!player.hasPermission("swornannouncer.staff")){
					return;
				}
				if (message2.contains("%NEWLN")){
					message2.split("%NEWLN");
			}
			String[] split = message2.split("%NEWLN");
			String prefix = getConfig().getString("prefix");
			for (int i = 0; i < split.length; i++) {
				   String s = split[i]; 
				   if (i == 0) s = prefix + s;
				   s = ChatColor.translateAlternateColorCodes('&', s);
				   player.sendMessage(s);
				  }
			}

			   index2++;
			   if (index2 >= messagesforstaff.size())
			    index2 = 0;
			   
			  
			}
		}
	}
}


