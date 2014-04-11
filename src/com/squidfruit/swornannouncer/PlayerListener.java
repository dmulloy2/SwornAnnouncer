package com.squidfruit.swornannouncer;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener
{
	private SwornAnnouncer plugin;
	public PlayerListener(SwornAnnouncer plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if ( plugin.getConfig().getBoolean( "onJoin.enabled" ) )
		{
			final Player player = event.getPlayer();
			final ItemStack inHand = new ItemStack( player.getItemInHand() );
			final ItemStack give = new ItemStack( Material.APPLE );

			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					ItemMeta meta = give.getItemMeta();
					meta.setDisplayName( ChatColor.GREEN + "Welcome to the server, " + player.getName() );
					give.setItemMeta( meta );

					player.getInventory().setItemInHand( give );

					// Fireworks :D
					Firework firework = player.getWorld().spawn( player.getLocation(), Firework.class );
					FireworkMeta fm = firework.getFireworkMeta();
					fm.addEffect( FireworkEffect.builder().flicker( false ).trail( true ).with( Type.BALL ).with( Type.BALL_LARGE )
							.with( Type.STAR ).withColor( Color.ORANGE ).withColor( Color.YELLOW ).withFade( Color.PURPLE )
							.withFade( Color.RED ).build() );
					fm.setPower( 2 );
					firework.setFireworkMeta( fm );

					firework = player.getWorld().spawn( player.getLocation(), Firework.class );
					fm = firework.getFireworkMeta();
					fm.addEffect( FireworkEffect.builder().flicker( false ).trail( true ).with( Type.BALL ).with( Type.BURST )
							.with( Type.STAR ).withColor( Color.GREEN ).withColor( Color.YELLOW ).withFade( Color.PURPLE )
							.withFade( Color.AQUA ).build() );

					fm.setPower( 1 );
					firework.setFireworkMeta( fm );

					// Broadcast message, if applicable
					String message = plugin.getConfig().getString( "onJoin.message" );
					if ( ! message.isEmpty() )
					{
						message = ChatColor.translateAlternateColorCodes( '&', message );
						for ( Player p : plugin.getServer().getOnlinePlayers() )
						{
							p.sendMessage( message );
						}
					}
				}
			}.runTaskLater( plugin, 1L );

			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Return their item
					player.getInventory().remove( give );
					player.setItemInHand( inHand );
				}
			}.runTaskLater( plugin, 35L );
		}
	}
}