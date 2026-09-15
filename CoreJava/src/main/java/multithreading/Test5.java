package multithreading;

class JoinThread extends Thread {
	public void run() {
		for (int i = 0; i < 5; i++) {
			System.out.println("Child thread");
		}

	}
}

public class Test5 {

	public static void main(String[] args) throws InterruptedException {
		Thread producer = new JoinThread();
		producer.start();
		producer.join();
		for (int i = 0; i < 5; i++) {
			System.out.println("Main thread");
		}
	}
}
