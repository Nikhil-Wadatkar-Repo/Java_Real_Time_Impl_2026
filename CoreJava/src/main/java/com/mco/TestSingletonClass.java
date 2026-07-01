package com.mco;

class MySingleTon {
	static MySingleTon mySingleTon;

	private MySingleTon() {
		if (mySingleTon != null)
			throw new RuntimeException("");
	}

	public static MySingleTon getInstance() {
		if (mySingleTon == null) {
			synchronized (MySingleTon.class) {
				if (mySingleTon == null)
					mySingleTon = new MySingleTon();
			}
		}
		return mySingleTon;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("");
	}

	public MySingleTon readResolve() {
		return this;
	}
}

public class TestSingletonClass {
	public static void main(String[] args) {
		MySingleTon instance1 = MySingleTon.getInstance();
		MySingleTon instance2 = MySingleTon.getInstance();
		System.out.println(instance2.hashCode() == instance1.hashCode());
	}
}
