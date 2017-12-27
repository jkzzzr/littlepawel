package funny.weibo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;   
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
public class GetList_Client{

	public static int COUNT = 2748;
	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		//一共只能爬取1200张图片
		for(int i = 29; i <=31; i++){
			System.err.println(i);
			Thread.sleep(100);
			run(i);
			System.err.println("\t"+i);
		}
	}

	public static void run(int page)throws ClientProtocolException, IOException, InterruptedException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("https://weibo.com/aj/photo/popview?ajwvr=6&mid=4188954749867153&pid=006Xr7qLgy1fmtbnwmj7dj30qo1bfwo0&type=0&uid=6375064239&page_id=100101B2094757D06FA5FB4998&pic_like_src=flow_layer&short_url=&"
				+ "page="+page
				+ "&count=40"
				+ "&ref=page&multiuser=1&annotations=filter%3Dfilter%3Dpoi%7C3%2C4&picSrc=photo&pic_objects=4188954748565042%7C0%7C0%7C1042018%3A21063563e021bb7cb928448b297f0295&__rnd=1514351526052");
		get.setHeader("Host", "weibo.com");
		get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		get.setHeader("Cache-Control", "no-cache");
		get.setHeader("ccept-Language", "zh-CN,zh;q=0.8");
		get.setHeader("Connection", "keep-alive");
		get.setHeader("Cache-Control", "no-cache");
		
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		byte[] bs = new byte[1024];
		/*int length = 0;
		while ((length = is.read(bs))>0){
			String string = new String(bs);
			System.out.println(string);
			
			Thread.sleep(1000);
		}
		is.close();*/
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine())!=null){
			sb.append(line);
		}
		br.close();
		readJson(sb.toString());
		System.out.println(response.getProtocolVersion().getProtocol());
	}
	public synchronized static  void  readJson(String fileString) throws IOException{
	//	System.out.println(fileString);
		JSONObject jsonobject = new JSONObject(fileString);
		JSONObject jsonObject2 = (JSONObject) jsonobject.get("data");
		JSONArray jsonArray = (JSONArray) jsonObject2.get("pic_list");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("uid-clear_picSrc", true));
		for (int i=0; i < jsonArray.length();i++){
			++COUNT;
			JSONObject jsonobject3 = (JSONObject) jsonArray.get(i);
//			bw.write(COUNT+"\t"+jsonobject3.get("uid")+"\t"+jsonobject3.get("clear_picSrc")+"\n");
			System.out.println(COUNT+"\t"+jsonobject3.get("uid")+"\t"+jsonobject3.get("clear_picSrc"));
		}
		bw.close();
	}
}
