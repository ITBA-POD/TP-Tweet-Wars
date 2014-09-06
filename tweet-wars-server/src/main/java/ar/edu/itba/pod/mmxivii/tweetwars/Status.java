package ar.edu.itba.pod.mmxivii.tweetwars;

import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.Nonnull;
import java.io.Serializable;

public class Status implements Serializable
{
	private static final long serialVersionUID = 4200705653115254475L;
	private final long id;
	@Nonnull private final String source;
	@Nonnull private final String text;
	@Nonnull private final String check;

	public Status(long id, @Nonnull String text, @Nonnull String source, @Nonnull String hash)
	{
		this.id = id;
		this.source = source;
		this.text = text;
		check = generateCheck(hash);
	}

	public long getId()
	{
		return id;
	}

	@Nonnull
	public String getSource()
	{
		return source;
	}

	@Nonnull
	public String getText()
	{
		return text;
	}

	@Nonnull
	public String getCheck()
	{
		return check;
	}

	public String generateCheck(@Nonnull String hash)
	{
		//noinspection ConstantConditions
		if (hash == null) throw new NullPointerException();

		return DigestUtils.sha1Hex(String.format("%d-%s-%s-%s", id, text, source, hash ));
	}

	@Override
	public String toString()
	{
		return "Status{" +
				"id=" + id +
				", source='" + source + '\'' +
				", text='" + text + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final Status status = (Status) o;

		return id == status.id && check.equals(status.check) && source.equals(status.source) && text.equals(status.text);
	}

	@Override
	public int hashCode()
	{
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + source.hashCode();
		result = 31 * result + text.hashCode();
		result = 31 * result + check.hashCode();
		return result;
	}
}
