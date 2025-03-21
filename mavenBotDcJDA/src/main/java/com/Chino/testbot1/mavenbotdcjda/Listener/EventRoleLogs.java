package com.Chino.testbot1.mavenbotdcjda.Listener;

import java.time.ZoneId;

//the import as comments are for the initRoles
//import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.Chino.testbot1.mavenbotdcjda.MavenBotDcJDA;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import net.dv8tion.jda.api.entities.Role;

public class EventRoleLogs extends ListenerAdapter{
    private Role role;
    private String oldColor;

    //for some reason when a role get his color modified and i call event.getOldColor() it always return null so i made a function to get and set the role color
    private String getColor(@NotNull RoleUpdateColorEvent event, boolean newColor){
        //if the boolean is false it send back the old color of the role
        if(newColor == false){
            return this.oldColor;
        }
        //if the boolean is true it update the new value of the role color and send it back
        if(newColor == true){
            this.oldColor = event.getNewColor().getRed() + "," + event.getNewColor().getGreen() + "," + event.getNewColor().getBlue(); 
            return this.oldColor;
        }
        return null;
    }

    //the get/set are private, because for now i dont need to use it in other classes
    //getRole is already used by jda so i called my get and set EventRole
    private Role getEventRole(){
        return this.role;
    }
    private void setEventRole(Role role){
        this.role = role;
    }

    //the init does not work for now :(
    /*
    public void initRoles(SnowflakeCacheView<Role> cache){
        List<Role> temp = cache.asList();
        
        for(int i = 0; i < temp.size(); i++){
            //System.out.println(temp.get(i));
            setEventRole((Role) temp.get(i));
            //System.out.println(i +" : GetRole = " + getRole());
        } 
    }
    */

    //Events Listener for role creation/modification and erasing
    @Override
    public void onRoleCreate(@NotNull RoleCreateEvent event){
        setEventRole(event.getRole());

        MavenBotDcJDA.getLogChan().sendMessage(
            //event.getMessage().getTimeCreated().atZoneSameInstant(ZoneId.systemDefault()
            "A new role have been created! \nAt: " + MavenBotDcJDA.getWhenCreated(event.getRole().getTimeCreated().atZoneSameInstant(ZoneId.systemDefault()))
            + "\nId: " + event.getRole().getId()
            ).queue();
    }

    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event){

        MavenBotDcJDA.getLogChan().sendMessage(
            "The role \"" + event.getRole().getName() + "\" have been deleted!" 
            + "\nId: " + event.getRole().getId()
            ).queue();
    }

    @Override
    public void onRoleUpdatePermissions(@NotNull RoleUpdatePermissionsEvent event){
        String newPerms = event.getNewPermissions().toString();

        MavenBotDcJDA.getLogChan().sendMessage(
            "The role: \"" + event.getRole().getName() + "\" got his perms modified!"
            + "\nOld perms: " + event.getOldValue() 
            + "\nNew perms: " + newPerms 
            + "\nId: " + role.getId()
            ).queue();
    }

    @Override
    public void onRoleUpdateName(@NotNull RoleUpdateNameEvent event){

        MavenBotDcJDA.getLogChan().sendMessage(
            "The role: \"" + event.getOldValue() + "\" got his name modified!" 
            + "\nNew name: " + event.getNewValue()
            + "\nId: " + event.getRole().getId()
            ).queue();
    }

    @Override
    public void onRoleUpdateColor(@NotNull RoleUpdateColorEvent event){

        if(this.oldColor != null){
            MavenBotDcJDA.getLogChan().sendMessage(
                "The role: " + role.getName() + " got his color modified!"
                + "\nOld color: " + getColor(event, false) + "\nNew color: " + getColor(event, true)
                + "\nId: " + role.getId()
            ).queue();
        }
        else if(this.oldColor == null){
            MavenBotDcJDA.getLogChan().sendMessage(
                "The role: " + role.getName() + " got his color modified!"
                + "\nOld color: Default Role Color"
                + "\nNew color: " + getColor(event, true)
                + "\nId: " + role.getId()
            ).queue();
        }
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {

        MavenBotDcJDA.getLogChan().sendMessage(
        "The role: \"" + getEventRole().getName() + "\" have been added to the user: " + event.getUser().getName()
        ).queue();
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {

        MavenBotDcJDA.getLogChan().sendMessage(
            "The role: \"" + getEventRole().getName() + "\" have been removed from the user: " + event.getUser().getName()
            ).queue();
    }
}
