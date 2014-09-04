package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.FortuneWheel;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.TweetsProviderImpl;
import org.junit.Test;

import javax.annotation.Nonnull;

import java.rmi.RemoteException;

import static org.assertj.core.api.Assertions.*;

public class AppTest
{
	public static final int LOOP = 20;
	public static final String HASH = "test";
	public static final String USER = "user";
	final FortuneWheel fortuneWheel = new FortuneWheel();

    @Test
    public void testFortuneWheel()
    {
	    //noinspection MagicNumber
	    for (int i = 0; i < LOOP; i++) {
		    final String next = fortuneWheel.next();
		    assertThat(next).isNotNull();
	    }
    }

	@Test
	public void testStatus()
	{
		for (int i = 0; i < LOOP; i++) {
			final String text = fortuneWheel.next();
			final Status status = new Status(i, text, USER, HASH);
			assertThat(status.getId()).isEqualTo(i);
			assertThat(status.getText()).isEqualTo(text);
			assertStatus(status);
		}
	}

	@Test
	public void testTweetsProvider()
	{
		final TweetsProvider provider = new TweetsProviderImpl();
		final GamePlayer player = new DummyPlayer();

		for (int i = 0; i < LOOP; i++) {
			try {
				final Status status = provider.getNewTweet(player, HASH);
				assertStatus(status);
			} catch (RemoteException e) {
				fail("nono", e);
			}
		}
	}

	private void assertStatus(Status status)
	{
		assertThat(status.getCheck()).isEqualTo(status.generateCheck(HASH));
	}

	class DummyPlayer implements GamePlayer
	{

		@Nonnull @Override public String getId()
		{
			return "meTest";
		}

		@Override public void publishTweet(@Nonnull Status tweet)
		{

		}
	}
}
