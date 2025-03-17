package com.Chino.testbot1.mavenbotdcjda.Listener;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventUserEntry extends ListenerAdapter{
    private String roleId;
    private String name;

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event){
        this.name = event.getMember().getEffectiveName();

        //if the role "New" dont exist then it create it
        if(event.getGuild().getRolesByName("New", false).isEmpty()){
            event.getGuild().createRole().setName("New").queue();

            event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
                "The role \"New\" have been created and implemented in the new user that joined"
            ).queue();
        }
        roleId = event.getGuild().getRolesByName("New", false).get(0).getId();

        //add the role "New" to the member that joined the server
        event.getGuild().addRoleToMember(
            event.getMember(), event.getGuild().getRoleById(roleId)
            ).queue();

        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            "A new user joined the server: " + this.name
        ).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {

        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            this.name + " quit the server :("
        ).queue();
    }

    @Override
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event){
        
        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            "The user: " + event.getUser().getName() + " changed his nickname!"
            + "\nOld nickname: " + event.getOldNickname()
            + "\nNew nickname: " + event.getNewNickname()
            + "\nId: " + event.getUser().getId()
        ).queue();
    }

}
