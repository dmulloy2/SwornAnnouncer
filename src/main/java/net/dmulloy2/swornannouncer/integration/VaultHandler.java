/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.swornannouncer.integration;

import java.util.logging.Level;

import lombok.Getter;
import net.dmulloy2.integration.DependencyProvider;
import net.dmulloy2.swornannouncer.SwornAnnouncer;
import net.dmulloy2.util.Util;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * @author dmulloy2
 */

@Getter
public class VaultHandler extends DependencyProvider<Vault>
{
	private Permission permission;
	public VaultHandler(SwornAnnouncer plugin)
	{
		super(plugin, "Vault");
		this.setup();
	}

	private final void setup()
	{
		if (! isEnabled())
			return;

		try
		{
			RegisteredServiceProvider<Permission> provider = handler.getServer().getServicesManager().getRegistration(Permission.class);
			if (provider != null)
				this.permission = provider.getProvider();
		}
		catch (Throwable ex)
		{
			handler.getLogHandler().debug(Level.WARNING, Util.getUsefulStack(ex, "setup()"));
		}
	}

	public final String getGroup(Player player)
	{
		if (! isEnabled())
			return null;

		try
		{
			if (permission != null)
				return permission.getPrimaryGroup(player);
		}
		catch (Throwable ex)
		{
			handler.getLogHandler().debug(Level.WARNING, Util.getUsefulStack(ex, "getGroup(" + player.getName() + ")"));
		}

		return null;
	}
}