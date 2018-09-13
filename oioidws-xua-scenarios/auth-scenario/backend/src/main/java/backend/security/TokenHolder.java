package backend.security;

public class TokenHolder {
	private static final ThreadLocal<Object> tokenHolder = new ThreadLocal<>();
	
	public static void setToken(Object token) {
		tokenHolder.set(token);
	}
	
	public static Object getToken() {
		return tokenHolder.get();
	}
}
