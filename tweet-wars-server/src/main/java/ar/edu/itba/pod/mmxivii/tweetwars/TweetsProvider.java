package ar.edu.itba.pod.mmxivii.tweetwars;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface TweetsProvider extends Remote
{
	@Nonnull Status getNewTweet(@Nonnull GamePlayer player, @Nonnull String hash) throws RemoteException;

	@Nonnull
	Status[] getNewTweets(@Nonnull GamePlayer player) throws RemoteException;

	@Nullable Status getTweet(long id) throws RemoteException;
}
