package test;

public class Main {

	public static void main(String args[]){	
		
		new Thread(()->{ServerApp.start();}).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(()->{ClientApp.start("test");}).start();
		new Thread(()->{ClientApp.start("test");}).start();		
		

				 
	}
}

