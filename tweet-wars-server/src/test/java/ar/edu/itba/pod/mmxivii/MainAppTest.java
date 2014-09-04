package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static ar.edu.itba.pod.mmxivii.DummyPlayer.HASH;
import static ar.edu.itba.pod.mmxivii.DummyPlayer.USER2;
import static org.assertj.core.api.Assertions.assertThat;

public class MainAppTest
{
	private static final int WAIT_APP = 500;

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

		final DummyPlayer player = new DummyPlayer();
		final DummyPlayer player2 = new DummyPlayer(USER2);
		gameMaster.newPlayer(player, HASH);
		gameMaster.newPlayer(player2, HASH);

		final Status tweet = tweetsProvider.getNewTweet(player, HASH);
		gameMaster.tweetReceived(player2, tweet);
		App.shutdown();
	}
}
