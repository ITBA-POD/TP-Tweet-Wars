package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.GameMasterImpl;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.TweetsProviderImpl;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.Map;

import static ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider.FIRST_REGISTER_BONUS;
import static org.assertj.core.api.Assertions.*;

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
			gameMaster.newPlayer(player1, player1.getHash());
			gameMaster.newPlayer(player2, player2.getHash());

			final Status status = tweetsProvider.getNewTweet(player1, player1.getHash());
			final Status status2 = tweetsProvider.getNewTweet(player1, HASH + "-fail");
			final Status status3 = tweetsProvider.getNewTweet(player1, player1.getHash());

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

	@Test
	public void testGameMasterScores()
	{
		try {
			gameMaster.newPlayer(player1, player1.getHash());
			assertThat(gameMaster.getScore(player1)).isZero();
			gameMaster.newPlayer(player2, player2.getHash());
			gameMaster.newPlayer(player3, player3.getHash());
			assertThat(gameMaster.getScore(player3)).isZero();
			assertThat(gameMaster.getScore(player2)).isZero();

			int score1 = gameMaster.getScore(player2);
			int score2 = gameMaster.getScore(player2);
			int score3 = gameMaster.getScore(player2);

			for (int i = 0; i < 20; i++) {
				final Status status = tweetsProvider.getNewTweet(player1, player1.getHash());

				final int newScore2 = gameMaster.tweetReceived(player2, status);
				assertThat(newScore2).isEqualTo(score2 + 1 + FIRST_REGISTER_BONUS);
				score2 = newScore2;

				final int newScore3 = gameMaster.tweetReceived(player3, status);
				assertThat(newScore3).isEqualTo(score3 + 1);
				score3 = newScore3;

				final int newScore1 = gameMaster.getScore(player1);
				assertThat(newScore1).isEqualTo(score1 + 2);
				score1 = newScore1;
			}

			for (int i = 0; i < 20; i++) {
				final Status status = tweetsProvider.getNewTweet(player2, player2.getHash());

				final int newScore1 = gameMaster.tweetReceived(player1, status);
				assertThat(newScore1).isEqualTo(score1 + 1 + FIRST_REGISTER_BONUS);
				score1 = newScore1;

				final int newScore3 = gameMaster.tweetReceived(player3, status);
				assertThat(newScore3).isEqualTo(score3 + 1);
				score3 = newScore3;
			}

			int prevScore = Integer.MAX_VALUE;
			for (Map.Entry<Integer, String> entry : gameMaster.getScores().entrySet()) {
				assertThat(entry.getKey()).isLessThanOrEqualTo(prevScore);
				prevScore = entry.getKey();
			}
		} catch (RemoteException e) {
			fail("no", e);
		}
	}
}
