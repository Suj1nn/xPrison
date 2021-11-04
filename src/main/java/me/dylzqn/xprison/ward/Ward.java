package me.dylzqn.xprison.ward;

import me.dylzqn.xcraftapi.api.locations.Region;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.Warp;

import java.util.List;

public interface Ward {

    int getID();
    WardType getWardType();
    String getName();
    String getPrefix();
    String getSuffix();
    Warp getWardSpawn();
    Warp getWardMine();
    Warp getWardLog();
    List<Region> entranceLocations();

}
