package com.player.upgrade.akka;

import java.time.LocalTime;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.player.upgrade.utils.Configs;

import akka.actor.ActorSelection;

public class Test {

	private static ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();

	public static void main(String[] args) {
		// testAkk();

		System.out.println(Configs.getPluginsAkkaIp());
		System.out.println(Configs.getPluginsAkkaPort());
	}

	private static void testAkk() {
		AkkaRemoteSystem.init();
		ActorSelection a = AkkaRemoteSystem.getRemoteRef("172.16.0.53", 6001);
		final TimerTask aTask = new TimerTask() {

			@Override
			public void run() {
				System.err.println("run--time: " + LocalTime.now());
				a.tell("现在的时间吗？ " + LocalTime.now(), AkkaRemoteSystem.senderActor);
			}
		};
		SERVICE.scheduleAtFixedRate(aTask, 1, 10, TimeUnit.SECONDS);
	}

}
