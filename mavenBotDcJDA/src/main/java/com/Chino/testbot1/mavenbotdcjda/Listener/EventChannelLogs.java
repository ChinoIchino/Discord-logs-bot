package com.Chino.testbot1.mavenbotdcjda.Listener;

import java.time.ZoneId;

import org.jetbrains.annotations.NotNull;

import com.Chino.testbot1.mavenbotdcjda.MavenBotDcJDA;

import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateUserLimitEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventChannelLogs extends ListenerAdapter{

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event){
        MavenBotDcJDA.getLogChan().sendMessage(
            "A " + event.getChannelType() + " channel have been created!"
            + "\nAt: " + MavenBotDcJDA.getWhenCreated(event.getChannel().getTimeCreated().atZoneSameInstant(ZoneId.systemDefault()))
            + "\nWith the name: " + event.getChannel().getName()
            + "\nId: " + event.getChannel().getId()
            ).queue();
    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event){
        MavenBotDcJDA.getLogChan().sendMessage(
            "A " + event.getChannelType() + " channel have been deleted!" 
            + "\nWith the name: " + event.getChannel().getName()
            + "\nId: " + event.getChannel().getId()
            ).queue();
    }

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent event){
        MavenBotDcJDA.getLogChan().sendMessage(
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
            MavenBotDcJDA.getLogChan().sendMessage(
                "The " + event.getChannel().getName() + " channel had his permissions modified!"
                + "\nOld User Limit: No Limit\nNew User Limit: " + event.getNewValue()
                + "\nId: " + event.getChannel().getId()
            ).queue(); 
        }
        else if(event.getNewValue() == 0){
            MavenBotDcJDA.getLogChan().sendMessage(
                "The " + event.getChannel().getName() + " channel had his permissions modified!" 
                + "\nOld User Limit: " + event.getOldValue() + "\nNew User Limit: No Limit" 
                + "\nId: " + event.getChannel().getId()
            ).queue();
        }
        else{
            MavenBotDcJDA.getLogChan().sendMessage(
                "The " + event.getChannel().getName() + " channel had his permissions modified!" 
                + "\nOld permissions: " + event.getOldValue() + "\nNew permissions: " + event.getNewValue()
                + "\nId: " + event.getChannel().getId()
            ).queue();
        }
    }
}
