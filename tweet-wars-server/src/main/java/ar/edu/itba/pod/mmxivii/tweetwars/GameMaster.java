package ar.edu.itba.pod.mmxivii.tweetwars;

import javax.annotation.Nonnull;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameMaster extends Remote
{
	void newPlayer(@Nonnull GamePlayer player, @Nonnull String hash);

	void tweetReceived(@Nonnull GamePlayer player, @Nonnull Status tweet) throws RemoteException;

	void tweetsReceived(@Nonnull GamePlayer player, @Nonnull Status[] tweets) throws RemoteException;
}
