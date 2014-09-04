package ar.edu.itba.pod.mmxivii.tweetwars.impl;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.*;

public class FortuneWheel
{
	private static final String PREFIX = "/fortunes/";
	private static final String[] FILES = {"joel-on-software.txt", "murphy.txt", "paul-graham.txt", "startrek.txt", "zippy.txt"};
	private static final List<String> QUOTES = loadQuotes();
	private static final int QUOTES_SIZE = QUOTES.size();
	private final Random random = new Random();

	@Nonnull public String next()
	{
		return QUOTES.get(random.nextInt(QUOTES_SIZE));
	}

	private static List<String> loadQuotes()
	{
		final ArrayList<String> result = new ArrayList<>();
		for (String file : FILES) {
			result.addAll(loadQuotesFile(PREFIX + file));
		}
		return result;
	}

	private static Collection<String> loadQuotesFile(@Nonnull String path)
	{
		final ArrayList<String> result = new ArrayList<>();

		final InputStream stream = FortuneWheel.class.getResourceAsStream(path);
		final Scanner scanner = new Scanner(stream).useDelimiter("%");
		while (scanner.hasNext()) {
			result.add(scanner.next());
		}
		return result;
	}


}
