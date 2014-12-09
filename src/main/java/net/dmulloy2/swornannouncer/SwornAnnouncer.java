/**
 * SwornAnnouncer - a bukkit plugin
 * Copyright (C) 2014 dmulloy2
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.dmulloy2.swornannouncer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import lombok.Getter;
import net.dmulloy2.SwornPlugin;
import net.dmulloy2.commands.CmdHelp;
import net.dmulloy2.handlers.CommandHandler;
import net.dmulloy2.handlers.LogHandler;
import net.dmulloy2.handlers.PermissionHandler;
import net.dmulloy2.swornannouncer.commands.CmdReload;
import net.dmulloy2.swornannouncer.commands.CmdSend;
import net.dmulloy2.swornannouncer.integration.ProtocolHandler;
import net.dmulloy2.swornannouncer.integration.VaultHandler;
import net.dmulloy2.swornannouncer.listeners.PlayerListener;
import net.dmulloy2.swornannouncer.tasks.AutoMessageTask;
import net.dmulloy2.swornannouncer.types.MessageSet;
import net.dmulloy2.types.Reloadable;
import net.dmulloy2.util.FormatUtil;
import net.dmulloy2.util.Util;

import org.bukkit.GameMode;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * @author dmulloy2
 */

public class SwornAnnouncer extends SwornPlugin implements Reloadable
{
	private @Getter ProtocolHandler protocolHandler;
	private @Getter VaultHandler vaultHandler;

	private @Getter String prefix = FormatUtil.format("&3[&eSwornAnnouncer&3]&e ");
	private List<Listener> listeners;

	@Override
	public void onEnable()
	{
		long start = System.currentTimeMillis();

		// Configuration
		saveDefaultConfig();
		reloadConfig();
		loadConfig();

		// Register handlers
		logHandler = new LogHandler(this);
		commandHandler = new CommandHandler(this);
		permissionHandler = new PermissionHandler(this);

		// Integration
		setupIntegration();

		// Register commands
		commandHandler.setCommandPrefix("am");
		commandHandler.registerPrefixedCommand(new CmdHelp(this));
		commandHandler.registerPrefixedCommand(new CmdReload(this));
		commandHandler.registerPrefixedCommand(new CmdSend(this));

		// Register listeners
		listeners = new ArrayList<>();
		registerListener(new PlayerListener(this));

		// AutoMessage task
		new AutoMessageTask(this).runTaskTimer(this, delay, delay);

		logHandler.log("{0} has been enabled. Took {1} ms.", getDescription().getFullName(), System.currentTimeMillis() - start);
	}

	private final void setupIntegration()
	{
		try { protocolHandler = new ProtocolHandler(this); } catch (Throwable ex) { }
		try { vaultHandler = new VaultHandler(this); } catch (Throwable ex) { }
	}

	private final void registerListener(Listener listener)
	{
		getServer().getPluginManager().registerEvents(listener, this);
		listeners.add(listener);
	}

	@Override
	public void onDisable()
	{
		long start = System.currentTimeMillis();

		// Cancel tasks
		getServer().getScheduler().cancelTasks(this);

		// Clear memory
		listeners.clear();
		messageSets.clear();

		logHandler.log("{0} has been disabled. Took {1} ms.", getDescription().getFullName(), System.currentTimeMillis() - start);
	}

	@Override
	public void reload()
	{
		reloadConfig();
		loadConfig();

		for (Listener listener : listeners)
		{
			if (listener instanceof Reloadable)
				((Reloadable) listener).reload();
		}
	}

	// ---- Config Options
	private int delay;
	private boolean useActionBar;
	private MessageSet defaultSet;
	private List<MessageSet> messageSets;

	private @Getter String messagePrefix;

	public void loadConfig()
	{
		this.delay = getConfig().getInt("delay") * 20;
		this.messagePrefix = FormatUtil.format(getConfig().getString("prefix"));
		this.useActionBar = getConfig().getBoolean("useActionBar", true);

		this.messageSets = new ArrayList<>();
		Map<String, Object> values = getConfig().getConfigurationSection("messages").getValues(false);
		for (Entry<String, Object> entry : values.entrySet())
		{
			String name = entry.getKey();
			MemorySection section = (MemorySection) entry.getValue();
			boolean isDefault = section.getBoolean("default", false);
			boolean random = section.getBoolean("random", true);
			List<String> messages = new ArrayList<>();
			for (String message : section.getStringList("messages"))
				messages.add(messagePrefix + FormatUtil.format(message));
			List<String> groups = new ArrayList<>();
			for (String group : section.getStringList("groups"))
				messages.add(group.toLowerCase());

			MessageSet messageSet = new MessageSet(name, random, messages, groups);
			messageSets.add(messageSet);

			if (isDefault)
				this.defaultSet = messageSet;
		}
	}

	public final void sendMessage(Player player)
	{
		String group = isVaultEnabled() ? vaultHandler.getGroup(player) : null;
		MessageSet messages = group != null ? getMessageSet(group) : null;
		if (messages == null && defaultSet != null)
			messages = defaultSet;

		if (messages == null)
		{
			logHandler.log(Level.WARNING, "Failed to find a message set for {0}.", player.getName());
			return;
		}

		String message = messages.getNextMessage();
		sendMessage(player, message);
	}

	private final MessageSet getMessageSet(String group)
	{
		group = group.toLowerCase();

		for (MessageSet messageSet : messageSets)
		{
			if (messageSet.getGroups().contains(group))
				return messageSet;
		}

		return null;
	}

	public final void sendMessage(Player player, String message)
	{
		// Replace variables
		message = replaceVariables(player, message);

		// Attempt to use the Action Bar (ProtocolLib)
		// Interesting caveat: Players do not see the action bar while in creative
		if (useActionBar && isProtocolLibEnabled() && player.getGameMode() != GameMode.CREATIVE)
		{
			if (protocolHandler.sendActionMessage(player, message))
				return;
		}

		// Fall back to the old method
		player.sendMessage(message);
	}

	public final String replaceVariables(Player player, String message)
	{
		// TODO: Add some more variables
		return message.replaceAll("%name", player.getName())
				.replaceAll("%world", player.getWorld().getName())
				.replaceAll("%online", Util.getOnlinePlayers().size() + "")
				.replaceAll("%max_players", getServer().getMaxPlayers() + "");
	}

	private final boolean isProtocolLibEnabled()
	{
		return protocolHandler != null && protocolHandler.isEnabled();
	}

	private final boolean isVaultEnabled()
	{
		return vaultHandler != null && vaultHandler.isEnabled();
	}
}