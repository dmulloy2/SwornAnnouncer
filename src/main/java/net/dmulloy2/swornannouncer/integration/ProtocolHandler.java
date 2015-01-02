/**
 * (c) 2015 dmulloy2
 */
package net.dmulloy2.swornannouncer.integration;

import java.util.logging.Level;

import net.dmulloy2.integration.DependencyProvider;
import net.dmulloy2.swornannouncer.SwornAnnouncer;
import net.dmulloy2.util.Util;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

/**
 * @author dmulloy2
 */

public class ProtocolHandler extends DependencyProvider<ProtocolLibrary>
{
	private ProtocolManager manager;
	public ProtocolHandler(SwornAnnouncer plugin)
	{
		super(plugin, "ProtocolLib");
	}

	@Override
	public void onEnable()
	{
		manager = ProtocolLibrary.getProtocolManager();
	}

	/**
	 * Sends a given player an Action Bar message.
	 * 
	 * @param player Player to send the message to
	 * @param message Message to send
	 * @return True if sending was successful, false if not
	 */
	public final boolean sendActionMessage(Player player, String message)
	{
		if (! isEnabled())
			return false;

		try
		{
			String json = "{\"text\": \"" + message + "\"}";
			WrappedChatComponent component = WrappedChatComponent.fromJson(json);

			PacketConstructor constructor = manager.createPacketConstructor(PacketType.Play.Server.CHAT,
					component.getHandleType(), byte.class);
			PacketContainer packet = constructor.createPacket(component.getHandle(), (byte) 2);

			manager.sendServerPacket(player, packet);
			return true;
		}
		catch (Throwable ex)
		{
			handler.getLogHandler().log(Level.WARNING, Util.getUsefulStack(ex, "sending action bar message to {0}", player.getName()));
		}

		return false;
	}
}