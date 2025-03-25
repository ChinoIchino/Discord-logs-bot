package com.Chino.testbot1.mavenbotdcjda.Commands;

import com.Chino.testbot1.mavenbotdcjda.MavenBotDcJDA;

import java.time.ZoneId;
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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashCommandsLibrary extends ListenerAdapter{

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("setup")){
          OptionMapping logChan = event.getOption("channel");
            MavenBotDcJDA.setLogChan(logChan.getAsChannel().asTextChannel());
            event.reply(MavenBotDcJDA.getLogChan().getName() + " (Id=" + MavenBotDcJDA.getLogChan().getId() + ") was set as the new log channel successfully!").queue();
        }

        else if(event.getName().equals("clearlogchannel")){
          if(MavenBotDcJDA.isInSafeChanList(MavenBotDcJDA.getLogChan())){
            event.reply("Error: This channel is in the safe channel list").queue();
          }
          else{
            MavenBotDcJDA.clearLogChannel(MavenBotDcJDA.getLogChan());

            event.reply(MavenBotDcJDA.getLogChan().getName() + " (Id=" + MavenBotDcJDA.getLogChan().getId() + ") have been cleared successfully!").queue();
          }
        }

        else if(event.getName().equals("findinchannel")){
          //get the User that was given in the command
          User userToFind = event.getOption("user").getAsUser();

          //add the log channel as the default choice
          TextChannel channelInto = MavenBotDcJDA.getLogChan();

          //if the optional information was entered the choice change from the default to the one given
          if(event.getOption("into") != null){
            channelInto = event.getOption("into").getAsChannel().asTextChannel();
          }

          //get the channel where the cache need to be taken from
          TextChannel channelFrom = event.getOption("from").getAsChannel().asTextChannel();

          //retrieve the "cache" of the channel (the max ammount is 100 messages, its represented by the .limit(100) )
          MessageHistory channelCache = MessageHistory.getHistoryFromBeginning(channelFrom).limit(100).complete();
          //change it to a list of messages
          List<Message> channelCacheList = channelCache.getRetrievedHistory();

          //creating a string that will be used in the display
          String reply = "History of the user \"" + userToFind.getName() + "\" :\n";

          //creating a temporary list (discord give a limitation of 2000 characters by message, so if it goes above this list will have the entire reply cut in multiple parts)
          ArrayList<String> temp = new ArrayList<>();

          //the channelCacheList start from the newest message to the oldest, so i need to use a inversed for loop to add the last message as the first in my string
          for(int i = (channelCacheList.size() - 1); i >= 0; i--){
            //if the id of the author of the message is the same as the id of the user we are searching
            if(channelCacheList.get(i).getAuthor().getId().equals(userToFind.getId())){
              //add the message and the time of when the message was sent into a temporary string
              String newEntry = "\n\"" + channelCacheList.get(i).getContentRaw() 
              + "\" At: " + MavenBotDcJDA.getWhenCreated(channelCacheList.get(i).getTimeCreated().atZoneSameInstant(ZoneId.systemDefault())) + "\"\n";

              reply += newEntry;

              //if the reply get to the limit of a discord message
              if(reply.length() >= 2000){
                //strip the newEntry from the reply (so that the reply is under 2000 characters)
                String tempString = reply.substring(0, (reply.length() - newEntry.length() - 1));
                
                temp.add(tempString);

                //delete the entire reply string and add only the exceeding value
                reply = newEntry;

                //if its the last iteration of the for loop and the reply goes above the limitation, add the exceeding value into the list
                if(i == 0){
                  temp.add(reply);
                }
              }
            }
          }

          //if the reply is above 2000 characters use the list to send the entire message
          if (temp.size() > 0) {
            for(int i = 0; i < temp.size(); i++){
              channelInto.sendMessage(
                temp.get(i)
              ).queue();
            }
          }
          //else give back the result
          else{
            channelInto.sendMessage(
              reply
            ).queue();
          }
          event.reply(
            "The channel informations about " + userToFind.getName() + " (Id= " + userToFind.getId() + ") was sent in " 
            + channelInto.getName() + " (Id= " + channelInto.getId() + ") successfully!"
            ).queue();
        }

        //safe list commands
        //add
        else if(event.getName().equals("safelistadd")){
          TextChannel chan = event.getOption("channel").getAsChannel().asTextChannel();

          //if indexOf(chan) return -1 that mean that this channel isnt yet in the list (to avoid duplicate channels)
          if(MavenBotDcJDA.getSafeChanList().indexOf(chan) == -1){
            MavenBotDcJDA.getSafeChanList().add(chan);
            event.reply(chan.getName() + " (Id=" + chan.getId() + ") have been added to the safe list successfully!").queue();
          }
          else{
            event.reply("This channel is already in the list").queue();
          }
        }

        //remove
        else if(event.getName().equals("safelistremove")){
          TextChannel chan = event.getOption("channel").getAsChannel().asTextChannel();
          MavenBotDcJDA.getSafeChanList().remove(chan);
          event.reply(chan.getName() + " (Id=" + chan.getId() + ") have been deleted from the safe list successfully!").queue();
        }

        //show
        else if(event.getName().equals("safelistshow")){
          ArrayList<TextChannel> safeChanList = MavenBotDcJDA.getSafeChanList();
          String safeChanListAsString = "";

        for(int i = 0; i < safeChanList.size(); i++){
            safeChanListAsString += "\n" + (i+1) + ": "+ safeChanList.get(i).getName() + " (Id=" + safeChanList.get(i).getId() + ")";
          }

          event.reply("The list: " + safeChanListAsString).queue();
        }

        else if(event.getName().equals("testprint")){
          System.out.println(MavenBotDcJDA.getLogChan().getPositionInCategory());
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event){
      List<SlashCommandData> commandDataList = new ArrayList<>();

      //a option that let the user enter a text channel if its needed in the command
      OptionData optionDataChannel = new OptionData(
        OptionType.CHANNEL, "channel", "The text channel that will be used in the command", true
        );
      //OptionDataChannelFrom and OptionDataChannelInto are the same as the one above, it is used only for display purpose for the user
      OptionData optionDataChannelFrom = new OptionData(
        OptionType.CHANNEL, "from", "from what channel the command need to take informations", true
        );
      OptionData optionDataChannelInto = new OptionData(
        OptionType.CHANNEL, "into", "into what channel the command need to send the informations (default = log channel)", false
        );
        
      //same as above but to retreive a user
      OptionData optionDataUser = new OptionData(
        OptionType.USER, "user", "The user that will be used in the command", true
        );

      //Command: /setup TEXT_CHANNEL
      commandDataList.add(
        Commands.slash("setup", "Setup a log channel for the bot to send information into")
        //add the option channel that is required for the command to work
        .addOptions(optionDataChannel)
        //only administrator or people that can manage channels can use this command
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR, Permission.MANAGE_CHANNEL))
        );
      
      //Command: /clearlogchannel
      commandDataList.add(
        Commands.slash("clearlogchannel", "Clear the entire log channel")
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
      );

      //Command: /safelistadd TEXT_CHANNEL
      commandDataList.add(
        Commands.slash("safelistadd", "Add a channel to the safe list, to avoid clearing by mistake a channel with /clearlogchannel")
        .addOptions(optionDataChannel)
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
      );

      //Command: /safelistremove TEXT_CHANNEL
      commandDataList.add(
        Commands.slash("safelistremove", "Remove the channel from the safe list")
        .addOptions(optionDataChannel)
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
      );

      //Command: /safelistshow
      commandDataList.add(
        Commands.slash("safelistshow", "Show which channels are in the safe list")
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR, Permission.MANAGE_CHANNEL))
      );

      //Command /findinlog USER From:TEXT_CHANNEL Into:TEXT_CHANNEL(Default = log channel)
      commandDataList.add(
        Commands.slash("findinchannel", "Search last 100 messages of a channel about a certain user")
        .addOptions(optionDataUser)
        .addOptions(optionDataChannelFrom)
        .addOptions(optionDataChannelInto)
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
      );
      commandDataList.add(
        Commands.slash("testprint", "a display of values that i need")
      );


      //update the commandDataList into the slash command of the bot
      event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }

    //exactly the same as the onGuildReady
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event){
      List<SlashCommandData> commandDataList = new ArrayList<>();
      OptionData optionDataChannel = new OptionData(
        OptionType.CHANNEL, "channel", "The text channel that will be used in the command", true
        );
      OptionData optionDataChannelFrom = new OptionData(
        OptionType.CHANNEL, "from", "from what channel the command need to take informations", true
        );
      OptionData optionDataChannelInto = new OptionData(
        OptionType.CHANNEL, "into", "into what channel the command need to send the informations (default = log channel)", false
        );
      OptionData optionDataUser = new OptionData(
        OptionType.USER, "user", "The user that will be used in the command", true
        );
      commandDataList.add(
        Commands.slash("setup", "Setup a log channel for the bot to send information into")
        .addOptions(optionDataChannel)
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR, Permission.MANAGE_CHANNEL))
        );
      commandDataList.add(
        Commands.slash("clearlogchannel", "Clear the entire log channel")
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
      );
      commandDataList.add(
        Commands.slash("safelistadd", "Add a channel to the safe list, to avoid clearing by mistake a channel with /clearlogchannel")
        .addOptions(optionDataChannel)
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
      );
      commandDataList.add(
        Commands.slash("safelistremove", "Remove the channel from the safe list")
        .addOptions(optionDataChannel)
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
      );
      commandDataList.add(
        Commands.slash("safelistshow", "Show which channels are in the safe list")
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR, Permission.MANAGE_CHANNEL))
      );
      commandDataList.add(
        Commands.slash("findinchannel", "Search last 100 messages of a channel about a certain user")
        .addOptions(optionDataUser)
        .addOptions(optionDataChannelFrom)
        .addOptions(optionDataChannelInto)
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
      );
        event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }
}
