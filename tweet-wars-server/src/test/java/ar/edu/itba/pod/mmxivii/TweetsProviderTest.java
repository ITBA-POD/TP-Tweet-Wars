package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.FortuneWheel;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.TweetsProviderImpl;
import org.junit.Test;

import java.rmi.RemoteException;

import static ar.edu.itba.pod.mmxivii.DummyPlayer.HASH;
import static ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider.MAX_BATCH_SIZE;
import static ar.edu.itba.pod.mmxivii.tweetwars.impl.TweetsProviderImpl.MAX_DELTA;
import static ar.edu.itba.pod.mmxivii.tweetwars.impl.TweetsProviderImpl.MIN_DELAY;
import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.Assertions.*;

public class TweetsProviderTest
{
	public static final int LOOP = 20;
	public static final String USER = "user";

    @Test
    public void testFortuneWheel()
    {
	    final FortuneWheel fortuneWheel = new FortuneWheel();
	    //noinspection MagicNumber
	    for (int i = 0; i < LOOP; i++) {
		    final String next = fortuneWheel.next();
		    assertThat(next).isNotNull();
	    }
    }

	@Test
	public void testStatus()
	{
		final FortuneWheel fortuneWheel = new FortuneWheel();
		for (int i = 0; i < LOOP; i++) {
			final String text = fortuneWheel.next();
			final Status status = new Status(i, text, USER, HASH);
			assertThat(status.getId()).isEqualTo(i);
			assertThat(status.getText()).isEqualTo(text);
			assertStatus(status, HASH);
		}
	}

	@Test
	public void testTweetsProvider1()
	{
		final TweetsProvider provider = new TweetsProviderImpl();
		final DummyPlayer player = new DummyPlayer();

		for (int i = 0; i < LOOP; i++) {
			try {
				final Status status = provider.getNewTweet(player, player.getHash());
				assertStatus(status, player.getHash());
				assertStatus(provider.getTweet(status.getId()), player.getHash());
				assertThat(provider.getTweet(status.getId())).isEqualTo(status);
			} catch (RemoteException e) {
				fail("nono", e);
			}
		}
	}

	@SuppressWarnings({"NestedTryStatement", "MagicNumber"})
	@Test public void testTweetsProvider2()
	{
		final TweetsProvider provider = new TweetsProviderImpl();
		final DummyPlayer player = new DummyPlayer();

		try {
			try {
				provider.getNewTweets(player, player.getHash(), 0);
				failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
			} catch (IllegalArgumentException ignored) {}
			try {
				provider.getNewTweets(player, player.getHash(), 101);
				failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
			} catch (IllegalArgumentException ignored) {}

			final Status[] statuses = provider.getNewTweets(player, player.getHash(), 100);
			assertThat(statuses).isNotNull();
			assertThat(statuses).hasSize(100);
			for (final Status status : statuses) {
				assertStatus(status, player.getHash());
				assertStatus(provider.getTweet(status.getId()), player.getHash());
				assertThat(provider.getTweet(status.getId())).isEqualTo(status);
			}
			final Status[] statuses2 = provider.getNewTweets(player, player.getHash(), 50);
			assertThat(statuses2).isNotNull();
			assertThat(statuses2).hasSize(50);
		} catch (RemoteException e) {
			fail("nono", e);
		}
	}

	@Test public void testSlowTweetsProvider()
	{
		final TweetsProvider provider = new TweetsProviderImpl(true);
		final DummyPlayer player = new DummyPlayer();

		try {
			//noinspection TooBroadScope
			long init;
			init = currentTimeMillis();
			provider.getNewTweets(player, player.getHash(), 10);
			assertThat(currentTimeMillis() - init).isBetween((long) MIN_DELAY, (long) MIN_DELAY + MAX_DELTA);

			init = currentTimeMillis() + MIN_DELAY;
			provider.getNewTweet(player, player.getHash());
			assertThat(currentTimeMillis()).isBetween(init, init + MAX_DELTA);
		} catch (RemoteException e) {
			fail("nono", e);
		}
	}

	@Test public void testTweetsProvider3()
	{
		final TweetsProvider provider = new TweetsProviderImpl();
		final DummyPlayer player = new DummyPlayer();

		try {

			final Status[] statuses = provider.getNewTweets(player, player.getHash(), MAX_BATCH_SIZE);
			assertThat(statuses).isNotNull();
			assertThat(statuses).hasSize(MAX_BATCH_SIZE);
			for (final Status status : statuses) {
				assertStatus(status, player.getHash());
				assertStatus(provider.getTweet(status.getId()), player.getHash());
				assertThat(provider.getTweet(status.getId())).isEqualTo(status);
			}
			final Status[] statuses2 = provider.getNewTweets(player, player.getHash(), 50);
			assertThat(statuses2).isNotNull();
			assertThat(statuses2).hasSize(50);

			final Status[] statuses3 = provider.getNewTweets(player, player.getHash(), MAX_BATCH_SIZE - 5);
			assertThat(statuses3).isNotNull();
			assertThat(statuses3).hasSize(MAX_BATCH_SIZE -5 );

			final long[] ids = new long[statuses3.length + 1];
			int i = 0;
			for (; i < statuses3.length; i++) {
				ids[i] = statuses3[i].getId();
			}
			ids[i] = statuses3[i - 1].getId() + 100;

			final Status[] statuses4 = provider.getTweets(ids);
			assertThat(statuses4).isNotNull();
			assertThat(statuses4.length).isEqualTo(ids.length);
			for (i = 0; i < statuses4.length - 1; i++) {
				assertThat(statuses4[i]).isEqualTo(statuses3[i]);
			}
			assertThat(statuses4[i]).isNull();

		} catch (RemoteException e) {
			fail("nono", e);
		}
	}

	private void assertStatus(Status status, String hash)
	{
		assertThat(status.getCheck()).isEqualTo(status.generateCheck(hash));
	}

}
