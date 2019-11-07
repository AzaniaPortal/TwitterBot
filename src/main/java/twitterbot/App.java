package twitterbot;

import java.io.IOException;
import twitter4j.FilterQuery;
import twitter4j.MediaEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.conf.ConfigurationBuilder;
import twitterbot.controller.SMSController;
import twitterbot.controller.SendToAPI;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusUpdate;
import twitter4j.TwitterStreamFactory;

public class App {

	Twitter twitter;
	TwitterFactory twitterFactory;
	TwitterStream twitterStream;
	
	public static void main(String[] args) {
		App app = new App();
		app.initConfig();
		//start function to listen on tweets
		app.listenOnTweets();
		//start Spring context
		SMSController.main(args);
	}

	/**
	 * build auth to the twitter api
	 */
	public void initConfig() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
	
		configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey("**")
				.setOAuthConsumerSecret("**")
				.setOAuthAccessToken("**")
				.setOAuthAccessTokenSecret("**");
		twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
	}

	/**
	 * Listen to tweets on twitter
	 */
	public void listenOnTweets() {
		StatusListener statusListener = new StatusListener() {

			@Override
			public void onException(Exception ex) {
				System.out.println("ERRRORR Tweet Exception: " + ex.getMessage());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("LIMIT: " + numberOfLimitedStatuses);
			}

			@Override
			public void onStatus(Status status) {
				String imageUrl = "no-image";
				
				for(MediaEntity me : status.getMediaEntities()){
					if(me.getMediaURLHttps() != null)
						imageUrl = me.getMediaURLHttps();
				}
				
				//process the tweet
				processTweet(status.getText(), status.getUser().getName(), status.getUser().getScreenName(), imageUrl, status.getId());
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				System.out.println("STALLING: " + warning);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("USER: " + userId + " : " + upToStatusId);
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("DELETING STATUS: " + statusDeletionNotice);
			}
		};

		// the magic is here..a bit of it
		FilterQuery filterQuery = new FilterQuery();
		//only harvest for tweets containing this hashtag
		String keyWord = "#ThanosTadhack2019";
		filterQuery.track(keyWord);
		twitterStream.addListener(statusListener);
		twitterStream.filter(filterQuery);

	}

	/**
	 * 
	 * @param tweet the tweet body
	 * @param reporter twitter handle
	 * @param imageLink image link to the missing persons profile
	 */
	public void processTweet(String tweet, String reportersName ,String reporter, String imageLink, long tweetId) {

		String name, location, date, description;
		//String tweetSample = "John Doe-Fairland, Randburg-2019/10/12-He was wearing a white suit, sources say he might be john wicks evil brother #ThanosTadhack2019".replace("#ThanosTadhack2019", "");

		String tweet_data[] = tweet.split("-");
		name = tweet_data[0];
		location = tweet_data[1];
		date = tweet_data[2].replaceAll("/", "-");
		description = tweet_data[3];

		try {
			if(location == null) {location = "South Africa";}
			SendToAPI.sendData(name, reportersName ,reporter, location, date, imageLink, description, tweetId);
			
		} catch (IOException e) {		
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param inReplyToStatusId
	 * @param usersTwitterHandle
	 * @param responseCode
	 * @param responseStatus
	 * @throws TwitterException
	 */
	public void reply(long inReplyToStatusId, String usersTwitterHandle, int responseCode, String responseStatus, String missingPersonName)
	{
		if(responseCode == 200 && responseStatus.equals("success")) 
		{
			replyToPerson(inReplyToStatusId, "Hey @" + usersTwitterHandle  + ", " + missingPersonName + " has been reported :) Get their missing report link here:");
		} else if (responseCode == 200 && responseStatus.equals("failed")) 
		{
			replyToPerson(inReplyToStatusId, "Hi @" + usersTwitterHandle + ", " + missingPersonName + " has already been reported sorry.");
		} else if(responseCode == 200 && responseStatus.equals("feedback")) {
			replyToPerson(inReplyToStatusId, "Good News!!! @" + usersTwitterHandle + ", " + missingPersonName + " has been found :), love them better");
		} 
		else {
			replyToPerson(inReplyToStatusId, "Hi @" + usersTwitterHandle + ", something has went wrong");
		}
	}

	/**
	 * 
	 * @param inReplyToStatusId
	 * @param usersTwitterHandle
	 */
	public void replyToPerson(long inReplyToStatusId, String reply_text) {

		//redundant auth rebuild, will be removed
		ConfigurationBuilder configurationBuilderTwitterFactory = new ConfigurationBuilder();
		configurationBuilderTwitterFactory.setDebugEnabled(true).setOAuthConsumerKey("**")
		.setOAuthConsumerSecret("**")
		.setOAuthAccessToken("**")
		.setOAuthAccessTokenSecret("**");

		twitterFactory = new TwitterFactory(configurationBuilderTwitterFactory.build());
		twitter = twitterFactory.getInstance();
		StatusUpdate statusReply = new StatusUpdate(reply_text);
		statusReply.setInReplyToStatusId(inReplyToStatusId);
		
		try {
			Status status = twitter.showStatus(inReplyToStatusId);
			//reply to the tweet
			Status reply = twitter.updateStatus(new StatusUpdate(reply_text).inReplyToStatusId(status.getId()));
			//like the tweet
			status = twitter.createFavorite(inReplyToStatusId);
			//twitter.updateStatus(statusReply);
		} catch (TwitterException ex) {
			ex.printStackTrace();
		}
	}
}