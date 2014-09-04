package ar.edu.itba.pod.mmxivii.tweetwars.impl;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;

import javax.annotation.Nonnull;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class GameMasterImpl implements GameMaster
{
	public static final String PLAYER_FIELD = "player";
	public static final String HASH_FIELD = "hash";
	public static final String TWEET_FIELD = "tweet";
	public static final String TWEETS_FIELDS = "tweets";
	private final Map<String, GamePlayerData> players = new HashMap<>();

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
	public void tweetReceived(@Nonnull GamePlayer player, @Nonnull Status tweet) throws RemoteException
	{
		//noinspection ConstantConditions
		if (tweet == null) throw new NullPointerException(TWEET_FIELD);
		final GamePlayerData playerData = getGamePlayerData(player);
		registerTweet(tweet, playerData);
	}

	@Override
	public void tweetsReceived(@Nonnull GamePlayer player, @Nonnull Status[] tweets) throws RemoteException
	{
		//noinspection ConstantConditions
		if (tweets == null) throw new NullPointerException(TWEETS_FIELDS);
		if (tweets.length < 1 || tweets.length > 100) throw new IllegalArgumentException("Invalid tweeets size");
		final GamePlayerData playerData = getGamePlayerData(player);
		for (Status tweet : tweets) {
			registerTweet(tweet, playerData);
		}
	}

	private GamePlayerData getGamePlayerData(@Nonnull GamePlayer player)
	{
		//noinspection ConstantConditions
		if (player == null) throw new NullPointerException(PLAYER_FIELD);
		final GamePlayerData data = players.get(player.getId());
		if (data == null) throw new IllegalArgumentException("player not registered: " + player.getId());
		return data;
	}

	private void registerTweet(Status tweet, GamePlayerData playerData)
	{
		final String hashCheck = tweet.generateCheck(playerData.hash);
		if (!tweet.getCheck().equals(hashCheck)) throw new IllegalArgumentException("invalid hash check");
	}

	class GamePlayerData
	{
		final GamePlayer player;
		final String hash;

		GamePlayerData(GamePlayer player, String hash)
		{
			this.player = player;
			this.hash = hash;
		}
	}
}
