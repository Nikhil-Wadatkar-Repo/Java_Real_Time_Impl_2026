package multithreading;

class MyThread2 implements Runnable {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 1; i <= 5; i++)
			System.out.println("Running child thread");
	}
}

public class Test2 {

	public static void main(String[] args) {
		Thread thread1 = new Thread(new MyThread2());
		thread1.start();
		Thread.currentThread().setPriority(1);
		for (int i = 1; i <= 5; i++)
			System.out.println("Running main thread");

	}

}
