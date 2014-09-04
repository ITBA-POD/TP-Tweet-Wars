package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.GameMasterImpl;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.TweetsProviderImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class App
{
	private App()
	{
	}

	public static void main( String[] args )
    {
	    System.out.println( "Hello World!" );
	    final GameMasterImpl gameMaster = new GameMasterImpl();
	    final TweetsProvider tweetsProvider = new TweetsProviderImpl(true);

	    try {
		    final Registry registry = LocateRegistry.createRegistry(7242);

		    //noinspection CastToIncompatibleInterface
		    final TweetsProvider stub = (TweetsProvider) UnicastRemoteObject.exportObject(tweetsProvider, 0);
		    registry.bind("tweetsProvider", stub);
//		    registry.bind("gameMaster", UnicastRemoteObject.exportObject(gameMaster, 0));


	    } catch (RemoteException e) {
		    e.printStackTrace();
	    } catch (AlreadyBoundException e) {
		    e.printStackTrace();
	    }

    }
}
