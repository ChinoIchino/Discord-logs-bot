/*
 * I still want to add
 * Guild related
 *      GuildJoinEvent
 *      GuildLeaveEvent
 *      GuildBanEvent
 *      GuildUnbeanEvent
 * 
 * Channel related
 *      GenericChannelUpdateEvent
 *          ChannelUpdatePositionEvent
 *          ChannelUpdateRegionEvent ? (need to see what is that)
 */

package com.Chino.testbot1.mavenbotdcjda.Listener;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventMessageLogs extends ListenerAdapter {
    //cant get the raw message in the onMessageDelete function so i need to store it in a variable
    private String messageRaw;
    private Message message;

    private String getEventTime(ZonedDateTime messageAtTime){
        if(messageAtTime.getMinute() < 10){
            return 
            messageAtTime.getDayOfMonth() + "/" + messageAtTime.getMonthValue() + "/" + messageAtTime.getYear() + " "
            + messageAtTime.getHour() + "h0" + messageAtTime.getMinute() + " ("
            + messageAtTime.getOffset() + " " + messageAtTime.getZone() + ")";
        }
        return 
            messageAtTime.getDayOfMonth() + "/" + messageAtTime.getMonthValue() + "/" + messageAtTime.getYear() + " "
            + messageAtTime.getHour() + "h" + messageAtTime.getMinute() + " ("
            + messageAtTime.getOffset() + " " + messageAtTime.getZone() + ")";
    }
    
    //get activated when a message is sent in the group
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        //if the author of the message isnt a bot (to prevent infinit loops, where the bot take his own message as events)
        if(!(event.getAuthor().getName().equals("test_bot_1_JDA"))){
            this.messageRaw = event.getMessage().getContentRaw();
            this.message = event.getMessage();
            //ask bot to send message to the log channel with all the information created before
            event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
                event.getAuthor().getName() + " sent: " + event.getMessage().getContentRaw() 
                + "\nAt: " + getEventTime(event.getMessage().getTimeCreated().atZoneSameInstant(ZoneId.systemDefault())) 
                + "\nIn the channel: " + event.getChannelType() + ":" + event.getChannel().getName()
                + "\nId: " + event.getMessageId()
                 ).queue();
        }
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event){
        //this event function is very limited with which information i can get based on the event, so i need to use mainly variables that are set in the onMessageReceived function
        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
        "The message: " + this.messageRaw + " have been deleted! "
        + "\nAt: " + getEventTime(this.message.getTimeCreated().atZoneSameInstant(ZoneId.systemDefault())) 
        + "\nIn the channel: " + event.getChannelType() + ":" + event.getChannel().getName() 
        + "\nId: " + event.getMessageId()
        ).queue();
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event){
        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            event.getAuthor() + " modified his message! " 
            + "\nAt: " + getEventTime(this.message.getTimeCreated().atZoneSameInstant(ZoneId.systemDefault()))
            + "\nFrom: " + this.messageRaw + "\nInto: " + event.getMessage().getContentRaw()
            + "\nIn the channel: " + event.getChannelType() + ":" + event.getChannel().getName()
            + "\nId: " + event.getMessageId()
            ).queue();

            //update messageRaw and message with the new values
            this.messageRaw = event.getMessage().getContentRaw();
            this.message = event.getMessage();
    }

    //get activated when a reaction is added to a message 
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event){
        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            event.getUser().getName() + " added a emoji: " + event.getEmoji().getName() 
            + "\nOn the message: " + this.messageRaw
            + "\nIn the channel: " + event.getChannelType() + ":" + event.getChannel().getName()
            + "\nId: " + event.getMessageId()
            ).queue();
    }

    //get activated when a reaction is deleted from a message
    //exactly the same code that for the onMessageReactionAdd
    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event){
        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            event.getUser().getName() + " removed a emoji: " + event.getEmoji().getName() 
            + "\nOn the message: " + this.messageRaw
            + "\nIn the channel: " + event.getChannelType() + ":" + event.getChannel().getName()
            + "\nId: " + event.getMessageId()
            ).queue();
    }
}
