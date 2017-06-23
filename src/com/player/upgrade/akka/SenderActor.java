package com.player.upgrade.akka;

import java.util.Iterator;
import java.util.Map;

import com.player.upgrade.backupReplace.UpgradePlayerImpl;
import com.player.upgrade.detect.ManifestInfo;
import com.player.upgrade.index.Upgrade;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class SenderActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		System.out.println("upgrade SenderActor onReceive: " + message);

		if (message instanceof Intent) {
			Intent aIntent = (Intent) message;

			switch (aIntent.getAction()) {
			case CommandUpgrade.PLAYER_SHUTDOWN:
				// got close player result; backup and replace
				Upgrade.backAndReplace();
				break;
			case CommandUpgrade.PLAYER_RESTARE:
				// got restart player result; clear
				Iterator<Map<String, Object>> aIterator = aIntent.getValue().iterator();
				while (aIterator.hasNext()) {
					System.out.println("map keySet: " + aIterator.next().keySet());
				}
				clear(false);
				break;
			}
		}

		getSender().tell("I Got it!---", ActorRef.noSender());
	}

	private void clear(boolean deletePackFile) {
		if (deletePackFile) {
			UpgradePlayerImpl.getInstance().cleanJunkFiles(ManifestInfo.getInstance().getUpdatePackPath());
		} else {
			UpgradePlayerImpl.getInstance().cleanJunkFiles();
		}
	}
}
