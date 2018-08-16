package nyc;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import com.github.nyc.portal.util.HttpUtils;

public class DogTest implements Runnable{
	
	HttpUtils httpUtils=new HttpUtils();
	
	
	private String threadName;
	 
	public DogTest(String name) {
		this.threadName=name;
	}

	@Override
	public void run() {
		httpUtils.initialize();
		for (int i = 1; i <= 1000; i++) {
			String s;
			try {
				s = httpUtils.get("http://127.0.0.1");
				//System.out.println(s);
				//Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		Long start=System.currentTimeMillis();
		for(int i=0;i<10;i++) {
			DogTest dog=new DogTest("a"+i);
			Thread t=new Thread(dog);
			t.start();
		}
		Long end=System.currentTimeMillis();
		System.out.println("total:"+(end-start));
	
	}

}
