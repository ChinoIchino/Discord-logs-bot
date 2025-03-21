package com.Chino.testbot1.mavenbotdcjda.Commands;

import com.Chino.testbot1.mavenbotdcjda.MavenBotDcJDA;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashCommandsLibrary extends ListenerAdapter{

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("setup")){
          OptionMapping logChan = event.getOption("channel");
            MavenBotDcJDA.setLogChan(logChan.getAsChannel().asTextChannel());
            event.reply(MavenBotDcJDA.getLogChan().getName() + "(Id= " + MavenBotDcJDA.getLogChan().getId() + ") was set as the new log channel successfully!").queue();
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event){
      List<SlashCommandData> commandDataList = new ArrayList<>();

      //Command: /setup TEXT_CHANNEL

      OptionData setupCommandOptionData = new OptionData(OptionType.CHANNEL, "channel", "The channel were information will be logged", true);
      commandDataList.add(
        Commands.slash("setup", "setup a log channel for the bot to send information into")
        .addOptions(setupCommandOptionData)
        //only administrator or people that can manage channels can use this command
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR, Permission.MANAGE_CHANNEL))
        );

        //update the commandDataList into the slash command of the bot
        event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }

    //exactly the same as the onGuildReady
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event){
      List<SlashCommandData> commandDataList = new ArrayList<>();

      OptionData setupCommandOptionData = new OptionData(OptionType.CHANNEL, "channel", "The channel were information will be logged", true)
        .setChannelTypes(ChannelType.TEXT);
      commandDataList.add(
        Commands.slash("setup", "setup a log channel for the bot to send information into")
        .addOptions(setupCommandOptionData)
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS, Permission.MANAGE_CHANNEL))
        );

        event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }

}