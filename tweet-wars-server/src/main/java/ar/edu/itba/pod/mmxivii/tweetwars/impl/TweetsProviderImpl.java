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
	@Nonnull private final Map<Long, TweetData> tweets = Collections.synchronizedMap(new HashMap<Long, TweetData>());

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
		delay();
		final TweetData tweetData = tweets.get(id);
		return tweetData != null && tweetData.isReal ? tweetData.tweet : null;
	}

	void registerFakeTweet(@Nonnull Status tweet, GamePlayer player, @Nonnull String sourceHash)
	{
		final TweetData checkTweet = tweets.get(tweet.getId());
		if (checkTweet != null) {
			if (!checkTweet.tweet.equals(tweet)) throw new IllegalArgumentException("Invalid tweet: " + tweet);
			if (checkTweet.isReal) throw new IllegalArgumentException("This tweet is not fake! " + tweet);
		} else {
			registerNewFakeTweet(tweet, sourceHash);
		}
	}

	Status getTweetInternal(long id)
	{
		final TweetData tweetData = tweets.get(id);
		return tweetData != null ? tweetData.tweet : null;
	}

	int[] registerTweet(@Nonnull Status tweet, @Nonnull GamePlayer sourcePlayer, @Nonnull String sourceHash)
	{
		int tweetScore = 1;
		int playerScore = 1;
		synchronized (tweets) {
			final TweetData tweetData = tweets.get(tweet.getId());
			if (tweetData == null) {
				registerNewFakeTweet(tweet, sourceHash);
				tweetScore += NEW_FAKE_TWEET_BONUS; // first register of a fake tweet, good for the source
				playerScore += FIRST_REGISTER_BONUS; // first register of a tweet
			} else {
				if (!tweet.equals(tweetData.tweet)) throw new IllegalArgumentException("There is another tweet with the same id!");
				if (!tweetData.isRegistered) {
					playerScore += FIRST_REGISTER_BONUS;
					tweetData.isRegistered = true;
				}
			}
		}
		return new int[]{playerScore, tweetScore};
	}

	private void registerNewFakeTweet(@Nonnull Status tweet, @Nonnull String sourceHash)
	{
		synchronized (tweets) {
			final String hashCheck = tweet.generateCheck(sourceHash);
			if (!tweet.getCheck().equals(hashCheck)) throw new IllegalArgumentException("invalid hash check");
			// first register of a false tweet, good!
			final TweetData data = new TweetData(tweet, false, true);
			tweets.put(tweet.getId(), data);
		}
	}

	private Status generateTweet(@Nonnull GamePlayer player, @Nonnull String hash)
	{
		synchronized (tweets) {
			long id = tweetId.incrementAndGet();
			while (tweets.containsKey(id)) id = tweetId.incrementAndGet();
			final Status status = new Status(id, fortuneWheel.next(), player.getId(), hash);
			final TweetData tweetData = new TweetData(status, true, false);
			tweets.put(id, tweetData);
			return status;
		}
	}

	void delay()
	{
		if (slow)
			try { Thread.sleep(random.nextInt(MAX_DELTA) + MIN_DELAY); } catch (InterruptedException ignore) {}
	}

	class TweetData
	{
		Status tweet;
		boolean isReal;
		boolean isRegistered;

		TweetData(Status tweet, boolean isReal, boolean isRegistered)
		{
			this.tweet = tweet;
			this.isReal = isReal;
			this.isRegistered = isRegistered;
		}
	}
}
