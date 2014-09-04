package ar.edu.itba.pod.mmxivii.tweetwars.impl;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicLong;

public class TweetsProviderImpl implements TweetsProvider
{
	private final AtomicLong tweetId = new AtomicLong();
	private final FortuneWheel fortuneWheel = new FortuneWheel();

	@Nonnull
	@Override
	public Status getNewTweet(@Nonnull GamePlayer player, @Nonnull String hash) throws RemoteException
	{
		return generateTweet(player, hash);
	}

	@Nonnull
	@Override
	public Status[] getNewTweets(@Nonnull GamePlayer player) throws RemoteException
	{
		return null;
	}

	@Nullable
	@Override
	public Status getTweet(long id) throws RemoteException
	{
		return null;
	}

	private Status generateTweet(@Nonnull GamePlayer player, String hash)
	{
		return new Status(tweetId.incrementAndGet(), fortuneWheel.next(), player.getId(), hash);
	}
}
