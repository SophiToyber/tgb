# telegram-api-service
## Installing

You can install telegram-bot with:

```
$ git clone https://gitlab.com/messanger-core-group/telegram-api-service.git
```

## Getting started
#### In Eclipse

Click on `File -> Import -> Existing Gradle Project`. Indicate your project root directory and click finish.

Then change application run configuration:

- Select **Run** → **Run Configurations** from the top-level menu
- Right click on **Spring Boot App** → **New Configuration** 
 In **Project** drop-down menu select `telegram-api-service` .
- Set **Main type** as `io.crypto.beer.telegram.bot.CryptobeerApplication` .
- Set **Profile** as `dev`, then click **Apply** and **Run** . 

## Set up test environment

### Create a Telegram bot

In order to test `telegram-api-service`, you will need a bot. Find [@BotFather](https://t.me/BotFather) in Telegram, then
use the `/newbot` command to create a new bot. Follow all the steps within [@BotFather](https://t.me/BotFather) conversation, then get the authorization token of your new bot.

### Disable decryption

*telegram-api-service* (`POST /bots` endpoint) expects encrypted credentials and will try
to decrypt them. For testing only purposes, you can disable decryption temporarily.<br/><br/>
Go to *io.bf.messengers.telegram.api.transformer.TelegramCredentialsTransformer* and comment out the lines which are
handling decryption. Warning: Do not commit these changes!

### Set up `Postman`

[Postman](https://www.postman.com/) will be very helpful while testing, although `curl` should be fine too.

### Set up `Father Bot`

You will need to set up and run [Father Bot](https://gitlab.com/messanger-core-group/father-bot) as *telegram-api-service* depends on it.


### Example request
A request should have this signature:
```
POST http://localhost:8086/bots
Content-Type: application/json
```
In `Postman`, create a new request, select POST method and then set `Body -> raw -> JSON`.<br/><br/>
Example JSON:
```
{
    "botId": "botId1",
    "token": "<token>",
    "username": "<bot-username>"
}
```