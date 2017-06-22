package com.player.upgrade.download;

import com.player.upgrade.utils.Configs;

/**
 * 
 * @author ywkj
 *
 *         Download Strategy
 */
public interface DownloadStrategy {

	/**
	 * updatePack directory
	 */
	String UPGRADE_DIR = Configs.getUpdatePackFileDirPath().toString();

	String FTP_WAY = "FTP";

	String HTTP_WAY = "HTTP";

	String UNKNOWN_WAY = "UNKNOWN_WAY";

}
