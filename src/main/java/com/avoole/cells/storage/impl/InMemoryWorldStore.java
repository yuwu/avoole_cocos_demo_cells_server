package com.avoole.cells.storage.impl;

import com.avoole.cells.data.Cell;
import com.avoole.cells.data.Color;
import com.avoole.cells.data.Player;
import com.avoole.cells.data.Vec2;
import com.avoole.cells.storage.WorldStore;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryWorldStore implements WorldStore {
    private final AtomicInteger atomic = new AtomicInteger(0);

    private ConcurrentHashMap<String, Cell> cells = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    private Color[] colors;
    private int width;
    private int height;

    public InMemoryWorldStore() {
        this.width = 50;
        this.height = 50;
        this.colors = new Color[]{Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA};
        this.initCells();
    }

    private void initCells(){
        for(int i=0; i<500; i++){
            float x = (float)(Math.random() * (this.width-0.01));
            float y = (float)(Math.random() * (this.height-0.01));

            x = InMemoryWorldStore.range(x, 0.01f, this.width);

            Cell cell = new Cell();
            cell.setId(String.valueOf(atomic.getAndIncrement()));
            cell.setPosition(new Vec2(x, y));
            cell.setHp(0.5f);
            Color color = this.getRandomColor();
            cell.setColor(color.getValue());

            this.cells.put(cell.getId(), cell);
        }
    }

    @Override
    public List<Cell> getCells() {
        return cells.values().stream().collect(Collectors.toList());
    }

    @Override
    public void removeCells(String id) {
        this.cells.remove(id);
    }

    @Override
    public List<Player> getPlayers() {
        return players.values().stream().collect(Collectors.toList());
    }

    @Override
    public Player getPlayer(String id) {
        return this.players.get(id);
    }

    @Override
    public boolean hasPlayer(Player player) {
        return this.players.containsKey(player.getId());
    }

    @Override
    public void addPlayer(Player player) {
        this.players.put(player.getId(), player);
    }

    @Override
    public void removePlayer(String id) {
        this.players.remove(id);
    }


    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }

    public static float range(float value , float min, float max){
        return Math.min(Math.max(value, min), max);
    }

    Color getRandomColor(){
        int index = (int)(Math.random() * (this.colors.length-1));
        return this.colors[index];
    }
}
