package com.player.upgrade.index;

public class UpgradeIndex {

	public static void main(String[] args) {
		// 启动更新程序
		// Upgrade.getInstance().start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				System.err.println("    启动更新程序     ");
				// 启动更新程序
				Upgrade.getInstance().start();
			}
		}).start();
	}
}
