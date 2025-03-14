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

    private void sendDateInvalid(CommandSender sender) {
        sender.sendMessage("Le Format de date est invalid");
        sender.sendMessage(
                "Pensé au jour aux année bissextile, force à toi pour les comprende...");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 3) {
            try {
                int day = Integer.parseInt(args[0]);
                int month = Integer.parseInt(args[1]);
                int year = Integer.parseInt(args[2]);
                if (cmd.getName().equals("setTemp")) {

                    if ((year > temporal.getFirstDate().getYear()
                            || (year == temporal.getFirstDate().getYear()
                                    && (month > temporal.getFirstDate().getMonth()
                                            || (month == temporal.getFirstDate().getMonth()
                                                    && day >= temporal.getFirstDate().getDay()))))
                            && temporal.setActualyDate(day, month, year)) {
                        sender.sendMessage("changement de temporalité effectuer avec success");
                        return true;
                    } else {
                        sendDateInvalid(sender);
                        sender.sendMessage("La date acuelle ne doit pas être inférieur à la date de début!");
                    }
                }
                if (cmd.getName().equals("setStartTemp")) {
                    if ((year < temporal.getActualDate().getYear()
                            || (year == temporal.getActualDate().getYear()
                                    && (month < temporal.getActualDate().getMonth()
                                            || month == temporal.getActualDate().getMonth()
                                                    && day <= temporal.getActualDate().getDay())))
                            && temporal.setFirstDate(day, month, year)) {
                        sender.sendMessage("Changement de début de temporalité effectuer avec success");
                        return true;
                    } else {
                        sendDateInvalid(sender);
                        sender.sendMessage("La date de début ne doit pas être supérieur à la date acuelle!");
                    }
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("Format de nombre invalid");
            }
            return true;

        } else if (cmd.getName().equals("date")) {
            sender.sendMessage(temporal.getActualDate().toString());
            return true;
        }
        return false;
    }
}
