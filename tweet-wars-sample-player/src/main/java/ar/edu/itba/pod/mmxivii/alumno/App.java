package ar.edu.itba.pod.mmxivii.alumno;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App
{
	public static final String TWEETS_PROVIDER_NAME = "tweetsProvider";
	public static final String GAME_MASTER_NAME = "gameMaster";
	private App()
	{
	}

	public static void main(String[] args)
	{
		final GamePlayer gp = new GamePlayer("yo", "aquel");
		final GamePlayer gp2 = new GamePlayer("yo2", "aquel otro");
		System.out.println("empezando!");
		try {
			final Registry registry = LocateRegistry.getRegistry(args[0], 7242);
			final TweetsProvider tweetsProvider = (TweetsProvider) registry.lookup(TWEETS_PROVIDER_NAME);
			final GameMaster gameMaster = (GameMaster) registry.lookup(GAME_MASTER_NAME);

			final String hash = "abceddd";
			try {
				gameMaster.newPlayer(gp, hash);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			try {
				gameMaster.newPlayer(gp2, hash);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			final Status[] tweets = tweetsProvider.getNewTweets(gp, hash, 10);
			for (Status tweet : tweets) {
				System.out.println("tweet = " + tweet);
				gameMaster.tweetReceived(gp2, tweet);
			}

			for (int i = 0; i < 10; i++) {
				System.out.println("new tweets " + i);
				final Status[] newTweets = tweetsProvider.getNewTweets(gp, hash, 100);
				gameMaster.tweetsReceived(gp2, newTweets);
			}

		} catch (RemoteException | NotBoundException e) {
			System.err.println("App Error: " + e.getMessage());
			System.exit(-1);
		}
		System.out.println("Hola alumno!");
	}
}
