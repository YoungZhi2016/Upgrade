package com.player.upgrade.index;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import com.player.upgrade.Player;
import com.player.upgrade.backupReplace.UpgradePlayerImpl;
import com.player.upgrade.detect.Detect;
import com.player.upgrade.detect.ManifestInfo;
import com.player.upgrade.utils.Configs;
import com.player.upgrade.utils.Logs;
import com.player.upgrade.utils.MD5Util;
import com.player.upgrade.utils.ZIPUtil;

public class Upgrade {

	private Upgrade() {
	}

	public static Upgrade getInstance() {
		return UpgradeHolder.aUpgrade;
	}

	private static class UpgradeHolder {
		private static Upgrade aUpgrade = new Upgrade();
	}

	private static void playerRun() {
		if (!preInit()) {
			return;
		}
		Path uPFPath = Configs.getUpdatePackFilePath();

		if (Files.exists(uPFPath)) { // 没有更新包文件
			unZipPack(uPFPath.toString());
		} else {
			// DownloadServer.getInstance().down(ManifestInfo.getInstance().getDownLoadUrl(),
			// Configs.getPackName());
			Logs.info("找不到更新包!");
		}
	}

	public static boolean unZipPack(String zipFilePath) {
		boolean unResult = false;
		// 校验文件完整性
		try {
			String md5 = ManifestInfo.getInstance().getMD5();
			if (md5 == null || !md5.equals(MD5Util.getMd5ByFile(new File(zipFilePath)))) {
				Logs.error("md5值不一致!");
				return unResult;
			}

			// unZip
			boolean unZ = ZIPUtil.unZip(zipFilePath, Configs.getUpdatePackUnDirPath().toString());
			if (!unZ) {
				Logs.error("解压失败!");
				return unResult;
			}

			// 关闭程序
			Player.closePlayer();

			UpgradePlayerImpl aImpl = UpgradePlayerImpl.getInstance();
			if (aImpl.backuPlayer()) {
				if (aImpl.updateReplace()) {
					// 重启程序
					unResult = Player.restartPlayer();
				} else {
					Logs.error("替换失败!");
					aImpl.rollBackReplace();
				}
			} else {
				Logs.error("备份失败!");
			}
		} catch (Exception e) {
			Logs.error("unZipPack error: " + e.getMessage());
		} finally {
			over(unResult);
		}
		return unResult;
	}

	private static boolean preInit() {
		// 升级程序的清单文件是否存在
		String manifestFilePath = Configs.getManifestFilePath();
		if (manifestFilePath == null || !Detect.detectManifestFilExist(manifestFilePath)) {
			Logs.error("更新程序的清单文件找不到!");
			return false;
		}

		ManifestInfo aInfo = ManifestInfo.getInstance();
		if (aInfo == null || aInfo.getUpgradeVersion() == null
				|| aInfo.getUpgradeVersion().equals(Player.getCurrentVersion())) {
			Logs.info("清单文件解析失败或版本一致！");
			return false;
		}

		if (!Detect.detectDiskSpace(1024)) {
			Logs.error("磁盘空间小于1G,清理空间再重试!");
			return false;
		}

		if (!Detect.detectNetwork(aInfo.getServerIP())) {
			Logs.error("无法连接到服务器!");
			return false;
		}
		return true;
	}

	/**
	 * upgrade start
	 */
	public void start() {
		Logs.info("Upgrade Start!");
		playerRun();
	}

	/**
	 * upgrade over
	 */
	public static void over(boolean delet) {
		Logs.info("Upgrade over!" + delet);
		// 删除临时文件
		UpgradePlayerImpl.getInstance().cleanJunkFiles(delet);
	}

}
