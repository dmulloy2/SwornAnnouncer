/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.swornannouncer.commands;

import net.dmulloy2.commands.Command;
import net.dmulloy2.swornannouncer.SwornAnnouncer;

/**
 * @author dmulloy2
 */

public abstract class SwornAnnouncerCommand extends Command
{
	protected final SwornAnnouncer plugin;
	public SwornAnnouncerCommand(SwornAnnouncer plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.usesPrefix = true;
	}
}