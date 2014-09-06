package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class DummyPlayer extends GamePlayer
{
	public static final String HASH = "test";
	private static final AtomicInteger count = new AtomicInteger();
	private static final Random random = new Random();
	private static final long serialVersionUID = -3583518950845834237L;
	private final String hash;

	public DummyPlayer()
	{
		super("player-" + count.incrementAndGet() + "--" + random.nextInt(9999), "desc");
		hash = "hash" + random.nextInt(9999);
	}

	public String getHash()
	{
		return hash;
	}
}
