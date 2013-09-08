package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Enums.ManagerType;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageManager extends DataManagerBase {
    public File languageFile;
    public YamlConfiguration language;
    public YamlConfiguration defaultLanguage;
    
    public LanguageManager() {
        super(ManagerType.LANGUAGE);
    }
    
    @Override
    public void tryLoad() throws IOException {
        languageFile = new File(getDataFolder(), "messages.yml");
        if(!languageFile.exists()) {
            languageFile.createNewFile();
        }

        language = YamlConfiguration.loadConfiguration(languageFile);
        defaultLanguage = YamlConfiguration.loadConfiguration(languageFile);
        YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(languageFile);

        //Create Default Values
        cDV("nointeract", "%e%You cannot interact here.");
        cDV("enterwilderness", "%d%You are now in the %i%Wilderness%d%.");
        cDV("neednamedelete", "%e%Enter a Village name to delete.");
        cDV("villagedoesntexist", "%e%Village doesn't exist.");
        cDV("villagedelete", "%d%Deleted the village %i%%v%%d%!");
        cDV("needvillagename", "%e%Please provide a Village name.");
        cDV("enteramount", "%e%Please enter an amount to deposit into the bank.");
        cDV("mustbeone", "%e%Amount must be more than zero.");
        cDV("mustbenumber", "%e%Amount must be a number.");
        cDV("notinvillage", "%e%You aren't in a Village.");
        cDV("cantfindvillage", "%e%Couldn't find Village.");
        cDV("notenoughmoney", "%e%You don't have %n%.");
        cDV("depositedmoney", "%i%%p% %d%deposited %i%%n% %d%into the Village Bank.");
        cDV("withdrawledmoney", "%i%%p% %d%withdrew %i%%n% %d%from the Village Bank.");
        cDV("leavevillagemayor", "%e%You cannot leave the Village, you are the mayor.");
        cDV("closevillagenotmayor", "%e%You cannot close the Village, you are not the mayor.");
        cDV("leftvillage", "%i%%p% %d%left the Village");
        cDV("goingtovillage", "%d%Going to Village square.");
        cDV("enterdescription", "%e%Please enter a description.");
        cDV("notmayordescription", "%e%Only the mayor can change the Village description");
        cDV("newdescription", "%i%The new Village description is: %d%%description%");
        cDV("entermessage", "%e%Please enter a message.");
        cDV("enterkickname", "%e%Enter the name of the player to kick.");
        cDV("mayorkickonly", "%e%Only the mayor can kick players.");
        cDV("cantkickmayor", "%e%You cannot kick the mayor of the Village.");
        cDV("notresident", "%e%%p% isn't a resident the Village.");
        cDV("residentkicked", "%i%%p% %d%was kicked from the Village.");
        cDV("enterchunks", "%e%Enter the amount of chunks to expand.");
        cDV("onlymayorexpand", "%e%Only the mayor can expand the Village.");
        cDV("villagebankneedmore", "%e%You don't have enough money in the Village bank! You need %n% to do this.");
        cDV("expandvillageoverlap", "%e%You cannot expand the Village this much, it overlaps another Village.");
        cDV("createvillageoverlap", "%e%You can't create a Village here, it overlaps another Village.");
        cDV("villageexpanded", "%d%The Village was expanded %i%%n% %d%chunk(s).");
        cDV("playeronly", "%e%Only players can do this.");
        cDV("entervillagename", "%e%Please enter a Village name.");
        cDV("invalidvillagename", "%e%Invalid Village name, name can only have letters and/or numbers.");
        cDV("villagenameused", "%e%This Village name is already in use or is reserved.");
        cDV("alreadyinvillage", "%e%You are already in a Village.");
        cDV("createdvillage", "%i%%p% %d%created the Village %i%%v%%d%!");
        cDV("enterplayer", "%e%Please enter a player name.");
        cDV("villageinvite", "%i%%p% %d%has invited you to join %i%%v%%d%,");
        cDV("residentinvited", "%i%%p% %d%was invited to the Village.");
        cDV("noinvite", "%e%You haven't received an invite.");
        cDV("joinedvillage", "%i%%p% %d%joined the village.");
        cDV("villageclosed", "%d%The Village %i%%v% %d%fell into Anarchy!");
        cDV("onlymayorbank", "%e%Only the mayor can edit the village bank.");
        cDV("cantcontinueitems", "%e%The village doesn't have the necessary items to continue!");
        cDV("cantcontinuemoney", "%e%The village doesn't have enough money to continue!");
        cDV("cantattackwilderness", "%e%Cannot attack players who aren't in a village.");
        cDV("cantattacksamevillage", "%e%Cannot attack players who are in the same village as you.");
        cDV("cantattackdifferentvillage", "%e%Cannot attack players who are in a different village to you.");
        cDV("maxofthreeexpand", "%e%You can only expand 3 chunks at a time.");
        cDV("expandingvillage", "%i%Expanding Village... please wait...");
        cDV("expandregionoverlap", "%e%Can't expand village, the expansion overlaps a region.");
        cDV("createvillageregionoverlap", "%e%Can't create village here, it overlaps a region.");
        cDV("notenougharguments", "%e%Not enough arguments.");
        cDV("error", "%e%An Error occured! please contact the admin.");
        cDV("claimedchunkinfo", "%i%%p% %d%has claimed this plot.");
        cDV("claimedchunk", "%d%Claimed %x%, %z%!");
        cDV("chunkavailable", "%d%This chunk is available!");
        cDV("onlymayorplot", "%e%Only the mayor can edit plots.");
        cDV("playernotfound", "%e%Couldn't find %p%!");
        cDV("setplotowner", "%d%Set the plot owner as %i%%p%");
        cDV("chunkclaimed", "%d%The %i%Mayor %d%has claimed %i%%x%, %z%%d% for you.");
        cDV("plotnotinvillage", "%e%This plot is not in the Village.");
        cDV("notmoney", "%e%The entered number was not a valid amount of money.");
        cDV("setplotprice", "%d%Set the plot price to %i%%n%");
        cDV("invalidargument", "%e%Invalid argument supplied.");
        cDV("chunkclaimedbyplayer", "%e%This plot is claimed by another player.");
        cDV("chunknotowned", "%e%You don't own this plot.");
        cDV("nopermission", "%e%You don't have permission for this.");
        cDV("nexttaxday", "The next tax day for %i%%tax% %d%is in %i%%date%%d%.");
        cDV("notaxes", "There are no taxes!");
        cDV("taxes", "%i%Taxes, type /taxamount [name] for more info.");
        cDV("cantfindtax", "%e%Can't find a tax by that name.");
        cDV("taxesdue", "%i%Taxes due for %tax%:");
        cDV("notinthisworld", "%e%You can't do that in this world!");
        cDV("onlymayorexplode", "%e%Only the mayor can explode the Village.");
        cDV("villageexploded", "%i%The Village was Exploded!");
        cDV("playernotinvillage", "%d%Player %i%%p%%d% isn't in part of any Village.");
        cDV("playervillage", "%d%Player %i%%p%%d% is in Village %i%%v%%d%.");

        //Save YML
        this.save();
    }
    
    @Override
    public void trySave() throws IOException {
        language.save(languageFile);
    }
    
    public void cDV(String key, Object defaultValue) {
        if(!language.contains(key)) {
            language.set(key, defaultValue);
        }
        defaultLanguage.set(key, defaultValue);
    }
}
