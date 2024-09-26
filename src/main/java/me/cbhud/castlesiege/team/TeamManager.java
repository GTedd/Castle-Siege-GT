package me.cbhud.castlesiege.team;

import me.cbhud.castlesiege.CastleSiege;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {
    private final CastleSiege plugin;
    private final Map<String, Team> playerTeams; // Map to store player UUIDs and their corresponding teams
    private final int maxPlayersPerTeam;

    public TeamManager(CastleSiege plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.playerTeams = new HashMap<>();

        // Read the maxPlayersPerTeam value from the config, default to 5 if not present
        this.maxPlayersPerTeam = config.getInt("maxPlayersPerTeam", 16);
    }

    public boolean joinTeam(Player player, Team team) {
        // Check if the team has reached the maximum number of players
        if (getPlayersInTeam(team) >= maxPlayersPerTeam) {
             player.sendMessage("§cThe " + plugin.getConfigManager().getTeamName(team) + " team is full!");
        }


        if (getTeam(player) == team){
            return false;
        }
        playerTeams.put(player.getUniqueId().toString(), team);
        player.sendMessage("§aYou have joined the " + plugin.getConfigManager().getTeamName(team) + " team");
        plugin.getPlayerKitManager().setDefaultKit(player);
        plugin.getScoreboardManager().updateLobbyScoreboardForTeam(team);

        return true;
    }

    public Team getTeam(Player player) {
        // Get the team for a player
        return playerTeams.get(player.getUniqueId().toString());
    }

    public Team removeTeam(Player player){
        return  playerTeams.put(player.getUniqueId().toString(), null);
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public int getPlayersInTeam(Team team) {
        // Count the number of players in the specified team
        return (int) playerTeams.values().stream().filter(t -> t == team).count();
    }

    public boolean tryRandomTeamJoin(Player player) {
            if (plugin.getTeamManager().getPlayersInTeam(Team.Attackers) < plugin.getTeamManager().getMaxPlayersPerTeam()) {
                plugin.getTeamManager().joinTeam(player, Team.Attackers);
                return true;
            }
            if (plugin.getTeamManager().getPlayersInTeam(Team.Defenders) < plugin.getTeamManager().getMaxPlayersPerTeam()) {
                plugin.getTeamManager().joinTeam(player, Team.Defenders);
                return true;
        }
        return false;
    }

}
