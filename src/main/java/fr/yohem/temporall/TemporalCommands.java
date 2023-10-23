package fr.yohem.temporall;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TemporalCommands implements CommandExecutor {
    Temporal temporal;

    public TemporalCommands(Temporal temporal) {
        this.temporal = temporal;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        switch (cmd.getName()){
            case "setTemp":{
                if(args.length == 3){
                    try {
                        int day = Integer.parseInt(args[0]);
                        int month = Integer.parseInt(args[1]);
                        int year = Integer.parseInt(args[2]);
                        if (temporal.setFristDate(day, month, year)) {
                            temporal.resetNbDay();
                            sender.sendMessage("changement de temporalité effectuer avec success");
                            sender.sendMessage(temporal.getTemp());
                            return true;
                        }
                        else
                            sender.sendMessage("Le Format de date est invalid");
                    }catch (NumberFormatException e){
                        sender.sendMessage("Format de nombre invalid");
                    }
                    return true;
                }
            }
            break;
            case "setStartTemp":{
                if(args.length == 3){
                    try {
                        int day = Integer.parseInt(args[0]);
                        int month = Integer.parseInt(args[1]);
                        int year = Integer.parseInt(args[2]);
                        if (temporal.setFristDate(day, month, year)) {
                            sender.sendMessage("changement de debut de temporalité effectuer avec success");
                            sender.sendMessage(temporal.getTemp());
                            return true;
                        }
                        else
                            sender.sendMessage("Le Format de date est invalid");
                    }catch (NumberFormatException e){
                        sender.sendMessage("Format de nombre invalid");
                    }
                    return true;

                }
            }
            break;
            case "date":{
                sender.sendMessage(temporal.getTemp());
                return true;
            }
        }
        sender.sendMessage(temporal.getTemp());
        return false;
    }
}
