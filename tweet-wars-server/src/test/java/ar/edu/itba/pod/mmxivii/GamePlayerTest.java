package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.GameMasterImpl;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.TweetsProviderImpl;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class GamePlayerTest
{
	public static final String HASH = "test";
	public static final String HASH2 = "test2";
	private final TweetsProvider tweetsProvider;
	private final GameMasterImpl gameMaster;
	private final DummyPlayer player1 = new DummyPlayer();
	private final DummyPlayer player2 = new DummyPlayer();
	private final DummyPlayer player3 = new DummyPlayer();

	public GamePlayerTest()
	{
		tweetsProvider = new TweetsProviderImpl();
		gameMaster = new GameMasterImpl((TweetsProviderImpl) tweetsProvider);
	}

	@Test
	public void testGameMaster1()
	{

		gameMaster.newPlayer(player1, player1.getHash());
		gameMaster.newPlayer(player2, player2.getHash());

		try {
			gameMaster.newPlayer(player1, HASH);
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException ignore) {}

		try {
			//noinspection ConstantConditions
			gameMaster.newPlayer(player1, null);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException ignore) {}
		try {
			//noinspection ConstantConditions
			gameMaster.newPlayer(null, HASH);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException ignore) {}

		gameMaster.newPlayer(player3, player3.getHash());
	}

	@SuppressWarnings("NestedTryStatement")
	@Test
	public void testGameMaster2()
	{
		try {
			gameMaster.newPlayer(player1, HASH);
			gameMaster.newPlayer(player2, HASH);

			final Status status = tweetsProvider.getNewTweet(player1, HASH);
			final Status status2 = tweetsProvider.getNewTweet(player1, HASH + "-fail");
			final Status status3 = tweetsProvider.getNewTweet(player1, HASH);

			final int score1 = gameMaster.tweetReceived(player2, status);

			try {
				gameMaster.tweetReceived(player3, status);
				failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
			} catch (IllegalArgumentException ignore) {}

			try {
				gameMaster.tweetReceived(player1, status2);
				failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
			} catch (IllegalArgumentException ignore) {}
		} catch (RemoteException e) {
			fail("no", e);
		}
	}

}
