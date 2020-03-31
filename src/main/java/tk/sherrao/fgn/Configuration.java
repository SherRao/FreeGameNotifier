package tk.sherrao.fgn;

import io.github.cdimascio.dotenv.Dotenv;

public class Configuration {

	private static final Dotenv dotenv = Dotenv.load();
	
	public static final String get(String key) {
		return dotenv.get(key);

	}

}