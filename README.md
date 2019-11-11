# Missing Persons Twitter Bot #
Twitter Bot for harvesting tweets about South African missing persons. Can be used for any country

## SETTING THINGS UP ##
##### Make sure you have the following. #####
1. Java 8+ installed on you Machine.
2. [Maven](https://maven.apache.org/install.html) Installed (to build dependencies)
3. [Twitter Developer account](https://developer.twitter.com/en/apps)

### LET'S START
- Clone/Download this project and Locate to the projects root folder
- Run this command, to download dependencies:
    ``` 
    mvn install dependency:copy-dependencies 
    ```
- Fetch your Twitter Developer app Keys and Tokens: 
    - make sure your Twiter app has Read and Write permissions.
    - Insert your Keys and Tokens in the `InitConfig()` and `replyToPerson()` methods, in the `App.java` file.
- Change the hashtag of tweets you would like to harvest in `App.java:96`
- All should be ready now, run your app to listen to incoming Tweets :smile:

## Contribution Guidelines ##
- To be updated

> I will be updating this ReadMe to give more clarification on how to set this project up. Should you enconter problems, you can get in touch with me on Twitter: [@JosiahThobejane](https://twitter.com/josiahthobejane) or email JosiahThobejane@gmail.com