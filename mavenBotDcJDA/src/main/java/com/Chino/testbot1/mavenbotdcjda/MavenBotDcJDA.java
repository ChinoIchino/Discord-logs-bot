package com.Chino.testbot1.mavenbotdcjda;

//import from java lib
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import com.Chino.testbot1.mavenbotdcjda.Commands.SlashCommandsLibrary;
import com.Chino.testbot1.mavenbotdcjda.Listener.EventChannelLogs;
//import the EventListener.java from listener file
import com.Chino.testbot1.mavenbotdcjda.Listener.EventMessageLogs;
import com.Chino.testbot1.mavenbotdcjda.Listener.EventRoleLogs;
import com.Chino.testbot1.mavenbotdcjda.Listener.EventUserEntry;

//get .env 
import io.github.cdimascio.dotenv.Dotenv;

//import from JDA
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class MavenBotDcJDA {
    private final ShardManager shardManager;
    private final Dotenv config;
    private static TextChannel logChan = null;
    private static ArrayList<TextChannel> safeChanList = new ArrayList<>();
    //private Channel logChan;

    public MavenBotDcJDA() throws LoginException{
        //get the token in the .env file
        //config = Dotenv.configure().ignoreIfMissing().load();
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        //create a shard named builder wit the token
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        //set a activity for the bot
        builder.setActivity(Activity.playing("On Chino Patience"));
        //open gateway to be able listen for messages, guild join, leave, etc... that are used in triggers
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);

        shardManager = builder.build();

        //add listeners
        shardManager.addEventListener(new SlashCommandsLibrary(), new EventMessageLogs(), new EventRoleLogs(), new EventChannelLogs(), new EventUserEntry());
    }

    //gets
    public ShardManager getShardManager(){
        return shardManager;
    }
    public Dotenv getConfig(){
        return config;
    }

    //static get and set because its a general variable that is shared between all the events
    public static void setLogChan(TextChannel channel){
        logChan = channel;
    }
    public static TextChannel getLogChan(){
        return logChan;
    }

    //functions about the safeChannelList
    public static ArrayList<TextChannel> getSafeChanList(){
        return safeChanList;
    }
    
    public static boolean isInSafeChanList(TextChannel toFind){
        for(int i = 0; i < getSafeChanList().size(); i++){
            if(toFind.equals(getSafeChanList().get(i))){
                return true;
            }
        }
        return false;
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

    public static void clearLogChannel(TextChannel logChan){
        //get the position of the channel
        int position = logChan.getPositionInCategory();
        //create a new channel with the same name and the same position
        logChan.createCopy().setPosition(position).queue();
        //delete the original (with all the logs)
        logChan.delete().queue();

        //set the new log channel
        setLogChan((TextChannel) logChan.getGuild().getChannels().get(position));
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
