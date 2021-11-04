package me.dylzqn.xprison.commands.xcommand;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class xCommand {

    private xPrisonCore prisonCore;
    private String name;
    private Rank requiredRank;
    private WardType requiredWardType;
    private String syntax;
    private List<String> labels;
    private String description;

    public xCommand(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
        this.labels = new ArrayList<>();
    }

    public abstract boolean execute(CommandSender sender, String[] args) throws Exception;

    public xPrisonCore getPrisonCore() {
        return prisonCore;
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

    public List<String> getLabels() {
        return labels;
    }

    public String getSyntax() {
        return syntax;
    }

    public String getDescription() {
        return description;
    }

    public xCommand setName(String name) {
        this.name = name;
        return this;
    }

    public xCommand setRequiredRank(Rank requiredRank) {
        this.requiredRank = requiredRank;
        return this;
    }

    public xCommand setRequiredWard(WardType requiredWardType) {
        this.requiredWardType = requiredWardType;
        return this;
    }

    public xCommand setSyntax(String syntax) {
        this.syntax = syntax;
        return this;
    }

    public xCommand setDescription(String description) {
        this.description = description;
        return this;
    }
}
