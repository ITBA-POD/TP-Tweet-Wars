package ar.edu.itba.pod.mmxivii.tweetwars.impl;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

import javax.annotation.Nonnull;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GameMasterImpl implements GameMaster
{
	public static final String PLAYER_FIELD = "player";
	public static final String HASH_FIELD = "hash";
	public static final String TWEET_FIELD = "tweet";
	public static final String TWEETS_FIELDS = "tweets";
	public static final int ALREADY_REGISTERED_SCORE = 1;
	public static final int FIRST_REGISTERED_SCORE = 10;
	@Nonnull private final Map<String, GamePlayerData> players = Collections.synchronizedMap(new HashMap<String, GamePlayerData>());
	@Nonnull private final TweetsProviderImpl tweetsProvider;


	public GameMasterImpl(@Nonnull TweetsProviderImpl tweetsProvider)
	{
		this.tweetsProvider = tweetsProvider;
	}

	@Nonnull public TweetsProvider getTweetsProvider()
	{
		return tweetsProvider;
	}

	@Override
	public void newPlayer(@Nonnull GamePlayer player, @Nonnull String hash)
	{
		//noinspection ConstantConditions
		if (hash == null) throw new NullPointerException(HASH_FIELD);
		//noinspection ConstantConditions
		if (player == null) throw new NullPointerException(PLAYER_FIELD);

		if (players.containsKey(player.getId())) throw new IllegalArgumentException("player id already registered: " + player.getId());

		players.put(player.getId(), new GamePlayerData(player, hash));
	}

	@Override
	public int tweetReceived(@Nonnull GamePlayer player, @Nonnull Status tweet) throws RemoteException
	{
		//noinspection ConstantConditions
		if (tweet == null) throw new NullPointerException(TWEET_FIELD);
		final GamePlayerData playerData = getGamePlayerData(player);

		return registerTweet(tweet, playerData);
	}

	@Override
	public int tweetsReceived(@Nonnull GamePlayer player, @Nonnull Status[] tweets) throws RemoteException
	{
		//noinspection ConstantConditions
		if (tweets == null) throw new NullPointerException(TWEETS_FIELDS);
		if (tweets.length < 1 || tweets.length > 100) throw new IllegalArgumentException("Invalid tweeets size");
		final GamePlayerData playerData = getGamePlayerData(player);

		int result = 0;
		for (Status tweet : tweets) result = registerTweet(tweet, playerData);
		return result;
	}

	@Override
	public int getScore(@Nonnull GamePlayer player) throws RemoteException
	{
		final GamePlayerData playerData = getGamePlayerData(player);
		return playerData.score.get();
	}

	@Override
	public Map<Integer, String> getScores() throws RemoteException
	{
		final TreeMap<Integer, String> result = new TreeMap<>(Collections.reverseOrder());
		for (GamePlayerData data : players.values()) {
			result.put(data.score.get(), data.getId());
		}
		return result;
	}

	@Nonnull private GamePlayerData getGamePlayerData(@Nonnull GamePlayer player)
	{
		//noinspection ConstantConditions
		if (player == null) throw new NullPointerException(PLAYER_FIELD);
		return getGamePlayerData(player.getId());
	}

	@Nonnull private GamePlayerData getGamePlayerData(@Nonnull String playerId)
	{
		//noinspection ConstantConditions
		if (playerId == null) throw new NullPointerException(PLAYER_FIELD);
		final GamePlayerData data = players.get(playerId);
		if (data == null) throw new IllegalArgumentException("player not registered: " + playerId);
		if (data.isBanned) throw new IllegalArgumentException("player is banned, cannot play anymore: " + playerId);
		return data;
	}

	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	private int registerTweet(@Nonnull Status tweet, @Nonnull GamePlayerData playerData)
	{
		try {
			if (tweet.getSource().equals(playerData.getId())) throw new IllegalArgumentException("Cannot register your own tweets");
			final GamePlayerData sourcePlayerData = getGamePlayerData(tweet.getSource());

			final int[] r = tweetsProvider.registerTweet(tweet, sourcePlayerData.player, sourcePlayerData.hash);

			sourcePlayerData.addAndGet(r[1]);
			return playerData.addAndGet(r[0]);
		} catch (IllegalArgumentException e) {
			playerData.banned();
			throw e;
		}
	}

	class GamePlayerData
	{
		final GamePlayer player;
		final String hash;
		AtomicInteger score = new AtomicInteger();
		boolean isBanned;

		GamePlayerData(@Nonnull GamePlayer player, @Nonnull String hash)
		{
			this.player = player;
			this.hash = hash;
		}

		void banned()
		{
			isBanned = true;
		}

		int addAndGet(int delta)
		{
			return score.addAndGet(delta);
		}

		String getId()
		{
			return player.getId();
		}
	}
}
