/**
 * (c) 2015 dmulloy2
 */
package net.dmulloy2.swornannouncer.tasks;

import lombok.AllArgsConstructor;
import net.dmulloy2.swornannouncer.SwornAnnouncer;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author dmulloy2
 */

@AllArgsConstructor
public class MessageSendTask extends BukkitRunnable
{
	private final SwornAnnouncer plugin;
	private final Player player;
	private final String message;
	private int times;

	@Override
	public void run()
	{
		if (times > 0)
		{
			plugin.sendMessage(player, message, true);
			times -= 1;
		}
		else
		{
			plugin.getTasks().remove(player.getName());
			cancel();
		}
	}
}