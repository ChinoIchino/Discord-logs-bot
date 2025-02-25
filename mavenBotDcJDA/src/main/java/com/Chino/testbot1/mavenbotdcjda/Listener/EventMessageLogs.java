/*
 * I still want to add
 * Guild related
 *      GuildJoinEvent
 *      GuildLeaveEvent
 *      GuildBanEvent
 *      GuildUnbeanEvent
 * 
 * Channel related
 *      ChannelCreatedEvent
 *      ChannelDeletedEvent
 *      GenericChannelUpdateEvent
 *          ChannelUpdateNameEvent
 *          ChannelUpdatePositionEvent
 *          ChannelUpdateRegionEvent ? (need to see what is that)
 * 
 * Roles related
 *      RoleCreatedEvent
 *      RoleDeleteEvent
 *      GenericRoleUpdateUpdateEvent
 *          RoleUpdateColorEvent
 *          RoleUpdateMentionableEvent ? (need to see what is that)
 *          RoleUpdateNameEvent
 *          RoleUpdatePermissionEvent
 *          RoleUpdatePositionEvent ? (need to see what is that)
 */

package com.Chino.testbot1.mavenbotdcjda.Listener;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventMessageLogs extends ListenerAdapter {
    //get activated when a message is sent in the group
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        //get the user that sent the message
        User user = event.getAuthor();
        //if the author of the message isnt a bot (to prevent infinit loops, where the bot take his own message as events)
        if(!(user.isBot())){
            //get the adress of the logs channel
            TextChannel logChan = event.getGuild().getTextChannelsByName("message_logs", true).get(0);

            //get the raw message into a string
            String messageToLog = event.getMessage().getContentRaw();

            //get the date of the message based on the zoneId of the system
            ZonedDateTime messageTime = event.getMessage().getTimeCreated().atZoneSameInstant(ZoneId.systemDefault());

            String messageId = event.getMessageId();

            //ask bot to send message to the log channel with all the information created before
            logChan.sendMessage(user.getName() + " sent: " + messageToLog + "\nMessage id: " + messageId +"\nDate: " + messageTime).queue();
        }
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event){
        //cant get the user because MessageDeleteEvent dont have user method :(
        String messageId = event.getMessageId();
        String eventChan = event.getChannel().getAsMention();
        TextChannel logChan = event.getGuild().getTextChannelsByName("message_logs", true).get(0);

        logChan.sendMessage("A message was deleted with the id: " + messageId + " in the channel: " + eventChan).queue();
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event){
        User user = event.getAuthor();
        String messageToLog = event.getMessage().getContentRaw();
        ZonedDateTime messageTime = event.getMessage().getTimeCreated().atZoneSameInstant(ZoneId.systemDefault());
        String messageId = event.getMessageId();

        TextChannel logChan = event.getGuild().getTextChannelsByName("message_logs", true).get(0);

        logChan.sendMessage(user.getName() + " modified his message into: " + messageToLog + "\nmessage id: " + messageId + "\nDate: " + messageTime).queue();
    }
    //get activated when a reaction is added to a message 
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event){
        User user = event.getUser();
        if(!(user.isBot())){
            TextChannel logChan = event.getGuild().getTextChannelsByName("message_logs", true).get(0);

            //get the emoji based on the event
            String emoji = event.getEmoji().getName();

            String message = event.getMessageId();

            //get the channel of the event as a mention (example: #General)
            String eventChan = event.getChannel().getAsMention();

            logChan.sendMessage(user.getName() + " added a emoji: " + emoji + "\nMessage id: " + message + "\nIn the channel: " + eventChan).queue();
        }
    }

    //get activated when a reaction is deleted from a message
    //exactly the same code that for the onMessageReactionAdd
    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event){
        User user = event.getUser();
            if(!(user.isBot())){
                TextChannel logChan = event.getGuild().getTextChannelsByName("message_logs", true).get(0);

                //get the emoji based on the event
                String emoji = event.getEmoji().getName();

                String message = event.getMessageId();

                //get the channel of the event as a mention (example: #General)
                String eventChan = event.getChannel().getAsMention();

                logChan.sendMessage(user.getName() + " removed a emoji: " + emoji + "\nMessage id: " + message + "\nIn the channel: " + eventChan).queue();
            }
    }
}
/*
 *      //get the user that activated the event (put a emoji reaction on a message)
        User user = event.getUser();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();

        //getAsTag = send the name of the user without ping
        String message = user.getName() + " reacted to a message with " + "[to update]" + " in the " + channelMention + " channel! (the link: " + jumpLink + ")";
        //get the first channel created on the guild
        DefaultGuildChannelUnion defaultchan = event.getGuild().getDefaultChannel();
        //send the message in the default channel
        defaultchan.asTextChannel().sendMessage(message).queue();
        //if the one reaction have this id
        if(user.getId().equals("525693164112052255")){
            String messageIf = "Stupid Monkey Detected";
            //send this message where the user added a mention
            event.getChannel().sendMessage(messageIf).queue();
        }
 */