package com.Chino.testbot1.mavenbotdcjda;

//import from java lib
import java.io.IOException;
import java.time.ZonedDateTime;

import javax.security.auth.login.LoginException;

//import the EventListener.java from listener file
import com.Chino.testbot1.mavenbotdcjda.Listener.EventMessageLogs;
import com.Chino.testbot1.mavenbotdcjda.Listener.EventRoleLogs;

//get .env 
import io.github.cdimascio.dotenv.Dotenv;

//import from JDA
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class MavenBotDcJDA {
    private final ShardManager shardManager;
    private final Dotenv config;

    public MavenBotDcJDA() throws LoginException{
        //get the token in the .env file
        //config = Dotenv.configure().ignoreIfMissing().load();
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        //create a shard named builder wit the token
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        //set a activity for the bot
        builder.setActivity(Activity.playing("On Chino Patience"));
        //open gateway to be able listen for messaage, guild join,... that are used in triggers
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);
        //let the bot retrieve all the users in the server (used in the initGetNameAndGetId function in eventMessageLogs)
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        //ask the bot to add everyone to the member cache
        builder.setChunkingFilter(ChunkingFilter.ALL);
        
        shardManager = builder.build();

        
        //shardManager.addEventListener(new EventListener(), new CommandsManager());

        //add listeners new EventMessageLogs(), new EventRoleLogs(), new EventChannelLogs(), new EventUserEntry()
        shardManager.addEventListener(new EventMessageLogs(), new EventRoleLogs(), new EventChannelLogs(), new EventUserEntry());
    }

    //gets
    public ShardManager getShardManager(){
        return shardManager;
    }
    public Dotenv getConfig(){
        return config;
    }

    public static String getWhenCreated(ZonedDateTime event){

        if(event.getMinute() < 10){
            return 
            event.getDayOfMonth() + "/" + event.getMonthValue() + "/" + event.getYear() + " "
            + event.getHour() + "h0" + event.getMinute() + " ("
            + event.getOffset() + " " + event.getZone() + ")";
        }
        return 
            event.getDayOfMonth() + "/" + event.getMonthValue() + "/" + event.getYear() + " "
            + event.getHour() + "h" + event.getMinute() + " ("
            + event.getOffset() + " " + event.getZone() + ")";
    }


    public static void main(String[] args) throws IOException{
        //catching token with LoginException if token is wrong the bot isnt created and the "Error: ..." shows up
        try{
            MavenBotDcJDA bot = new MavenBotDcJDA();
        } catch (LoginException e){
            System.out.println("Error: bot token is invalid!");
        }

    }
}
