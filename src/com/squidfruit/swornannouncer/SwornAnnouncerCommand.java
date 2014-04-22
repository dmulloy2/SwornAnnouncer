package com.squidfruit.swornannouncer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
			if ( sender.hasPermission( "swornannouncer.reload" ))
			{
				plugin.reloadConfig();
				plugin.loadConfig();

				sender.sendMessage( ChatColor.GREEN + "SwornAnnouncer has been reloaded!" );
			} else
			{
				sender.sendMessage( ChatColor.RED + "You do not have permission to do this!" );
			}
		}

		return true;
	}
}