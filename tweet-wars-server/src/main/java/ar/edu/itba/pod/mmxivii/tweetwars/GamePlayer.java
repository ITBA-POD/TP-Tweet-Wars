package ar.edu.itba.pod.mmxivii.tweetwars;

import javax.annotation.Nonnull;
import java.io.Serializable;

public interface GamePlayer extends Serializable
{
	@Nonnull String getId();

	void publishTweet(@Nonnull Status tweet);
}
