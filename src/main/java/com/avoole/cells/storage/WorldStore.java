package com.avoole.cells.storage;

import com.avoole.cells.data.Cell;
import com.avoole.cells.data.Player;

import java.util.List;

public interface WorldStore {

    List<Cell> getCells();

    void removeCells(String id);

    List<Player> getPlayers();

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
