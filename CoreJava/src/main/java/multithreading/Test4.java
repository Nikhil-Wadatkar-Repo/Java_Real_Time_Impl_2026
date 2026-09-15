package multithreading;

class Producer extends Thread {
	public void run() {
		for (int i = 0; i < 5; i++) {
			System.out.println("Producing item " + i);
			// Producer yields after producing an item
			Thread.yield();
		}
		
	}
}

class Consumer extends Thread {
	public void run() {
		for (int i = 0; i < 9; i++) {
			System.out.println("Consuming item " + i + "--");
			// Consumer yields after consuming an item
			Thread.yield();
		}
	}
}

public class Test4 {

	public static void main(String[] args) {
		Thread producer = new Producer();
		Thread consumer = new Consumer();
		producer.start();
		consumer.start();
	}
}
