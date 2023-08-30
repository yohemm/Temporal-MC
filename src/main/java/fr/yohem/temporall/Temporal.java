package fr.yohem.temporall;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Temporal extends JavaPlugin {
    private int fristDate = 910;
    private int fristYear = 2001;
    private long nbDayPass = 0;

    public boolean setFristDate(int day, int month, int year){
        if (0<month && month < 13){
            if (day > 0 && day  <= maxDayOfAnMonth(month, year)){
                fristYear = year;
                fristDate = day * 100 +month;
                return true;
            }
        }
        return false;
    }
    public boolean setFristDate(int date){
        return setFristDate(date%100,date%10000/100, date/10000);
    }
    public void resetNbDay(){
        nbDayPass = 0;
    }

    public int fullDate() {
        return fristYear*10000+fristDate;
    }

    public String getTemp(){
        String res="";
        long dayToAdd = nbDayPass;
        List<String> month= Arrays.asList("janvier", "février", "mars", "avril", "mai", "juin", "juillet", "aout", "septembre", "octobre", "novembre", "decembre");

        int y = fristYear;
        int m = (fristDate)%100;
        long d = (fristDate/100);
        while (dayToAdd>0){
            int maxOfMonth = maxDayOfAnMonth(m,y);
            if (maxOfMonth < dayToAdd+(d-1)){
                d = 1;
                m ++;
                if (m >12){
                    m=1;
                    y++;
                }
            }
            else {
                d+=dayToAdd;
            }
            dayToAdd -=maxOfMonth-(d-1);
        }
        return d + " "+month.get(m-1) + " " + y;
    }

    public int maxDayOfAnMonth(int m, int y){
        int maxOfMonth = 30;
        switch (m){
            case 1:case 3:case 5:case 7:case 8: case 10: case 12:
                maxOfMonth = 31;
                break;
            case 2: {
                if (y%4==0 && (y%100!=0 || y%400==0))maxOfMonth = 29;
                else maxOfMonth = 28;
            };
            break;
        }
        return maxOfMonth;
    }

    @Override
    public void onLoad(){
        final File file = new File(this.getDataFolder()+"/date.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        try {
            int date = (int) yamlConfiguration.get("date");
            setFristDate(date);
            resetNbDay();

        }catch (NullPointerException e){
            throw new RuntimeException();
        }
    }
    @Override
    public void onEnable() {
        World world = getServer().getWorld("world");
        final boolean[] day = {false};

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,new Runnable(){

            @Override
            public void run() {
                if (world.getTime() < 13000&&!day[0]){
                    day[0] = true;
                    nbDayPass++;
                    getServer().getOnlinePlayers().forEach(player -> player.sendTitle("§6"+getTemp(), "§9le soleil se leve a l'horizon", 10, 40,10));
                }else if (world.getTime() >= 13000&& day[0]){
                    day[0] = false;
                }
            }
        }, 0, 1);


        getCommand("setTemp").setExecutor(new TemporalCommands(this));
        getCommand("setStartTemp").setExecutor(new TemporalCommands(this));
        getCommand("date").setExecutor(new TemporalCommands(this));
        getServer().getPluginManager().registerEvents(new TemporalListeners(this), this);
    }

    @Override
    public void onDisable() {
        final File file = new File(this.getDataFolder()+"/date.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set("date", fullDate());
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
