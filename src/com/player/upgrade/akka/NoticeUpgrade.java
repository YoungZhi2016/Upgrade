package com.player.upgrade.akka;

import com.player.upgrade.utils.Configs;

public class NoticeUpgrade {

	public static void noticePlugins(Intent aIntent) {
		notice(Configs.getPluginsAkkaIp(), Configs.getPluginsAkkaPort(), aIntent);
	}

	public static void noticePlayer(Intent aIntent) {
		notice(Configs.getPlayerAkkaIp(), Configs.getPlayerAkkaPort(), aIntent);
	}

	public static void notice(String ip, int port, Intent aIntent) {
		AkkaRemoteSystem.getRemoteRef(ip, port).tell(aIntent, AkkaRemoteSystem.senderActor);
	}
}
