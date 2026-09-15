package multithreading;

class OtherThread extends Thread {
	public int age = 0;

	public void run() {
		for (int i = 0; i < 5; i++) {
			System.out.println("Child thread");
			age++;
		}

		synchronized (this) {
			this.notify();
		}

	}
}

public class Test6 {

	public static void main(String[] args) throws InterruptedException {
		OtherThread producer = new OtherThread();
		System.out.println("Main thread start " + producer.age);
		producer.start();

		synchronized (producer) {
			producer.wait();
		}
			System.out.println("Main thread end "+ producer.age);
	}
}
