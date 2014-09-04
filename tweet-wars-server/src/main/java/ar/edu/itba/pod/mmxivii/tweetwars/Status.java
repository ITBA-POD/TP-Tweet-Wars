package ar.edu.itba.pod.mmxivii.tweetwars;

import java.io.Serializable;

public class Status implements Serializable
{
	private static final long serialVersionUID = 4200705653115254475L;
	private final long id;
	private final String source;
	private final String text;

	public Status(long id, String source, String text)
	{
		this.id = id;
		this.source = source;
		this.text = text;
	}

	public long getId()
	{
		return id;
	}

	public String getSource()
	{
		return source;
	}

	public String getText()
	{
		return text;
	}
}
