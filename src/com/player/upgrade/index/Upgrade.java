package com.player.upgrade.index;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.player.upgrade.Player;
import com.player.upgrade.akka.AkkaRemoteSystem;
import com.player.upgrade.akka.CommandUpgrade;
import com.player.upgrade.akka.Intent;
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

	/**
	 * upgrade start
	 */
	public void start() {
		playerRun();
	}

	private void playerRun() {
		if (!preInit()) {
			return;
		}
		// get update pack file path
		String packPath = ManifestInfo.getInstance().getUpdatePackPath();
		if (Files.exists(Paths.get(packPath))) { // 没有更新包文件
			unZipPack(packPath);
		} else {
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
			noticePlayer(CommandUpgrade.PLAYER_SHUTDOWN, false);
		} catch (Exception e) {
			Logs.error("unZipPack error: " + e.getMessage());
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
		return true;
	}

	public static void backAndReplace() {
		boolean unResult = false;
		UpgradePlayerImpl aImpl = UpgradePlayerImpl.getInstance();
		if (aImpl.backuPlayer()) {
			if (!aImpl.updateReplace()) {
				Logs.error("替换失败!");
				aImpl.rollBackReplace();
			} else {
				unResult = true;
			}
		} else {
			Logs.error("备份失败!");
		}
		// 重启程序
		noticePlayer(CommandUpgrade.PLAYER_RESTARE, unResult);
	}

	private static void noticePlayer(String cmd, boolean unResult) {
		Intent aIntent = new Intent();
		aIntent.setAction(cmd);
		Map<String, Object> aMap = new HashMap<>();
		aMap.put("name", "upgrade");
		aMap.put("unResult", unResult);

		aIntent.addValue(aMap);

		AkkaRemoteSystem.getRemoteRef(Configs.getPlayerAkkaIp(), Configs.getPlayerAkkaPort()).tell(aIntent,
				AkkaRemoteSystem.senderActor);
	}
}
