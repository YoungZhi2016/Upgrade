package com.player.upgrade.akka;

import akka.actor.UntypedActor;

public class RemoteActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Throwable {
		System.out.println("RemoteActor onReceive: " + message);
		getSender().tell("收到消息：", getSelf());
	}

}
