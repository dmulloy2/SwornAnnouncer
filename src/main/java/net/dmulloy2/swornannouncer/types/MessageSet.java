/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.swornannouncer.types;

import java.util.List;
import java.util.Random;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author dmulloy2
 */

@Data
@RequiredArgsConstructor
public class MessageSet
{
	private transient int lastIndex;

	private final String name;
	private final boolean random;
	private final List<String> groups;
	private final List<String> messages;

	public final String getNextMessage()
	{
		if (random)
			return messages.get(new Random().nextInt(messages.size()));

		String message = messages.get(lastIndex);

		lastIndex++;
		if (lastIndex >= messages.size())
			lastIndex = 0;

		return message;
	}

	@Override
	public String toString()
	{
		return "MessageSet[name=" + name + ", random=" + random + ", groups=" + groups + ", messages=" + messages + "]";
	}
}