package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static org.assertj.core.api.Assertions.assertThat;

public class MainAppTest
{
	private static final int WAIT_APP = 500;
	private final DummyPlayer player1 = new DummyPlayer();
	private final DummyPlayer player2 = new DummyPlayer();

	@Test public void testMain() throws RemoteException, NotBoundException, InterruptedException
	{
		final int port = 7242;
		new Thread() {
			@Override
			public void run()
			{
				App.main(new String[]{"-p", String.valueOf(port)});
			}
		}.start();
		Thread.sleep(WAIT_APP);
		final Registry registry = LocateRegistry.getRegistry(port);

		final TweetsProvider tweetsProvider = (TweetsProvider) registry.lookup(App.TWEETS_PROVIDER_NAME);
		assertThat(tweetsProvider).isNotNull();

		final GameMaster gameMaster = (GameMaster) registry.lookup(App.GAME_MASTER_NAME);
		assertThat(gameMaster).isNotNull();

		gameMaster.newPlayer(player1, player1.getHash());
		gameMaster.newPlayer(player2, player2.getHash());

		final Status tweet = tweetsProvider.getNewTweet(player1, player1.getHash());
		gameMaster.tweetReceived(player2, tweet);
		App.shutdown();
	}
}
