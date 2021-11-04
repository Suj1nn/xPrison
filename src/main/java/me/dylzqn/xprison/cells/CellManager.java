package me.dylzqn.xprison.cells;

import me.dylzqn.xcraftapi.api.locations.Region;
import me.dylzqn.xcraftapi.api.locations.Selection;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * *******************************************************************
 * » Copyright Dylzqn (c) 2015. All rights Reserved.
 * » Any code contained within this document, and any associated APIs with similar branding
 * » are the sole property of Dylzqn. Distribution, reproduction, taking snippets, or
 * » claiming any contents as your own will break the terms of the licence, and void any
 * » agreements with you. the third party.
 * » Thanks :D
 * ********************************************************************
 */
public class CellManager {

    private xPrisonCore prisonCore;
    private List<xCell> cells;

    public CellManager(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
        this.cells = new ArrayList<>();

        ResultSet rs = prisonCore.getxCore().getMysqlDatabase().getSet("SELECT * FROM xPrisonCells;");
        try {
            if (rs != null) {
                while (rs.next()) {
                    Selection selection = new Selection(prisonCore.serialiseLoc(rs.getString("Loc1")), prisonCore.serialiseLoc(rs.getString("Loc2")));
                    Region region = new Region(prisonCore.getApiCore(), rs.getString("CellID"), selection, false);
                    xCell cell;
                    if (rs.getString("UUID") == null) {
                        createCell(rs.getString("CellID"), region, rs.getInt("Price"), prisonCore.serialiseLoc(rs.getString("SignLocation")), null);
                    } else {
                        cell = createCell(rs.getString("CellID"), region, rs.getInt("Price"), prisonCore.serialiseLoc(rs.getString("SignLocation")), UUID.fromString(rs.getString("UUID")));
                        cell.setEndTime(rs.getLong("EndTime"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (xCell cell : cells) {
            cell.updateCell();
        }
    }

    public xCell createCell(String ID, Region region, int price, Location signLoc, UUID ownerUUID) {
        xCell cell = new xCell(prisonCore, ID, region, price, signLoc, ownerUUID);
        cells.add(cell);
        return cell;
    }

    public xCell getCell(String ID) {
        for (xCell cell : cells) {
            if (cell.getID().equalsIgnoreCase(ID)) {
                return cell;
            }
        }
        return null;
    }

    public xCell getCell(Location location) {
        for (xCell cell : cells) {
            if (cell.getRegion().isLocationInRegion(location)) {
                return cell;
            }
        }
        return null;
    }

    public List<xCell> getCells() {
        return cells;
    }
}
