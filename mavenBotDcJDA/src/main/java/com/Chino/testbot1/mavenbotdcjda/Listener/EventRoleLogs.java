package com.Chino.testbot1.mavenbotdcjda.Listener;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Role;


/*
* Roles related
*      GenericRoleUpdateUpdateEvent
*          RoleUpdateMentionableEvent ? (need to see what is that)
*          RoleUpdatePositionEvent ? (need to see what is that)
*/

public class EventRoleLogs extends ListenerAdapter{
    private String oldColor;

    private String getColor(@NotNull RoleUpdateColorEvent event, boolean newColor){
        //if the boolean is false it send back the old color of the role
        if(newColor == false){
            return this.oldColor;
        }
        //if the boolean is true it update the new value of the role color and send it back
        if(newColor == true){
            this.oldColor = event.getNewColor().getRed() + "," + event.getNewColor().getGreen() + "," + event.getNewColor().getBlue(); 
            return event.getNewColor().getRed() + "," + event.getNewColor().getGreen() + "," + event.getNewColor().getBlue();
        }
        return null;
    }

    //I added the role as the input because the event differ based on the function
    private String getEventTime(Role role){
        ZonedDateTime roleAtTime = role.getTimeCreated().atZoneSameInstant(ZoneId.systemDefault());
        //just to have a better display if the minute are under 10 so it display 14h03 and not 14h3
        if(roleAtTime.getMinute() < 10){
            return 
            roleAtTime.getDayOfMonth() + "/" + roleAtTime.getMonthValue() + "/" + roleAtTime.getYear() + " "
            + roleAtTime.getHour() + "h0" + roleAtTime.getMinute() + " ("
            + roleAtTime.getOffset() + " " + roleAtTime.getZone() + ")";
        }
        return 
            roleAtTime.getDayOfMonth() + "/" + roleAtTime.getMonthValue() + "/" + roleAtTime.getYear() + " "
            + roleAtTime.getHour() + "h" + roleAtTime.getMinute() + " ("
            + roleAtTime.getOffset() + " " + roleAtTime.getZone() + ")";
    }

    //Events Listener for role creation/modification and erasing
    //get activated when a role is created
    @Override
    public void onRoleCreate(@NotNull RoleCreateEvent event){
        //get the role who activated the event
        Role role = event.getRole();

        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            "The role: " + role.getName() + " have been created! \nAt: " + getEventTime(role) 
            + "\nColor: Default Role Color" + "\nId: " + role.getId()
            ).queue();
    }

    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event){
        Role role = event.getRole();

        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            "The role: " + role.getName() + " have been deleted! \nAt: " + getEventTime(role) + "\nId: " + role.getId()
            ).queue();
    }

    @Override
    public void onRoleUpdatePermissions(@NotNull RoleUpdatePermissionsEvent event){
        Role role = event.getRole();
        String newPerms = event.getNewPermissions().toString();

        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            "The role: " + role.getName() + " got his perms modified!"+ "\nAt: " + getEventTime(role)
            + "\nOld perms: " + event.getOldValue() + "\nNew perms: " + newPerms 
            + "\nId: " + role.getId()
            ).queue();
    }

    @Override
    public void onRoleUpdateName(@NotNull RoleUpdateNameEvent event){
        Role role = event.getRole();

        event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
            "The role: " + event.getOldValue() + " got his name modified!" + "\nAt: " + getEventTime(role)
            + "\nOld name: " + event.getOldValue() + "\nNew name: " + event.getNewValue()
            + "\nId: " + role.getId()
            ).queue();
    }

    @Override
    public void onRoleUpdateColor(@NotNull RoleUpdateColorEvent event){
        Role role = event.getRole();

        if(this.oldColor != null){
            event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
                "The role: " + role.getName() + " got his color modified! \nAt: " + getEventTime(role) 
                + "\nOld color: " + getColor(event, false) + "\nNew color: " + getColor(event, true)
                + "\nId: " + role.getId()
            ).queue();
        }
        else if(this.oldColor == null){
            event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
                "The role: " + role.getName() + " got his color modified! \nAt: " + getEventTime(role) 
                + "\nOld color: Default Role Color"
                + "\nNew color: " + getColor(event, true)
                + "\nId: " + role.getId()
            ).queue();
        }
        else{
            event.getGuild().getTextChannelsByName("message_logs", true).get(0).sendMessage(
                "Error: this.oldColor isnt null nor does it have a value"
            ).queue();
        }
    }
    
}
