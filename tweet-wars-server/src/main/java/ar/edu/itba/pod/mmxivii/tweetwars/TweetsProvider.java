package ar.edu.itba.pod.mmxivii.tweetwars;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TweetsProvider extends Remote
{
	int FIRST_REGISTER_BONUS = 100;
	int NEW_FAKE_TWEET_BONUS = 1000;
	int MAX_BATCH_SIZE = 100;

	@Nonnull Status getNewTweet(@Nonnull GamePlayer player, @Nonnull String hash) throws RemoteException;

	@Nonnull
	Status[] getNewTweets(@Nonnull GamePlayer player, String hash, int size) throws RemoteException;

	@Nullable Status getTweet(long id) throws RemoteException;

	@Nonnull Status[] getTweets(long[] ids) throws RemoteException;
}
