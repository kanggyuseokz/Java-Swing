package coinmockproject.util;

import java.io.*;
import java.util.*;

public class EnvLoader {
	private static final Properties props = new Properties();
	static {
		try (InputStream input = new FileInputStream(".env")) {
			props.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		return props.getProperty(key);
	}
}
