package chat.util;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageIdFountainAtomicIntImpl implements MessageIdFountain {

	private AtomicInteger messageIdFountain = new AtomicInteger();

	public int getNextId() {
		return messageIdFountain.incrementAndGet();
	}
	
	public int getLastId() {
		return messageIdFountain.get();
	}
	
}
