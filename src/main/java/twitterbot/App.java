package twitterbot;


import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStreamFactory;

public final class App {
		Twitter twitter;
		TwitterFactory twitterFactory;
		TwitterStream twitterStream;

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
			App app = new App();
			app.initConfig();
			app.listenOnTweets();

    }

		public void initConfig() {
			ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
			configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey("5aQQf9IJWNZBv3qa1Bu7YeYzg")
							.setOAuthConsumerSecret("NJbAHyOH2guZ7VQL0b8RkWfaru7A2QismCUJTCoG5L0rYFGfvR")
							.setOAuthAccessToken("934531542659067905-ntEVbyIyXvlEE904TJcgKCsMYyBiwS6")
							.setOAuthAccessTokenSecret("CxwS47IUc3wbXQryUus6qrLrJdXJ1iu2gnHoT0PqTDQQx");
			twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
		}

    public void listenOnTweets()
    {
       StatusListener statusListener = new StatusListener(){
       
           @Override
           public void onException(Exception ex) {
               System.out.println("ERRRORR: " + ex);
           }
       
           @Override
           public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("LIMIT: " + numberOfLimitedStatuses);
           }
                  
           @Override
           public void onStatus(Status status) {
                String theStatus = status.getText().toUpperCase();         
                
                System.out.println("\n\n------------- NEW TWEET -----------------");
								System.out.println("USERS INFO:"
								+"User ID: " + status.getUser().getId()    
								+"User Handle: " + status.getUser().getName()
								+"Tweet TimeStamp: " + status.getCreatedAt()
								+"Tweet Id: " + status.getId()
								
								);
                System.out.println("\n\n------------- END TWEET -----------------");
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

       
       //the magic is here..a bit of it
       FilterQuery filterQuery = new FilterQuery();
       String keyWord = "#ThanosTadhack2019";
       filterQuery.track(keyWord);
       twitterStream.addListener(statusListener);
       twitterStream.filter(filterQuery);

    }

}