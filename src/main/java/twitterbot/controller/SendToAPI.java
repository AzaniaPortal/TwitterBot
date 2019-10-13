package twitterbot.controller;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import twitterbot.*;

public class SendToAPI {
		
	final static String API_END_POINT = "https://thanos.fortresswire.com/api/report";
	static OkHttpClient okHttpClient = new OkHttpClient();
	static App mainApp = new App();
	
   /**
	* 
	* @param name name of the lost person
	* @param reporter twitter handle of the person who reported the missing person
	* @param location last known location of the missing person
	* @param date last seen date of the missing person
	* @param image image of the image
	* @param description description of the person who went missing
	* @throws IOException
	*/
		
	public static void sendData(String name, String reportersName ,String reporter, String location, String date, String image, String description, long tweetId) throws IOException {

		//create a json request body 
		final RequestBody requestBody = new FormBody.Builder()
			.add("name", name)
			.add("reporter", "@"+reporter)
			.add("location", location)
			.add("date", date)
			.add("image", image)
			.add("description", description)
			.build();	

		//build the request
		final Request request = new Request.Builder()
			.url(API_END_POINT)
			.post(requestBody)
			.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwcm8iOiJhcGkiLCJpc3MiOiIxIiwiZXhwIjoxNjAyNTM5NzExLCJzdWIiOiIiLCJhdWQiOiIifQ.xUmfdDYOJ39VKFhOOfcbnd3SngTNNPHnlG4Rk7qosaM")
			.addHeader("Content-Type", "application/json")
			.build();

		//make the call to the api
		final Call call = okHttpClient.newCall(request);
		final Response response = call.execute();
		switch(response.code()) 
		{
			case 200: 
				mainApp.reply(tweetId, reporter, 200, "success", name);
				break;
			case 422: 
			mainApp.reply(tweetId, reporter, 200, "success", name);
			break;
			case 404:
				System.out.println("Our API is currently down");
				break;
			default:
				mainApp.reply(tweetId, reporter, 404, "failed", name);
				break;
		}

		//close connection to avoid memory leak
		response.close();
	}
}