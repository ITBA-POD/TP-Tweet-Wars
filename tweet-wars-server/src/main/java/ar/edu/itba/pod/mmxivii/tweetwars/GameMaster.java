package ar.edu.itba.pod.mmxivii.tweetwars;

import javax.annotation.Nonnull;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface GameMaster extends Remote
{
	int ALREADY_BANNED_SCORE = 100;
	int ALREADY_REGISTERED_SCORE = 1;
	int FIRST_BANNED_SCORE = 10000;
	int FIRST_REGISTERED_SCORE = 10;
	int MAX_TWEETS_BATCH = 100;
	int MIN_FAKE_TWEETS_BATCH = 10;

	void newPlayer(@Nonnull GamePlayer player, @Nonnull String hash) throws RemoteException;

	int tweetReceived(@Nonnull GamePlayer player, @Nonnull Status tweet) throws RemoteException;

	int tweetsReceived(@Nonnull GamePlayer player, @Nonnull Status[] tweets) throws RemoteException;

	int getScore(@Nonnull GamePlayer player) throws RemoteException;

	Map<Integer, String> getScores() throws RemoteException;

	int reportFake(@Nonnull GamePlayer player, @Nonnull Status[] tweets) throws RemoteException;
}
