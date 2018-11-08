package com.avoole.cells.storage;

import com.avoole.cells.data.Cell;
import com.avoole.cells.data.Player;
import com.avoole.cells.data.Vec2;

import java.util.List;

public interface WorldStore {

    int getWidth();

    int getHeight();

    List<Cell> getCells();

    void removeCells(String id);

    List<Player> getPlayers();

    Player newPlayer();

    Player getPlayer(String id);

    boolean hasPlayer(Player player);

    void addPlayer(Player player);

    void removePlayer(String id);

    /**
     * Start the Session Store
     */
    void start();

    /**
     * Shutdown the Session Store
     */
    void shutdown();
}
