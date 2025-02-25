/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.Chino.testbot1.mavenbotdcjda;

//import from java lib
import java.io.IOException;
import javax.security.auth.login.LoginException;

//import the EventListener.java from listener file
import com.Chino.testbot1.mavenbotdcjda.Listener.EventMessageLogs;
import com.Chino.testbot1.mavenbotdcjda.Listener.EventRoleLogs;

//get .env 
import io.github.cdimascio.dotenv.Dotenv;

//import from JDA
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class MavenBotDcJDA {
    private final ShardManager shardManager;
    private final Dotenv config;

    public MavenBotDcJDA() throws LoginException{
        //get the token in the .env file
        config = Dotenv.configure().ignoreIfMissing().load();
        String token = config.get("TOKEN");
        //create a shard named builder wit the token
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        //set a activity for the bot
        builder.setActivity(Activity.playing("On Chino Patience"));
        //open gateway to be able listen for messaage, guild join,... that are used in triggers
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);
        shardManager = builder.build();
        
        //shardManager.addEventListener(new EventListener(), new CommandsManager());
        //add listeners
        shardManager.addEventListener(new EventMessageLogs(),new EventRoleLogs());
    }

    //gets
    public ShardManager getShardManager(){
        return shardManager;
    }
    public Dotenv getConfig(){
        return config;
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
