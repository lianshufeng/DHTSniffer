package com.fast.dht.torrent.util;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * jndi包工具
 * 
 * @作者 练书锋
 * @时间 2018年3月2日
 *
 *
 */
public class LibraryUtil {
	public static void addLibraryDir(String libraryPath) throws IOException {
		try {
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[]) field.get(null);
			for (int i = 0; i < paths.length; i++) {
				if (libraryPath.equals(paths[i])) {
					return;
				}
			}

			String[] tmp = new String[paths.length + 1];
			System.arraycopy(paths, 0, tmp, 0, paths.length);
			tmp[paths.length] = libraryPath;
			field.set(null, tmp);
		} catch (IllegalAccessException e) {
			throw new IOException("Failedto get permissions to set library path");
		} catch (NoSuchFieldException e) {
			throw new IOException("Failedto get field handle to set library path");
		}
	}

	public static void addLibraryDir() throws IOException {
		addLibraryDir(LibraryUtil.class.getResource("/").getPath());
	}

}
