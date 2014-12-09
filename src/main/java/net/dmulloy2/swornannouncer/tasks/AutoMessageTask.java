/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.swornannouncer.tasks;

import lombok.AllArgsConstructor;
import net.dmulloy2.swornannouncer.SwornAnnouncer;
import net.dmulloy2.util.Util;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author dmulloy2
 */

@AllArgsConstructor
public class AutoMessageTask extends BukkitRunnable
{
	private final SwornAnnouncer plugin;

	@Override
	public void run()
	{
		for (Player player : Util.getOnlinePlayers())
		{
			plugin.sendMessage(player);
		}
	}
}