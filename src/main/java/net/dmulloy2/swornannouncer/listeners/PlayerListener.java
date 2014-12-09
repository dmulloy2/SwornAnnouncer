/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.swornannouncer.listeners;

import net.dmulloy2.swornannouncer.SwornAnnouncer;
import net.dmulloy2.types.Reloadable;
import net.dmulloy2.util.Util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author dmulloy2
 */

public class PlayerListener implements Listener, Reloadable
{
	private boolean joinEnabled;
	private String joinMessage;
	private int joinDelay;

	private final SwornAnnouncer plugin;
	public PlayerListener(SwornAnnouncer plugin)
	{
		this.plugin = plugin;
		this.reload();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if (! joinEnabled)
			return;

		final String name = event.getPlayer().getName();

		class JoinMessageTask extends BukkitRunnable
		{
			@Override
			public void run()
			{
				Player player = Util.matchPlayer(name);
				if (player != null && player.isOnline())
				{
					plugin.sendMessage(player, joinMessage);
				}
			}
		}

		new JoinMessageTask().runTaskLater(plugin, joinDelay);
	}

	@Override
	public void reload()
	{
		this.joinEnabled = plugin.getConfig().getBoolean("onJoin.enabled");
		this.joinMessage = plugin.getConfig().getString("onJoin.message");
		this.joinDelay = plugin.getConfig().getInt("onJoin.delay") * 20;
	}
}