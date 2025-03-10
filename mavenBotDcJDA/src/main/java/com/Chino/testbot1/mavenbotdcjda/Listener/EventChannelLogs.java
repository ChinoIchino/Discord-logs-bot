package com.Chino.testbot1.mavenbotdcjda.Listener;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateUserLimitEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventChannelLogs extends ListenerAdapter{

    private String getEventTime(ZonedDateTime chanAtTime){
        //just to have a better display if the minute are under 10 so it display 14h03 and not 14h3
        if(chanAtTime.getMinute() < 10){
            return 
            chanAtTime.getDayOfMonth() + "/" + chanAtTime.getMonthValue() + "/" + chanAtTime.getYear() + " "
            + chanAtTime.getHour() + "h0" + chanAtTime.getMinute() + " ("
            + chanAtTime.getOffset() + " " + chanAtTime.getZone() + ")";
        }
        return 
            chanAtTime.getDayOfMonth() + "/" + chanAtTime.getMonthValue() + "/" + chanAtTime.getYear() + " "
            + chanAtTime.getHour() + "h" + chanAtTime.getMinute() + " ("
            + chanAtTime.getOffset() + " " + chanAtTime.getZone() + ")";
    }

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event){
        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            "A " + event.getChannelType() + " channel have been created!"
            + "\nAt: " + getEventTime(event.getChannel().getTimeCreated().atZoneSameInstant(ZoneId.systemDefault()))
            + "\nWith the name: " + event.getChannel().getName()
            + "\nId: " + event.getChannel().getId()
            ).queue();
    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event){
        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            "A " + event.getChannelType() + " channel have been deleted!" 
            + "\nWith the name: " + event.getChannel().getName()
            + "\nId: " + event.getChannel().getId()
            ).queue();
    }

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent event){
        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            "The " + event.getOldValue() + " channel had his name modified!"
            + "\nOld name: " + event.getOldValue() + "\nNew name: " + event.getNewValue() 
            + "\nId: " + event.getChannel().getId()
            ).queue();
    }

    //Works only on voice channels
    @Override
    public void onChannelUpdateUserLimit(@NotNull ChannelUpdateUserLimitEvent event){
        //When a channel dont have a limit its equal to 0. So i needed to add some if statements for the messages to make more sense
        if(event.getOldValue() == 0){
            event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
                "The " + event.getChannel().getName() + " channel had his permissions modified!"
                + "\nOld User Limit: No Limit\nNew User Limit: " + event.getNewValue()
                + "\nId: " + event.getChannel().getId()
            ).queue(); 
        }
        else if(event.getNewValue() == 0){
            event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
                "The " + event.getChannel().getName() + " channel had his permissions modified!" 
                + "\nOld User Limit: " + event.getOldValue() + "\nNew User Limit: No Limit" 
                + "\nId: " + event.getChannel().getId()
            ).queue();
        }
        else{
            event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
                "The " + event.getChannel().getName() + " channel had his permissions modified!" 
                + "\nOld permissions: " + event.getOldValue() + "\nNew permissions: " + event.getNewValue()
                + "\nId: " + event.getChannel().getId()
            ).queue();
        }
    }
}
