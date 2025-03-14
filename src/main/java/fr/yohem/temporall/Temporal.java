package fr.yohem.temporall;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Temporal extends JavaPlugin {
    private long nbDayPass = 0;
    private Date firstDate = new Date(2001, 10, 9);
    private Date actualDate = new Date(2001, 10, 9);

    public boolean setDate(Date targetDate, int day, int month, int year) {
        if (0 < month && month < 13) {
            if (day > 0 && day <= Date.maxDayOfAnMonth(month, year)) {
                targetDate.setDay(day);
                targetDate.setMonth(month);
                targetDate.setYear(year);
                nbDayPass = Date.diffDate(firstDate, actualDate);
                return true;
            }
        }
        return false;
    }

    public boolean setDate(Date targetDate, int date) {
        return setDate(targetDate, date % 100, date % 10000 / 100, date / 10000);
    }

    public boolean setActualyDate(int day, int month, int year) {
        return setDate(actualDate, day, month, year);
    }

    public boolean setFirstDate(int day, int month, int year) {
        return setDate(firstDate, day, month, year);
    }

    public Date getFirstDate() {
        return Date.clone(firstDate);
    }

    public Date getActualDate() {
        return Date.clone(actualDate);
    }

    @Override
    public void onLoad() {
        final File file = new File(this.getDataFolder() + "/date.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        int date;
        nbDayPass = (int) yamlConfiguration.get("nbDayPass");
        date = (int) yamlConfiguration.get("dateFirst");
        if (date != 0) {
            firstDate = new Date(date);
            actualDate = Date.clone(firstDate);
            actualDate.addDay(nbDayPass);
        }
    }

    @Override
    public void onEnable() {
        World world = getServer().getWorld("world");
        final boolean[] day = { true };

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                if (world.getTime() < 13000 && !day[0]) {
                    day[0] = true;
                    nbDayPass++;
                    actualDate.addDay(1);
                    getServer().getOnlinePlayers().forEach(player -> player.sendTitle("§6" + getActualDate(),
                            "§9le soleil se leve a l'horizon", 10, 40, 10));
                    System.out.println("Nombre de jour écoule : " + nbDayPass + " | depuis : " + firstDate);
                } else if (world.getTime() >= 13000 && day[0]) {
                    day[0] = false;
                }
            }
        }, 0, 1);

        getCommand("setTemp").setExecutor(new TemporalCommands(this));
        getCommand("setStartTemp").setExecutor(new TemporalCommands(this));
        getCommand("date").setExecutor(new TemporalCommands(this));
    }

    @Override
    public void onDisable() {
        final File file = new File(this.getDataFolder() + "/date.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set("nbDayPass", nbDayPass);
        yamlConfiguration.set("dateFirst", Date.getDataDate(firstDate));
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
