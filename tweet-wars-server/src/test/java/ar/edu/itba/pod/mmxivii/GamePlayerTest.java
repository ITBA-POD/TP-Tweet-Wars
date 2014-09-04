package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
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
	private final GameMasterImpl gameMaster = new GameMasterImpl();
	private final TweetsProvider tweetsProvider = new TweetsProviderImpl();
	private final GamePlayer player = new DummyPlayer();

	@Test
	public void testGameMaster1()
	{

		gameMaster.newPlayer(player, HASH);
		gameMaster.newPlayer(new DummyPlayer("other2"), HASH);

		try {
			gameMaster.newPlayer(player, HASH);
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException ignore) {}

		try {
			//noinspection ConstantConditions
			gameMaster.newPlayer(player, null);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException ignore) {}
		try {
			//noinspection ConstantConditions
			gameMaster.newPlayer(null, HASH);
			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (NullPointerException ignore) {}

		gameMaster.newPlayer(new DummyPlayer("other3"), HASH);
	}

	@SuppressWarnings("NestedTryStatement")
	@Test
	public void testGameMaster2()
	{
		final GamePlayer player2 = new DummyPlayer(DummyPlayer.USER2);
		try {
			gameMaster.newPlayer(player, HASH);
			gameMaster.newPlayer(player2, HASH);

			final Status status = tweetsProvider.getNewTweet(player, HASH);
			final Status status2 = tweetsProvider.getNewTweet(player, HASH + "-fail");

			gameMaster.tweetReceived(player2, status);

			try {
				gameMaster.tweetReceived(new DummyPlayer("other4"), status);
				failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
			} catch (IllegalArgumentException ignore) {}

			try {
				gameMaster.tweetReceived(player, status2);
				failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
			} catch (IllegalArgumentException ignore) {}
		} catch (RemoteException e) {
			fail("no", e);
		}
	}

}
