/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.swornannouncer.integration;

import lombok.Getter;
import net.dmulloy2.integration.IntegrationHandler;
import net.dmulloy2.swornannouncer.SwornAnnouncer;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * @author dmulloy2
 */

@Getter
public class VaultHandler extends IntegrationHandler
{
	private Permission permission;
	private boolean enabled;

	private final SwornAnnouncer plugin;
	public VaultHandler(SwornAnnouncer plugin)
	{
		this.plugin = plugin;
		this.setup();
	}

	@Override
	public void setup()
	{
		try
		{
			RegisteredServiceProvider<Permission> permProvider = plugin.getServer().getServicesManager().getRegistration(Permission.class);
			if (permProvider != null)
			{
				this.permission = permProvider.getProvider();
				this.enabled = true;

				plugin.getLogHandler().log("Permission integration through {0}.", permission.getName());
			}
		}
		catch (Throwable ex)
		{
			enabled = false;
		}
	}

	public final String getGroup(Player player)
	{
		if (enabled && permission != null)
		{
			return permission.getPrimaryGroup(player);
		}

		return null;
	}
}