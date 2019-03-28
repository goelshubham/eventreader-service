package eventreader.eventreader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EventReader {

	public static void main(String[] args) {

		JSONParser parser = new JSONParser();
		JSONArray rootJsonArray = new JSONArray();
		JSONObject rootObject = new JSONObject();
		try {
			rootObject = (JSONObject) parser.parse(new FileReader("/Users/sgoel201/Desktop/input.json"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (rootObject.containsKey("events")) {
			rootJsonArray = (JSONArray) rootObject.get("events");

			for (int i = 0; i < rootJsonArray.size(); i++) {
				JSONObject jsonObject = (JSONObject) rootJsonArray.get(i);

				URI uri = null;
				
				try {
					uri = new URI("http://localhost:9200/events/event/" + (String) jsonObject.get("id"));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				HttpClient client = new DefaultHttpClient();
				HttpPut put = new HttpPut(uri);
				StringEntity entity = new StringEntity(jsonObject.toJSONString(), "UTF-8");
				entity.setContentType("application/json");
				put.setEntity(entity);
				HttpResponse response = null;
				try {
					response = client.execute(put);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(response);
			}

		}
	}

}
