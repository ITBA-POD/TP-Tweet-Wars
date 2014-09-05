package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class DummyPlayer implements GamePlayer
{
	public static final String HASH = "test";
	private static final AtomicInteger count = new AtomicInteger();
	private static final Random random = new Random();
	private static final long serialVersionUID = -3583518950845834237L;
	private final String id;
	private final String hash;

	public DummyPlayer()
	{
		id = "player-" + count.incrementAndGet() + "--" + random.nextInt(9999);
		hash = "hash" + random.nextInt(9999);
	}

	@Nonnull @Override public String getId()
	{
		return id;
	}

	public String getHash()
	{
		return hash;
	}

	@Override public void publishTweet(@Nonnull Status tweet)
	{

	}
}
