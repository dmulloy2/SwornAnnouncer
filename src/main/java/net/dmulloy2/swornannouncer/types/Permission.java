/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.swornannouncer.types;

import lombok.Getter;
import net.dmulloy2.types.IPermission;

/**
 * @author dmulloy2
 */

@Getter
public enum Permission implements IPermission
{
	CMD_RELOAD,
	CMD_SEND,
	;

	private final String node;
	private Permission()
	{
		this.node = toString().toLowerCase().replaceAll("_", ".");
	}
}