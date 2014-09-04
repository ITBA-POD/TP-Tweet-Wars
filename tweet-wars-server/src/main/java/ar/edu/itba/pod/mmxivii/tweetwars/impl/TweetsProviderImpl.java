package ar.edu.itba.pod.mmxivii.tweetwars.impl;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TweetsProviderImpl implements TweetsProvider
{
	public static final int MAX_BATCH_SIZE = 100;
	public static final int MIN_DELAY = 200;
	public static final int MAX_DELTA = 800;
	private final AtomicLong tweetId = new AtomicLong();
	private final FortuneWheel fortuneWheel = new FortuneWheel();
	private final Random random = new Random();
	private final boolean slow;
	private final Map<Long, Status> tweets = Collections.synchronizedMap(new HashMap<Long, Status>());

	public TweetsProviderImpl()
	{
		this(false);
	}

	public TweetsProviderImpl(boolean slow)
	{
		this.slow = slow;
	}

	@Nonnull
	@Override
	public Status getNewTweet(@Nonnull GamePlayer player, @Nonnull String hash) throws RemoteException
	{
		delay();
		return generateTweet(player, hash);
	}

	@Nonnull
	@Override
	public Status[] getNewTweets(@Nonnull GamePlayer player, String hash, int size) throws RemoteException
	{
		if (size < 1 || size > MAX_BATCH_SIZE) throw new IllegalArgumentException("invalid size: " + size);
		final Status[] result = new Status[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateTweet(player, hash);
		}
		delay();
		return result;
	}

	@Nullable
	@Override
	public Status getTweet(long id) throws RemoteException
	{
		return tweets.get(id);
	}

	private Status generateTweet(@Nonnull GamePlayer player, String hash)
	{
		final Status status = new Status(tweetId.incrementAndGet(), fortuneWheel.next(), player.getId(), hash);
		tweets.put(status.getId(), status);
		return status;
	}

	private void delay()
	{
		if (slow)
			try { Thread.sleep(random.nextInt(MAX_DELTA) + MIN_DELAY); } catch (InterruptedException ignore) {}
	}
}
