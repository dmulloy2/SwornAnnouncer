/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.swornannouncer.commands;

import net.dmulloy2.swornannouncer.SwornAnnouncer;
import net.dmulloy2.swornannouncer.types.Permission;

/**
 * @author dmulloy2
 */

public class CmdReload extends SwornAnnouncerCommand
{
	public CmdReload(SwornAnnouncer plugin)
	{
		super(plugin);
		this.name = "reload";
		this.aliases.add("rl");
		this.description = "Reloads the plugin";
		this.permission = Permission.CMD_RELOAD;
	}

	@Override
	public void perform()
	{
		long start = System.currentTimeMillis();
		sendpMessage("&eReloading &b{0}&e...", plugin.getName());

		plugin.reload();

		sendpMessage("&b{0} &ereloaded! Took &b{1} &ems!", plugin.getName(), System.currentTimeMillis() - start);
		plugin.getLogHandler().log("Reloaded {0}. Took {1} ms!", plugin.getName(), System.currentTimeMillis() - start);
	}
}