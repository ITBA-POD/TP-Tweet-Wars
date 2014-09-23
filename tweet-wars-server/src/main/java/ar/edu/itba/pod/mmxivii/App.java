package ar.edu.itba.pod.mmxivii;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.GameMasterImpl;
import ar.edu.itba.pod.mmxivii.tweetwars.impl.TweetsProviderImpl;
import org.apache.commons.cli.*;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings("DuplicateStringLiteralInspection")
public class App
{
	public static final String TWEETS_PROVIDER_NAME = "tweetsProvider";
	public static final String GAME_MASTER_NAME = "gameMaster";
	private static final String PORT_S = "p";
	private static final String PORT_L = "port";
	private static final String PORT_D = "7242";
	private static final String REAL_S = "r";
	private static final String REAL_L = "real";
	private static final String REAL_D = "true";
	private static final String MAX_THREADS_S = "t";
	private static final String MAX_THREADS_L = "max-threads";
	private static Registry registry = null;

	private App()
	{
	}

	public static void main( String[] args )
    {
	    try {
		    final CommandLine cmdLine = parseArguments(args);
		    final int port = Integer.valueOf( cmdLine.getOptionValue(PORT_L, PORT_D));
		    final boolean slow = cmdLine.hasOption(REAL_L);
		    if (cmdLine.hasOption(MAX_THREADS_L)) {
			    final String maxThreads = cmdLine.getOptionValue(MAX_THREADS_L);
			    System.setProperty("sun.rmi.transport.tcp.maxConnectionThreads", maxThreads);
		    }

		    final TweetsProviderImpl tweetsProvider = new TweetsProviderImpl(slow);
		    final GameMasterImpl gameMaster = new GameMasterImpl(tweetsProvider);

		    System.out.println( String.format("Starting Tweet Wars! Port:%d Slow:%s", port, String.valueOf(slow)));
		    registry = LocateRegistry.createRegistry(port);

		    registry.bind(TWEETS_PROVIDER_NAME, UnicastRemoteObject.exportObject(tweetsProvider, 0));
		    registry.bind(GAME_MASTER_NAME, UnicastRemoteObject.exportObject(gameMaster, 0));

		    System.out.println("Waiting for players");
		    final Scanner scan = new Scanner(System.in);
		    String line;
		    do {
			    line = scan.next();
			    //noinspection NestedTryStatement
			    try {
				    printScores();
			    } catch (NotBoundException ignore) {}
		    } while(!"x".equals(line));
		    shutdown();
		    System.exit(0);

	    } catch (RemoteException | ParseException | AlreadyBoundException e) {
		    System.err.println("App Error: " + e.getMessage());
		    System.exit(-1);
	    }

    }

	private static void printScores() throws RemoteException, NotBoundException
	{
		final GameMaster gameMaster = (GameMaster) registry.lookup(GAME_MASTER_NAME);
		System.out.println("Scores:");
		for (Map.Entry<Integer, String> entry : gameMaster.getScores().entrySet()) {
			System.out.println(String.format("%s: %d", entry.getValue(), entry.getKey()));
		}

	}
	public static void shutdown()
	{
		try {
			printScores();

			registry.unbind(TWEETS_PROVIDER_NAME);
			registry.unbind(GAME_MASTER_NAME);
		} catch (RemoteException | NotBoundException e) {
			System.err.println("Shutdown Error: " + e.getMessage());
			System.exit(-1);
		}

	}

	private static CommandLine parseArguments(String[] args) throws ParseException
	{
		try {
			final Options options = new Options();
			options.addOption(PORT_S, PORT_L, true, "Referee server port");
			options.addOption(REAL_S, REAL_L, false, "With simulated network latency");
			options.addOption(MAX_THREADS_S, MAX_THREADS_L, true, "Max Threads for Server");
			options.addOption("help", false, "Help");

			// parse the command line arguments
			final CommandLine commandLine = new BasicParser().parse(options, args, false);

			if (commandLine.hasOption("help")) {
				new HelpFormatter().printHelp("java -jar tweet-wars-server.jar <options>", options);
				System.exit(-3);
			}
			return commandLine;
		}
		catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			throw exp;
		}
	}
}
