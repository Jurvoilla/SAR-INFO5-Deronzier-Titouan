package task2;

import task1.Broker;

abstract class QueueBroker {
	Broker broker;
	
	 QueueBroker(Broker broker) {
		 this.broker = broker;
	}
	 abstract String name();
	 abstract MessageQueue accept(int port);
	 abstract MessageQueue connect(String name, int port);
	}


