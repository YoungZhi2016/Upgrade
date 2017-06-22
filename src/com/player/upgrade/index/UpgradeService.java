package com.player.upgrade.index;

import com.player.upgrade.akka.AkkaRemoteSystem;
import com.player.upgrade.utils.Logs;

public class UpgradeService implements Runnable {

	@Override
	public void run() {
		Logs.info("启动更新程序");
		AkkaRemoteSystem.init();
		Upgrade.getInstance().start();
	}

}
