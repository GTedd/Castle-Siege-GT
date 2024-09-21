package me.cbhud.castlesiege.state;
import me.cbhud.castlesiege.CastleSiege;


public class Game {
    private final CastleSiege plugin; // Add this field
    private GameState currentState;

    public Game(CastleSiege plugin) {
        this.plugin = plugin; // Initialize the field

        // Set the initial state to LOBBY
        currentState = GameState.LOBBY;
    }

    public void setState(GameState newState) {
        this.currentState = newState;
        plugin.getScoreboardManager().updateScoreboardForAll();
    }

    public GameState getState() {
        return currentState;
    }

}


