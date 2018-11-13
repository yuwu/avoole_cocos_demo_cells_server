package com.avoole.cells.storage;

import com.avoole.cells.Client;
import com.avoole.cells.data.Cell;
import com.avoole.cells.data.Player;
import com.avoole.cells.data.Vec2;

import java.util.List;

public interface WorldStore {

    int getWidth();

    int getHeight();

    List<Cell> getCells();

    void removeCell(Cell cell);

    Player getPlayer(String id);

    Player getPlayer(Client client);

    List<Player> getPlayers();

    void updatePlayer(Player player);

    boolean hasPlayer(Player player);

    void addPlayer(Player player);

    void removePlayer(Client client);

    void removePlayer(Player player);

    /**
     * Start the Session Store
     */
    void start();

    /**
     * Shutdown the Session Store
     */
    void shutdown();
}
