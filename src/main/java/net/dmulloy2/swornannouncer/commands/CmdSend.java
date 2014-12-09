/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.swornannouncer.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dmulloy2.swornannouncer.SwornAnnouncer;
import net.dmulloy2.swornannouncer.types.Permission;
import net.dmulloy2.util.FormatUtil;
import net.dmulloy2.util.Util;

import org.bukkit.entity.Player;

/**
 * @author dmulloy2
 */

public class CmdSend extends SwornAnnouncerCommand
{
	public CmdSend(SwornAnnouncer plugin)
	{
		super(plugin);
		this.name = "send";
		this.addRequiredArg("player");
		this.addRequiredArg("message");
		this.description = "Send an action bar message";
		this.permission = Permission.CMD_SEND;
	}

	@Override
	public void perform()
	{
		List<Player> players = new ArrayList<>();
		if (args[0].equals("*"))
		{
			players = Util.getOnlinePlayers();
		}
		else
		{
			Player player = Util.matchPlayer(args[0]);
			if (player == null)
			{
				err("Player \"&c{0}&4\" not found.", args[0]);
				return;
			}
			else
			{
				players = Arrays.asList(player);
			}
		}

		String message = plugin.getMessagePrefix() + FormatUtil.format(getFinalArg(1));

		for (Player player : players)
		{
			plugin.sendMessage(player, message);
		}

		sendpMessage("Sent message to &b{0} &eplayers.", players.size());
	}
}