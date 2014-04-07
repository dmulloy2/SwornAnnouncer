package com.squidfruit.swornannouncer;

import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;

/**
 * @author dmulloy2
 */

@AllArgsConstructor
public class MessageSet
{
	private int lastIndex;

	private String name;
	private long delay;
	private boolean random;
	private List<String> messages;

	public String getNextMessage()
	{
		if ( random )
		{
			return messages.get( new Random().nextInt( messages.size() ) );
		}

		String message = messages.get( lastIndex );

		lastIndex++;
		if ( lastIndex >= messages.size() )
			lastIndex = 0;

		return message;
	}

	public long getDelayInTicks()
	{
		return delay * 20;
	}

	public String getPermission()
	{
		return "swornannouncer." + name.toLowerCase();
	}
}