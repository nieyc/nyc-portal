package nyc;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.client.ClientProtocolException;
import com.github.nyc.portal.util.HttpUtils;

public class App {
	
	public static void main(String[] args) {
		HttpUtils httpUtils=new HttpUtils();
		httpUtils.initialize();
		try {
			for(int i=0;i<10000;i++) {
				String s=	httpUtils.get("http://127.0.0.1");
				System.out.println(s);
				//Thread.sleep(1000);
			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
