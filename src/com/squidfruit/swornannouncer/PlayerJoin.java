package com.squidfruit.swornannouncer;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJoin implements Listener{
	
	private SwornAnnouncer plugin;
	
	

	public PlayerJoin(SwornAnnouncer plugin)
	{
		this.plugin = plugin;
	}
	
	
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent pje) {
        boolean MessageOnJoin = plugin.getConfig().getBoolean("onlineevent");
		if (MessageOnJoin)
		{		
       	 Player player = pje.getPlayer();
       	 
       	 	// does the new feture (adds welcome message in itembox)
       	 	 final ItemStack inv = new ItemStack(player.getItemInHand());
			 final ItemStack stack = new ItemStack(Material.DIRT, 1);
			 final ItemStack air = new ItemStack(Material.AIR);
			 
			 
			 
			 Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
	             public void run(){
	            	 Player player = pje.getPlayer();
	            	 
	            	 
		 				ItemMeta meta = stack.getItemMeta();
		 				meta.setDisplayName(ChatColor.GREEN + "Welcome To The Server " + player.getName());
		 				stack.setItemMeta(meta);
		 				
	            	 
	            	 if (player.getItemInHand().getType() == Material.AIR) {
		 	 				player.getInventory().setItemInHand(stack);
	            		 Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
	        	             public void run(){
	        	               	 Player player = pje.getPlayer();
	        	               	 player.setItemInHand(air);
	        	            	 
	 	 				
	        	             }
	 	 				
	        	             }, 35);
	            		 
	            	 }
	            	 

	 				player.getInventory().setItemInHand(stack);
	 				
	            	 
	            	 
	            	 
	            	 
	            	 
             		for (Player player2 : plugin.getServer().getOnlinePlayers()) {
             			player2.sendMessage(ChatColor.AQUA + player.getName() + "'s " + plugin.getConfig().getString("onjoinmessage")
             					.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
                     Firework f = (Firework) pje.getPlayer().getWorld().spawn(pje.getPlayer().getLocation(), Firework.class);
                     Firework f2 = (Firework) pje.getPlayer().getWorld().spawn(pje.getPlayer().getLocation(), Firework.class);
                    
                     FireworkMeta fm = f.getFireworkMeta();
                     fm.addEffect(FireworkEffect.builder()
                                     .flicker(false)
                                     .trail(true)
                                     .with(Type.BALL)
                                     .with(Type.BALL_LARGE)
                                     .with(Type.STAR)
                                     .withColor(Color.ORANGE)
                                     .withColor(Color.YELLOW)
                                     .withFade(Color.PURPLE)
                                     .withFade(Color.RED)
                                     .build());
                     
                     fm.setPower(2);
                     f.setFireworkMeta(fm);
                     

                     FireworkMeta fm2 = f2.getFireworkMeta();
                     fm2.addEffect(FireworkEffect.builder()
                             .flicker(false)
                             .trail(true)
                             .with(Type.BALL)
                             .with(Type.BURST)
                             .with(Type.STAR)
                             .withColor(Color.GREEN)
                             .withColor(Color.YELLOW)
                             .withFade(Color.PURPLE)
                             .withFade(Color.AQUA)
                             .build());
             
             fm2.setPower(1);
             f2.setFireworkMeta(fm2);
                     
                     
                     
             		}
             		}
             		
                     
                     
                     
                    
             
     }, 1);
			 
			 
			 Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
	             public void run(){
	            	 
	            	 Player player = pje.getPlayer();
	            	 player.getInventory().remove(stack);
	            	 player.setItemInHand(inv);
	            	 
	            	 
	             
	             }
			 
			 
			 }, 35);
			 }
	}
	}
		
	

			
			
		
		
		
	  




		
	  



