package com.squidfruit.swornannouncer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SwornAnnouncerCommand implements CommandExecutor
{
	private SwornAnnouncer plugin;
	public SwornAnnouncerCommand(SwornAnnouncer plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if ( commandLabel.equalsIgnoreCase( "sareload" ) )
		{
			Player player = (Player) sender;
			if ( player.hasPermission( "swornannouncer.reload" ) )
			{
				plugin.reloadConfig();
				player.sendMessage( ChatColor.GREEN + "SwornAnnouncer Has Been Reloaded" );

			} else
			{
				player.sendMessage( ChatColor.RED + "Sorry You Dont Have Permission To Reload" );
			}
		}

		return true;
	}
}