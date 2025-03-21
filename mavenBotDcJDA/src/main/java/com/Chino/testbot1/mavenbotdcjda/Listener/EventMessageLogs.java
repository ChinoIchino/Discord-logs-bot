package com.Chino.testbot1.mavenbotdcjda.Listener;

//the import as comments are for the initUsersNameAndId
import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.Chino.testbot1.mavenbotdcjda.MavenBotDcJDA;

//import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import net.dv8tion.jda.api.utils.cache.MemberCacheView;

public class EventMessageLogs extends ListenerAdapter {
    //cant get the raw message in the onMessageDelete function so i need to store it in a variable
    private String messageRaw, userName, userId;
    private Message message;


    //init dont work for now :(
    /*
    private void initUsersNameAndId(MemberCacheView cache){
        ArrayList<String> cacheNameAndId = cacheUnpacking(cache.asList());
        for(int i = 0; i < cacheNameAndId.size(); i++){
            if(i % 2 == 0){
                setUserName(cacheNameAndId.get(i).toString());
                System.out.println("Adding the name: " + getUserName());
            }
            else{
                setUserId(cacheNameAndId.get(i).toString());
                System.out.println("Adding the id: " + getUserId());
            }
        }
    }
    */

    //update the this.message and the this.messageRaw
    private void setMessage(Message message){
        if(message.getContentRaw() == ""){
            //System.out.println("\nmessage.getContentRaw() = " + message.getContentRaw().toString() + "\nthis.messageRaw = " + this.messageRaw + "\nin 1st if");
            this.messageRaw = "[Attachment]";
            this.message = message;
        }
        else{
            //System.out.println("\nmessage.getContentRaw() = " + message.getContentRaw().toString() + "\nthis.messageRaw = " + this.messageRaw + "\nin else");
            this.messageRaw = message.getContentRaw().toString();
            this.message = message;
        }
    }

    private boolean isGif(Message message){
        if(message.getContentRaw().equals(this.messageRaw)){
            return true;
        }
        return false;
    }

    private void initUser(User user){
        this.userName = user.getName();
        this.userId = user.getId();
    }

    //this was used for the initUsersNameAndId, but for now its useless
    /*
    private ArrayList<String> cacheUnpacking(List<Member> cacheList){
        ArrayList<String> cacheUnpackingList = new ArrayList<>();
        String tempSplitName, tempSplitId = "";

        for(int i = 0; i < cacheList.size(); i++){
            //bunch of split to get the name and the id of everyone in the MemberCache
            tempSplitId = cacheList.get(i).toString().split("User:")[1];
            tempSplitName = tempSplitId.split("\\(id=")[0];
            tempSplitId = tempSplitId.split("\\(id=")[1];
            tempSplitId = tempSplitId.split("\\),")[0];

            cacheUnpackingList.add(tempSplitName);
            cacheUnpackingList.add(tempSplitId);
        }
        return cacheUnpackingList;
    }
    */

    //get activated when a message is sent in the group
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){

        //if the author of the message isnt the bot himself (to prevent a infinit loop)
        if(!(event.getAuthor().getName().equals("test_bot_1_JDA"))){
            initUser(event.getAuthor());
            setMessage(event.getMessage());

            //ask bot to send message to the log channel with all the information created before
            //sendMessage need a TextChannel var and not a Channel
            MavenBotDcJDA.getLogChan().sendMessage(
                this.userName + " (id= " + this.userId + ") sent: " + this.messageRaw 
                + "\nAt: " + MavenBotDcJDA.getWhenCreated(event.getMessage().getTimeCreated().atZoneSameInstant(ZoneId.systemDefault())) 
                + "\nIn the channel: " + event.getChannel().getAsMention()
                + "\nId: " + event.getMessageId()
                 ).queue();
        }
    }

    //this event function is very limited with which information i can get based on the event, so i need to use mainly variables that are set in the onMessageReceived function
    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event){

        if(!(this.userName.equals("test_bot_1_JDA"))){
                MavenBotDcJDA.getLogChan().sendMessage(
                    "The message: " + this.messageRaw + " from the user: " + this.userName + " (id= " + this.userId + ") have been deleted! "
                    + "\nIn the channel: " + event.getChannel().getAsMention()
                    + "\nId: " + event.getMessageId()
                    ).queue();
        }
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event){

        if(!(event.getAuthor().getName().equals("test_bot_1_JDA"))){
            //gifs can activate the onMessageUpdate event the moment they are sent, so this if statement is to bypass that problem
            if(this.message != null && !(this.isGif(event.getMessage()))){
                MavenBotDcJDA.getLogChan().sendMessage(
                    this.userName + " (id= " + this.userId + ") modified his message! " 
                    + "\nFrom: " + this.messageRaw + "\nInto: " + event.getMessage().getContentRaw()
                    + "\nIn the channel: " + event.getChannel().getAsMention()
                    + "\nId: " + event.getMessageId()
                    ).queue();
            }
            System.out.println(
                "The update event have been activated \nisGif() = " + this.isGif(event.getMessage())
                + "\nthis.messageRaw = " + this.messageRaw 
                + "\nevent.getMessage() = " + event.getMessage().getContentRaw()
            );

            //gifs can activate the onMessageUpdate event the moment they are sent, so this if statement is to bypass that problem

            setMessage(event.getMessage());
        }

            else{
                setMessage(event.getMessage());

                MavenBotDcJDA.getLogChan().sendMessage(
                    this.userName + " (id= " + this.userId + ") modified his message! " 
                    + "\nFrom: " + this.messageRaw + "\nInto: " + event.getMessage().getContentRaw()
                    + "\nIn the channel: " + event.getChannel().getAsMention()
                    + "\nId: " + event.getMessageId()
                    ).queue();
            }
    }

    //get activated when a reaction is added to a message 
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event){

        MavenBotDcJDA.getLogChan().sendMessage(
            this.userName + " added a emoji: " + event.getEmoji().getName() 
            + "\nOn the message: " + this.messageRaw
            + "\nIn the channel: " + event.getChannel().getAsMention()
            + "\nId: " + event.getMessageId()
            ).queue();
    }

    //get activated when a reaction is deleted from a message
    //exactly the same code that for the onMessageReactionAdd
    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event){

        MavenBotDcJDA.getLogChan().sendMessage(
            this.userName + " removed a emoji: " + event.getEmoji().getName() 
            + "\nOn the message: " + this.messageRaw
            + "\nIn the channel: " + event.getChannel().getAsMention()
            + "\nId: " + event.getMessageId()
            ).queue();
    }
}
