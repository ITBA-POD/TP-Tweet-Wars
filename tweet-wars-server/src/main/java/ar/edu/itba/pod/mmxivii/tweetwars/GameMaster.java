package ar.edu.itba.pod.mmxivii.tweetwars;

import javax.annotation.Nonnull;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface GameMaster extends Remote
{
	void newPlayer(@Nonnull GamePlayer player);

	void tweetReceived(@Nonnull GamePlayer player, @Nonnull Status tweet) throws RemoteException;

	void tweetsReceived(@Nonnull GamePlayer player, @Nonnull Collection<Status> tweets) throws RemoteException;
}
