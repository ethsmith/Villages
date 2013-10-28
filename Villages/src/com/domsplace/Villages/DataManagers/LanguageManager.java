package com.domsplace.Villages.DataManagers;

import static com.domsplace.Villages.Bases.Base.ChatDefault;
import static com.domsplace.Villages.Bases.Base.ChatError;
import static com.domsplace.Villages.Bases.Base.getDataFolder;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Enums.ManagerType;
import com.domsplace.Villages.Objects.Plot;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Tax;
import com.domsplace.Villages.Objects.Village;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class LanguageManager extends DataManager {
    private YamlConfiguration config;
    private File configFile;
    
    public LanguageManager() {
        super(ManagerType.LANGUAGE);
    }
    
    @Override
    public void tryLoad() throws IOException {
        this.configFile = new File(getDataFolder(), "messages.yml");
        if(!this.configFile.exists()) configFile.createNewFile();
        this.config = YamlConfiguration.loadConfiguration(configFile);
        
        cDV("alreadyinvillage", "%e%You are already in a Village.");
        cDV("cantattackdifferentvillage", "%e%Cannot attack players who are in a different village to you.");
        cDV("cantattacksamevillage", "%e%Cannot attack players who are in the same village as you.");
        cDV("cantattackwilderness", "%e%Cannot attack players who aren't in a village.");
        cDV("cantcontinueitems", "%e%The village doesn't have the necessary items to continue!");
        cDV("cantcontinuemoney", "%e%The village doesn't have enough money to continue!");
        cDV("cantexpandborder", "%e%Can't expand this region, it doesn't border your Village.");
        cDV("cantfindtax", "%e%Can't find a tax by that name.");
        cDV("cantfindvillage", "%e%Couldn't find Village.");
        cDV("cantkickmayor", "%e%You cannot kick the mayor of the Village.");
        cDV("chunkavailable", "%d%This chunk is available!\nPrce: %i%%x%");
        cDV("chunkclaimed", "%d%The %i%Mayor %d%has claimed %i%%r%%d% for you.");
        cDV("chunkclaimedbyplayer", "%e%This plot is claimed by another player.");
        cDV("chunknotowned", "%e%You don't own this plot.");
        cDV("claimedchunk", "%d%Claimed %r%!");
        cDV("claimedchunkinfo", "%i%%p% %d%has claimed this plot.");
        cDV("closevillagenotmayor", "%e%You cannot close the Village, you are not the mayor.");
        cDV("createdvillage", "%i%%p% %d%created the Village %i%%v%%d%!");
        cDV("createvillageoverlap", "%e%You can't create a Village here, it overlaps another Village.");
        cDV("createvillageregionoverlap", "%e%Can't create village here, it overlaps a region.");
        cDV("deniedinvite", "%i%Denied the Village invite.");
        cDV("depositedmoney", "%i%%p% %d%deposited %i%%x% %d%into the Village Bank.");
        cDV("economydisabled", "%e%The server has Economy disabled.");
        cDV("enteramount", "%e%Please enter an amount to deposit into the bank.");
        cDV("enteramt", "%e%Please enter a number.");
        cDV("enterdescription", "%e%Please enter a description.");
        cDV("enterkickname", "%e%Enter the name of the player to kick.");
        cDV("entermessage", "%e%Please enter a message.");
        cDV("enterplayer", "%e%Please enter a player name.");
        cDV("entervillagename", "%e%Please enter a Village name.");
        cDV("enterwilderness", "%d%You are now in the %i%Wilderness%d%.");
        cDV("error", "%e%An Error occured! please contact the admin.");
        cDV("expandingvillage", "%i%Expanding Village... please wait...");
        cDV("expandregionoverlap", "%e%Can't expand village, the expansion overlaps a region.");
        cDV("expandvillageoverlap", "%e%You cannot expand the Village, it overlaps another Village.");
        cDV("foevillageenter", "Visitor %i%%p% %d%has entered the Village!");
        cDV("foewildernessenter", "Visitor %i%%p% %d%has left the Village!");
        cDV("friendlyvillageenter", "Resident %i%%p% %d%has entered the Village!");
        cDV("friendlywildernessenter", "Resident %i%%p% %d%has left the Village!");
        cDV("goingtovillage", "%d%Going to Village spawn.");
        cDV("invalidargument", "%e%Invalid argument supplied.");
        cDV("invalidvillagename", "%e%Invalid Village name, name can only have letters and/or numbers.");
        cDV("descriptionlong", "%e%The Description must be less than 80 characters long.");
        cDV("invalidvillagedescription", "%e%New description contains an invalid symbol.");
        cDV("itembankdisabled", "%e%Item banks are disabled on this server.");
        cDV("joinedvillage", "%i%%p% %d%joined the village.");
        cDV("leavevillagemayor", "%e%You cannot leave the Village, you are the mayor.");
        cDV("leftvillage", "%i%%p% %d%left the Village");
        cDV("maxofthreeexpand", "%e%You can only expand 3 chunks at a time.");
        cDV("mayorkickonly", "%e%Only the mayor can kick players.");
        cDV("mustbenumber", "%e%Amount must be a number.");
        cDV("mustbeone", "%e%Amount must be more than zero.");
        cDV("muted", "%e%You can't do this, you're muted.");
        cDV("neednamedelete", "%e%Enter a Village name to delete.");
        cDV("needvillagename", "%e%Please provide a Village name.");
        cDV("newdescription", "%i%The new Village description is: %d%%x%");
        cDV("newmayor", "The Village has a new Mayor! %i%Mayor %p%%d%!");
        cDV("newmayorname", "%e%Please enter the name of the new Mayor.");
        cDV("nexttaxday", "The next tax day for %i%%tax% %d%is in %i%%date%%d%.");
        cDV("nointeract", "%e%You cannot interact here.");
        cDV("noinvite", "%e%You haven't received an invite.");
        cDV("nopermission", "%e%You don't have permission for this.");
        cDV("notaxes", "There are no taxes!");
        cDV("notenougharguments", "%e%Not enough arguments.");
        cDV("notenoughmoney", "%e%You don't have %x%.");
        cDV("notinthisworld", "%e%You can't do that in this world!");
        cDV("notinvillage", "%e%You aren't in a Village.");
        cDV("notmayordescription", "%e%Only the mayor can change the Village description");
        cDV("notmoney", "%e%The entered number was not a valid amount of money.");
        cDV("notplot", "%e%This plot doesn't have a price set!");
        cDV("notresident", "%e%%p% isn't a resident of the Village.");
        cDV("onlymayor", "%e%Only the Mayor can do this.");
        cDV("onlymayorbank", "%e%Only the mayor can edit the village bank.");
        cDV("onlymayorexpand", "%e%Only the mayor can expand the Village.");
        cDV("onlymayorexplode", "%e%Only the mayor can explode the Village.");
        cDV("onlymayorplot", "%e%Only the mayor can edit plots.");
        cDV("onlymayorsetmayor", "%e%Only the Mayor can elect the new Mayor.");
        cDV("playeraddedtovillage", "Added %i%%p%%d% to %i%%v%%d%!");
        cDV("playeralreadymayor", "%e%This player is already the Mayor of this Village.");
        cDV("playerdifferentvillage", "%e%This Player is in a different Village.");
        cDV("playerinvillage", "%e%%p% is already in a Village.");
        cDV("playernotfound", "%e%Couldn't find that player!");
        cDV("playernotinvillage", "%e%This Player is not in a Village.");
        cDV("playeronly", "%e%Only players can do this.");
        cDV("playerremovedfromvillage", "Removed %i%%p%%d% from %i%%v%%d%!");
        cDV("playersetmayor", "Made %i%%p%%d% is now the Mayor of %i%%v%%d%!");
        cDV("playervillage", "%d%Player %i%%p%%d% is in Village %i%%v%%d%.");
        cDV("plotnotinvillage", "%e%This plot is not in the Village.");
        cDV("plotsnotenabled", "%e%Plots are not enabled on this server.");
        cDV("residentinvited", "%i%%p% %d%was invited to the Village.");
        cDV("residentkicked", "%i%%p% %d%was kicked from the Village.");
        cDV("residentslist", "&c= &9Village Members &c=");
        cDV("setplotowner", "%d%Set the plot owner as %i%%p%");
        cDV("setplotprice", "%d%Set the plot price to %i%%x%");
        cDV("setvillagespawn", "Set the new spawn at %i%%r%%d%!");
        cDV("taxinfo", "%i%Information about %t%:");
        cDV("taxdayfail", "The Village %i%%v%%d% couldn't pay the tax man!");
        cDV("taxes", "%i%Taxes, type /%x% tax check [name] for more info.");
        cDV("taxeslist", "&c= &eTaxes Due &c=");
        cDV("taxeslistnotvillage", "No Taxes.");
        cDV("notaxfound", "%e%Couldn't find the tax by that name.");
        cDV("topvillages", "%i%The top %x% Villages are:");
        cDV("topvillageslist", " &c= &6Top Villages &c= ");
        cDV("villagebankneedmore", "%e%You don't have enough money in the Village bank! You need %n% to do this.");
        cDV("villageclosed", "%d%The Village %i%%v% %d%fell into Anarchy!");
        cDV("villagedelete", "%d%Deleted the village %i%%v%%d%!");
        cDV("villagedoesntexist", "%e%Village doesn't exist.");
        cDV("villageexpanded", "%d%The Village was expanded %i%%x% %d%region(s).");
        cDV("villageexplodeconfirm", "%e%Type /village mayor explode YES if you're sure you want to explode the Village.");
        cDV("villageexploded", "%i%The Village was Exploded!");
        cDV("villageinvite", "%i%%p% %d%has invited you to join %i%%v%%d%,\n\tType /accept to Accept\n\tType /deny to Decline");
        cDV("villagenamechaned", "New Village name is now %i%%v%%d%!");
        cDV("villagenameused", "%e%This Village name is already in use or is reserved.");
        cDV("villages", "%i%Villages: %d%%x%");
        cDV("welcomevillage", "");
        cDV("wildernessvillageneter", "Traveller %i%%p% %d%has entered the Village!");
        cDV("wildernesswildernessneter", "Traveller %i%%p% %d%left the Village and entered the Wilderness!");
        cDV("withdrawledmoney", "%i%%p% %d%withdrew %i%%n% %d%from the Village Bank.");
        
        this.trySave();
    }
    
    @Override
    public void trySave() throws IOException {
        config.save(configFile);
    }
    
    private void cDV(String key, Object defaultValue) {
        if(!config.contains(key)) {
            config.set(key, defaultValue);
        }
    }

    public List<String> getKey(String key, Object... o) {
        String string = config.getString(key, ChatError + "MISSING LANGUAGE KEY: " + key);
        
        String[] split = string.split("\\n");
        List<String> list = new ArrayList<String>();
        
        for(String s : split) {
            for(Object ob : o) {
                s = appendKey(s, ob);
            }

            s = s.replaceAll("%d%", ChatDefault);
            s = s.replaceAll("%i%", ChatImportant);
            s = s.replaceAll("%e%", ChatError);
            
            list.add(s);
        }
        
        return list;
    }
    
    private String appendKey(String oldData, Object newData) {
        if(newData instanceof Player) return appendKey(oldData, (Player) newData);
        if(newData instanceof OfflinePlayer) return appendKey(oldData, (OfflinePlayer) newData);
        if(newData instanceof Double) return appendKey(oldData, (Double) newData);
        if(newData instanceof Village) return appendKey(oldData, (Village) newData);
        if(newData instanceof Resident) return appendKey(oldData, (Resident) newData);
        if(newData instanceof Region) return appendKey(oldData, (Region) newData);
        if(newData instanceof Plot) return appendKey(oldData, (Plot) newData);
        if(newData instanceof Tax) return appendKey(oldData, (Tax) newData);
        
        return oldData.replaceAll("%x%", newData.toString());
    }
    
    private String appendKey(String oldData, Player p) {
        return oldData.replaceAll("%p%", p.getDisplayName());
    }
    
    private String appendKey(String oldData, OfflinePlayer p) {
        if(p.isOnline()) return appendKey(oldData, p.getPlayer());
        return oldData.replaceAll("%p%", p.getName());
    }
    
    private String appendKey(String oldData, Double amt) {
        return oldData.replaceAll("%n%", Double.toString(amt));
    }
    
    private String appendKey(String oldData, Village v) {
        return oldData.replaceAll("%v%", v.getName());
    }
    
    private String appendKey(String oldData, Resident r) {
        return oldData.replaceAll("%p%", r.getName());
    }
    
    private String appendKey(String oldData, Region r) {
        return oldData.replaceAll("%r%", r.toString());
    }
    
    private String appendKey(String oldData, Plot r) {
        return oldData.replaceAll("%r%", r.getRegion().toString());
    }
    
    private String appendKey(String oldData, Tax r) {
        return oldData.replaceAll("%t%", r.getName());
    }
}
