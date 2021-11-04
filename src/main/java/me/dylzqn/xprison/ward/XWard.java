package me.dylzqn.xprison.ward;

import me.dylzqn.xcraftapi.api.locations.Region;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.Warp;
import me.dylzqn.xprison.xPrisonCore;

import java.util.List;

public class XWard implements Ward{

    private xPrisonCore prisonCore;

    public XWard(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public WardType getWardType() {
        return WardType.X;
    }

    @Override
    public String getName() {
        return "X";
    }

    @Override
    public String getPrefix() {
        return WardType.X.getPrefix();
    }

    @Override
    public String getSuffix() {
        return null;
    }

    @Override
    public Warp getWardSpawn() {
        return null;
    }

    @Override
    public Warp getWardMine() {
        return null;
    }

    @Override
    public Warp getWardLog() {
        return null;
    }

    @Override
    public List<Region> entranceLocations() {
        return null;
    }
}
