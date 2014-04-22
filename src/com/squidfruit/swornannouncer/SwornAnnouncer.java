package com.squidfruit.swornannouncer;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SwornAnnouncer extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		// Configuration
		saveDefaultConfig();
		reloadConfig();
		loadConfig();

		// Register command
		getCommand( "sareload" ).setExecutor( new SwornAnnouncerCommand( this ) );

		// Register listener
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents( new PlayerListener( this ), this );

		getLogger().info( getDescription().getFullName() + " has been enabled" );
	}

	@Override
	public void onDisable()
	{
		getServer().getScheduler().cancelTasks( this );

		getLogger().info( getDescription().getFullName() + " has been disabled" );
	}

	public void loadConfig()
	{
		getServer().getScheduler().cancelTasks( this );

		if ( getConfig().isSet( "messageSets" ) )
		{
			for ( Entry<String, Object> entry : getConfig().getConfigurationSection( "messageSets" ).getValues( false ).entrySet() )
			{
				String name = entry.getKey();
				MemorySection value = (MemorySection) entry.getValue();
				long delay = value.getLong( "delay", 120 );
				boolean random = value.getBoolean( "random", true );
				List<String> messages = value.getStringList( "messages" );
				MessageSet messageSet = new MessageSet( 0, name, delay, random, messages );
				new AutoMessageTask( messageSet ).runTaskTimer( this, messageSet.getDelayInTicks(), messageSet.getDelayInTicks() );
			}
		}
	}

	public class AutoMessageTask extends BukkitRunnable
	{
		private final MessageSet messageSet;
		public AutoMessageTask(MessageSet messageSet)
		{
			this.messageSet = messageSet;
		}

		@Override
		public void run()
		{
			String prefix = getConfig().getString( "prefix" );
			String message = ChatColor.translateAlternateColorCodes( '&', messageSet.getNextMessage() );
			String[] messages = message.split( "%NEWLN" );

			for ( Player player : getServer().getOnlinePlayers() )
			{
				if ( ! player.hasPermission( messageSet.getPermission() ) )
					continue;

				for ( int i = 0; i < messages.length; i++ )
				{
					String msg = messages[i];
					if ( i == 0 )
						msg = prefix + msg;

					player.sendMessage( msg );
				}
			}
		}
	}
}