package com.player.upgrade.akka;

import akka.actor.UntypedActor;

public class RemoteActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		System.out.println("upgradeRemoteActor onReceive: " + message);
		getSender().tell("upgrade收到消息tell you ：", getSelf());
	}

}
