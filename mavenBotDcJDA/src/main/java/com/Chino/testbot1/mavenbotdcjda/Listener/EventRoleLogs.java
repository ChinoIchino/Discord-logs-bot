package com.Chino.testbot1.mavenbotdcjda.Listener;

//i need to verify how i get a string color from jawa.awt.Color (because i doubt it will work like that)

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.EventListener;
import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import kotlin.OverloadResolutionByLambdaReturnType;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;


/*
* Roles related
*      RoleCreatedEvent
*      RoleDeleteEvent
*      GenericRoleUpdateUpdateEvent
*          RoleUpdateColorEvent
*          RoleUpdateMentionableEvent ? (need to see what is that)
*          RoleUpdateNameEvent
*          RoleUpdatePermissionEvent
*          RoleUpdatePositionEvent ? (need to see what is that)
*
*        logChan.sendMessage("The Role Name: " + role.getName() + "\nRole Manager: " + role.getManager() + "\nRole Mention: " + role.getAsMention() + "\nRole Color: " + role.getColorRaw() +
         "\nRole id: " + role.getId() + "\nRole Perms: " + role.getPermissions() + "\rRole Perms Raw: " + role.getPermissionsRaw() + "\nRole Position: " + role.getPosition() + 
         "\nRole Position Raw: " + role.getPositionRaw()).queue();
*/
public class EventRoleLogs extends ListenerAdapter{
    private String name, id, perms;
    private TextChannel logChan;
    private int color;

    //get and set
    private void setRoleName(String name){
        this.name = name;
    }
    private String getRoleName(){
        return this.name;
    }
    
    private void setRoleId(String id){
        this.id = id;
    }
    private String getRoleId(){
        return this.id;
    }
    
    private void setRoleColor(int color){
        this.color = color;
    }
    private int getRoleColor(){
        return this.color;
    }

    private void setRolePerms(String perms){
        this.perms = perms;
    }
    private String getRolePerms(){
        return this.perms;
    }

    private void setLogChan(TextChannel logChan){
        this.logChan = logChan;
    }
    private TextChannel getLogChan(){
        return this.logChan;
    }

    //needed to input the "Role role", because the event is different based on which function was activated
    private ZonedDateTime getEventTime(Role role){
        return role.getTimeCreated().atZoneSameInstant(ZoneId.systemDefault()); 
    }

    //Events Listener for role creation/modification and erasing
    //get activated when a role is created
    @Override
    public void onRoleCreate(@NotNull RoleCreateEvent event){
        //get the role who activated the event
        Role role = event.getRole();
        //set the name, id, color and perms of the role in a private variable
        setRoleName(role.getName());
        setRoleId(role.getId());
        setRoleColor(role.getColorRaw());
        setRolePerms(role.getPermissions().toString());
        //also added the logs channel so it wont be needed to declare it in every function
        setLogChan(logChan = event.getGuild().getTextChannelsByName("message_logs", true).get(0));

        getLogChan().sendMessage(
            "A new role have been created, with the name: " + name + " at: " + getEventTime(role) +"\n" + name + " color: " + getRoleColor() 
            + "\n" + name + " admin perms: " + role.getPermissions() + "\n" + name + " id: " + getRoleId()
            ).queue();
        /*
        try {
            logChan.sendMessage("A new role was added: " + role).queue();
        } catch (Exception e) {
            logChan.sendMessage("??").queue();
        }
        */
    }

    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event){
        Role role = event.getRole();

        getLogChan().sendMessage("A role have been deleted: " + getRoleName() + " at: " + getEventTime(role) + "\n" + name + " id: " + getRoleId()).queue();
    }

    @Override
    public void onRoleUpdatePermissions(@NotNull RoleUpdatePermissionsEvent event){
        Role role = event.getRole();
        String newPerms = event.getNewPermissions().toString();

        getLogChan().sendMessage(
            "The role: " + getRoleName() + " got his perms modified! At: " + getEventTime(role) + "\nOld perms: " + getRolePerms() + "\nNew perms: " + newPerms + "\nId: " + getRoleId()
            ).queue();
        
        setRolePerms(newPerms);
    }

    @Override
    public void onRoleUpdateName(@NotNull RoleUpdateNameEvent event){
        Role role = event.getRole();
        //String oldName = event.getOldName().toString(); = getRoleName();
        String newName = event.getNewName().toString();

        getLogChan().sendMessage(
            "The role: " + getRoleName() + " got his name modified! At: " + getEventTime(role) + "\nOld name: " + getRoleName() + "\nNew name: " + newName + "\nId: " + getRoleId()
            ).queue();
        
        setRoleName(newName);
    }

    @Override
    public void onRoleUpdateColor(@NotNull RoleUpdateColorEvent event){
        Role role = event.getRole();
        int newColor = event.getOldColorRaw();

        getLogChan().sendMessage(
            "The role: " + getRoleName() + "got his color modified! At: " + getEventTime(role) + "\nOld color: " + getRoleColor() + "\nNew color" + newColor + "\nId: " + getRoleId()
        ).queue();

        setRoleColor(newColor);

    }
    
}
