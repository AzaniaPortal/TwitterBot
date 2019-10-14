package twitterbot.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import twitterbot.*;

public class SendToAPI {
		
	static App mainApp = new App();

	final static String API_END_POINT = "https://thanos.fortresswire.com/api/report";
	static OkHttpClient okHttpClient = new OkHttpClient()
	.newBuilder()
    .connectTimeout(20,TimeUnit.SECONDS)
    .writeTimeout(20,TimeUnit.SECONDS)
    .readTimeout(30,TimeUnit.SECONDS)
	.build();

	/**
	 * 
	 * @param message
	 * @param number
	 */
	public static void sendSMS(String message, String number) {
		//https://tadhackapi.azurewebsites.net/api/v1/public/sendotp?username=gift&password=Neo&apitoken=eff4891f43b24534376147ca02b49dbe
		HttpUrl HttpUrl = new HttpUrl.Builder()
			.scheme("https")
			.host("tadhackapi.azurewebsites.net")
			.addPathSegment("api/v1/public/sendotp")
			.addQueryParameter("username", "Neo")
			.addQueryParameter("password", "Neo")
			.addQueryParameter("apitoken", "eff4891f43b24534376147ca02b49dbe")
			.build();

		// final RequestBody requestBody = new FormBody.Builder()
		// 	.add("smsmessages", message)
		// 	.add("cellPhoneNumbers", number)
		// 	.build();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody requestBody = RequestBody.create(mediaType, "{\n \"smsmessages\": \"" + message+ "\",\n \"cellPhoneNumbers\": \"" + number+ "\"\n}");

		System.out.println("Request body: " + requestBody);
		Request request = new Request.Builder()
			.url(HttpUrl)
			.addHeader("Content-Type", "application/json")
			.post(requestBody)
			.build();
		final Call call = okHttpClient.newCall(request);
		try {
			final Response response = call.execute();
			System.out.println("SMS Response: " + response);
			//close the connection
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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

		// okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    	// okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
    	// okHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
		
		//create a json request body 
		final RequestBody requestBody = new FormBody.Builder()
			.add("tweet_id", Long.toString(tweetId))
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
			.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwcm8iOiJhcGkiLCJpc3MiOiIxIiwiZXhwIjoxNjAyNTg1MzczLCJzdWIiOiIiLCJhdWQiOiIifQ.HG9gE_VEqOOJdVDudNlXArgLnN7qoJmonn9Qbe08H3Q")
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