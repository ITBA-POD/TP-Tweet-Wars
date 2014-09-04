package ar.edu.itba.pod.mmxivii.tweetwars.impl;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;

import javax.annotation.Nonnull;
import java.rmi.RemoteException;
import java.util.Collection;

public class GameMasterImpl implements GameMaster
{
	@Override
	public void newPlayer(@Nonnull GamePlayer player)
	{

	}

	@Override
	public void tweetReceived(@Nonnull GamePlayer player, @Nonnull Status tweet) throws RemoteException
	{

	}

	@Override
	public void tweetsReceived(@Nonnull GamePlayer player, @Nonnull Collection<Status> tweets) throws RemoteException
	{

	}
}
