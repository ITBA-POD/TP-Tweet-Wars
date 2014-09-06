package ar.edu.itba.pod.mmxivii.tweetwars;

import javax.annotation.Nonnull;
import java.io.Serializable;

public class GamePlayer implements Serializable
{
	private static final long serialVersionUID = -5329286472138297210L;
	@Nonnull private final String id;
	@Nonnull private final String description;

	public GamePlayer(@Nonnull String id, @Nonnull String description)
	{
		this.id = id;
		this.description = description;
	}

	@Nonnull
	public String getId()
	{
		return id;
	}

	@Nonnull
	public String getDescription()
	{
		return description;
	}

	@Override
	public String toString()
	{
		return "GamePlayer{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
