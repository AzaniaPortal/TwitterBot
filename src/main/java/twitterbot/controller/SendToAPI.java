package twitterbot.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendToAPI {
		
	final String API_END_POINT = "";
  static OkHttpClient okHttpClient = new OkHttpClient();
	
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
	public static void sendData(String name, String reporter, String location, String date, String image, String description) throws IOException {

		//create a json request body 
		final RequestBody requestBody = new FormBody.Builder()
			.add("name", name)
			.add("reporter", reporter)
			.add("location", location)
			.add("date", date)
			.add("image", image)
			.add("description", description)
			.build();

		//build the request
		final Request request = new Request.Builder()
			.url(API_END_POINT)
			.put(requestBody)
			.build();

		//make the call to the api
		final Call call = okHttpClient.newCall(request);
		final Response response = call.execute();
		System.out.println("The Response: " + response);

	}
}