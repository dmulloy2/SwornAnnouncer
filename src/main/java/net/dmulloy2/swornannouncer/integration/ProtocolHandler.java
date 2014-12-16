/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.swornannouncer.integration;

import java.util.logging.Level;

import lombok.Getter;
import net.dmulloy2.integration.DependencyProvider;
import net.dmulloy2.swornannouncer.SwornAnnouncer;
import net.dmulloy2.util.Util;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

/**
 * @author dmulloy2
 */

@Getter
public class ProtocolHandler extends DependencyProvider<ProtocolLibrary>
{
	private static final PacketType CHAT = PacketType.Play.Server.CHAT;
	private static final byte ACTION_BAR = 2;

	private ProtocolManager manager;
	public ProtocolHandler(SwornAnnouncer plugin)
	{
		super(plugin, "ProtocolLib");
		if (isEnabled())
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
			PacketContainer packet = manager.createPacket(CHAT);
			packet.getChatComponents().write(0, WrappedChatComponent.fromText(message));
			packet.getBytes().write(0, ACTION_BAR);

			manager.sendServerPacket(player, packet);
			return true;
		}
		catch (Throwable ex)
		{
			handler.getLogHandler().log(Level.WARNING,
					Util.getUsefulStack(ex, "failed to send Action Bar message to " + player.getName()));
		}

		return false;
	}
}