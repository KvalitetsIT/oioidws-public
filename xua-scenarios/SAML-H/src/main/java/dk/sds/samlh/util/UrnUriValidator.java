package dk.sds.samlh.util;

import java.net.URI;
import java.util.regex.Pattern;

public class UrnUriValidator {
	public final static Pattern URN_PATTERN = Pattern.compile("^urn:[a-z0-9][a-z0-9-]{0,31}:([a-z0-9()+,\\-.:=@;$_!*']|%[0-9a-f]{2})+$", Pattern.CASE_INSENSITIVE);

	public static boolean validateURI(String uri) {
		final URI u;

		try {
			u = URI.create(uri);
		}
		catch (Exception e1) {
			return false;
		}

		return "http".equals(u.getScheme()) || "https".equals(u.getScheme());
	}

	public static boolean validateURN(String urn) {
		return URN_PATTERN.matcher(urn).matches();
	}
}
