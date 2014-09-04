package ar.edu.itba.pod.mmxivii.tweetwars;

import javax.annotation.Nonnull;

public interface GamePlayer
{
	@Nonnull String getId();

	void publishTweet(@Nonnull Status tweet);
}
