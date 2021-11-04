package me.dylzqn.xprison;

import me.dylzqn.xcraftapi.api.player.Rank;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Warp {

    private xPrisonCore prisonCore;
    private Integer ID;
    private String name;
    private Rank requiredRank;
    private WardType requiredWardType;
    private Location location;

    private HashMap<String, Object> dbData;

    public Warp(xPrisonCore prisonCore, String name, Rank requiredRank, WardType requiredWard, Location location) {
        this.prisonCore = prisonCore;
        this.name = name;
        this.requiredRank = requiredRank;
        this.requiredWardType = requiredWard;
        this.location = location;
        this.dbData = new HashMap<>();

        if (!prisonCore.getxCore().getMysqlDatabase().exists("xPrisonWarps", "Name", name)) {
            dbData.clear();
            dbData.put("Name", name);
            dbData.put("Rank", requiredRank);
            dbData.put("Ward", requiredWard);
            dbData.put("Location", prisonCore.deserialiseLoc(location));
            prisonCore.getApiCore().getxCraftCore().getMysqlDatabase().set("xPrisonWarps", dbData, "Name", name);
        }

        loadData();
    }

    public void loadData() {

        ResultSet rs = prisonCore.getApiCore().getxCraftCore().getMysqlDatabase().getSet("SELECT * FROM " + "xPrisonWarps" + " WHERE Name='" + name + "'");
        try {
            if (rs.next()) {
                this.ID = rs.getInt("WarpID");
                this.requiredRank = Rank.valueOf(rs.getString("Rank"));
                this.requiredWardType = WardType.valueOf(rs.getString("Ward"));
                this.location = prisonCore.serialiseLoc(rs.getString("Location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        dbData.clear();
        dbData.put("Rank", requiredRank);
        dbData.put("Ward", requiredWardType);
        dbData.put("Location", prisonCore.deserialiseLoc(location));
        prisonCore.getApiCore().getxCraftCore().getMysqlDatabase().set("xPrisonWarps", dbData, "Name", name);
    }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Rank getRequiredRank() {
        return requiredRank;
    }

    public WardType getRequiredWard() {
        return requiredWardType;
    }

    public Location getLocation() {
        return location;
    }

    public void setRequiredRank(Rank requiredRank) {
        this.requiredRank = requiredRank;
    }

    public void setRequiredWard(WardType requiredWardType) {
        this.requiredWardType = requiredWardType;
    }
}
