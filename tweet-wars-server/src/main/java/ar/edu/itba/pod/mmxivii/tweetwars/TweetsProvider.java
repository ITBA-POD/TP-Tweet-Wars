package ar.edu.itba.pod.mmxivii.tweetwars;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface TweetsProvider extends Remote
{
	@Nonnull Collection<Status> getTweets() throws RemoteException;

	@Nullable Status getTweet(long id) throws RemoteException;
}
