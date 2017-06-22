package com.player.upgrade.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 
 * @author ywkj config
 *
 */
public class Configs {

	private static ConfigUtil aApllicationConfig = new ConfigUtil();
	private static ConfigUtil aConfigPlayer = new ConfigUtil("akka.player.conf");
	private static ConfigUtil aConfigUpgrade = new ConfigUtil("akka.upgrade.conf");

	/**
	 * ManifestFile DIR path
	 */
	public static Path getManifestFileDirPath() {
		return getPlayerDirPath().resolve("upgrade");
	}

	/**
	 * ManifestFile name
	 */
	public static String getManifestFileName() {
		return "ManifestFile.json";
	}

	/**
	 * @return manifest file path
	 */
	public static String getManifestFilePath() {
		return getManifestFileDirPath().resolve(getManifestFileName()).toString();
	}

	/**
	 * @return backup directory or file
	 */
	public static Set<String> getBackupDirFile() {
		String back = aApllicationConfig.getString("backup_dir_file", null);
		Set<String> set = null;
		if (back != null) {
			set = new HashSet<>();
			StringTokenizer aTokenizer = new StringTokenizer(back, ",");
			while (aTokenizer.hasMoreTokens()) {
				set.add(aTokenizer.nextToken());
			}
		}
		return set;
	}

	/**
	 * Player DIR String
	 */
	public static String getPlayerDirString() {
		return aApllicationConfig.getString("player_path", ConfigUtil.getRootPath());
	}

	/**
	 * player Path
	 */
	public static Path getPlayerDirPath() {
		return Paths.get(getPlayerDirString());
	}

	/**
	 * player Parent Path
	 */
	public static Path getPlayerParentDirPath() {
		return getPlayerDirPath().getParent();
	}

	/**
	 * player Backup DIR Path
	 */
	public static Path getBackupDirPath() {
		return getPlayerParentDirPath().resolve("playerBackupDir");
	}

	/**
	 * @return update pack unzip DIR
	 */
	public static Path getUpdatePackUnDirPath() {
		return getPlayerParentDirPath().resolve("playerUpdatePackUnZip");
	}

	// /**
	// * @return update pack file DIR
	// */
	// public static Path getUpdatePackFileDirPath() {
	// return getPlayerParentDirPath().resolve("playerUpdatePack");
	// }
	//
	// /**
	// * @return update pack file name
	// */
	// public static Path getUpdatePackFilePath() {
	// return getUpdatePackFileDirPath().resolve(getPackName());
	// }
	//
	// public static String getPackName() {
	// return "playerUpdatePack.zip";
	// }

	/*----------------------------------AKKA-Configuration-----------------------------------------------------------*/

	public static int getUpgradeAkkaPort() {
		return aConfigUpgrade.getInt("akka.remote.netty.tcp.port", 6002);
	}

	public static String getUpgradeAkkaIp() {
		return aConfigUpgrade.getString("akka.remote.netty.tcp.hostname", "127.0.0.1");
	}

	public static int getPlayerAkkaPort() {
		return aConfigPlayer.getInt("akka.remote.netty.tcp.port", 6000);
	}

	public static String getPlayerAkkaIp() {
		return aConfigPlayer.getString("akka.remote.netty.tcp.hostname", "127.0.0.1");
	}

}
