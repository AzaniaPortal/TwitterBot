package twitterbot.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import twitterbot.App;
import twitterbot.Model.SMSFeedback;

/**
 * Send feedback to the original reporter tweet when the person is found.
 */
@SpringBootApplication
@RequestMapping("/api")
public class SMSController
{

	static App mainApp = new App();
	@RequestMapping(value = "/found-person", method = RequestMethod.POST, consumes="application/json")
	/**
	 * Listen for feedback from the image recognition system.
	 * @param userInfo
	 * @return
	 */
	public void givePersonFoundFeedback(@RequestBody SMSFeedback userInfo) {
		//give people feedback
		mainApp.reply(Long.parseLong(userInfo.getTweet_id()) , userInfo.getPerson_handle(), 200, "feedback", userInfo.getMissing_person_name());
	}
	

	public static void main(String[] args){
		SpringApplication.run(SMSController.class, args);
	}
}