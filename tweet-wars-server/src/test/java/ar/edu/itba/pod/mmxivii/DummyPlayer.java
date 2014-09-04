package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;

import javax.annotation.Nonnull;

class DummyPlayer implements GamePlayer
{
	private final String id;
	public DummyPlayer()
	{
		id = "meTest";
	}
	public DummyPlayer(@Nonnull String id)
	{
		this.id = id;
	}

	@Nonnull @Override public String getId()
	{
		return id;
	}

	@Override public void publishTweet(@Nonnull Status tweet)
	{

	}
}
