package twitterbot.Model;

public class SMSFeedback {
    private String tweet_id;
    private String person_handle;
    private String message;
    private String missing_person_name;

    public String getTweet_id() {
        return tweet_id;
    }

    public String getMissing_person_name() {
        return missing_person_name;
    }

    public void setMissing_person_name(String missing_person_name) {
        this.missing_person_name = missing_person_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPerson_handle() {
        return person_handle;
    }

    public void setPerson_handle(String person_handle) {
        this.person_handle = person_handle;
    }

    public void setTweet_id(String tweet_id) {
        this.tweet_id = tweet_id;
    }


}