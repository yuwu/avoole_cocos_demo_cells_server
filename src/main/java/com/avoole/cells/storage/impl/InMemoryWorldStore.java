package com.avoole.cells.storage.impl;

import com.avoole.cells.Client;
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
    private final AtomicInteger atomic = new AtomicInteger(1);

    private ConcurrentHashMap<String, Cell> cells = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    private Color[] colors;
    private int width;
    private int height;

    public InMemoryWorldStore() {
        this.width = 20;
        this.height = 20;
        this.colors = new Color[]{Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA};
        this.initCells();
    }

    private void initCells(){
        for(int i=0; i<50; i++){
            float x = (float)(Math.random() * (this.width-0.01));
            float y = (float)(Math.random() * (this.height-0.01));

            x = InMemoryWorldStore.range(x, 0.01f, this.width);

            Cell cell = new Cell();
            cell.setId(getUniqueId());
            cell.setPosition(new Vec2(x, y));
            cell.setHp(0.5f);
            Color color = this.getRandomColor();
            cell.setColor(color.getValue());

            this.cells.put(cell.getId(), cell);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public List<Cell> getCells() {
        return cells.values().stream().collect(Collectors.toList());
    }

    @Override
    public boolean hasCell(Cell cell) {
        if(cell == null || cell.getId() == null) return false;
        return this.cells.containsKey(cell.getId());
    }

    @Override
    public void removeCell(Cell cell) {
        this.cells.remove(cell.getId(), cell);
    }

    @Override
    public List<Player> getPlayers() {
        return players.values().stream().collect(Collectors.toList());
    }

    @Override
    public Player getPlayer(String id) {
        return players.get(id);
    }

    @Override
    public Player getPlayer(Client client) {
        List<Player> players = getPlayers();
        for(Player player : players){
            if(client.equals(player.getClient())){
                return player;
            }
        }
        return null;
    }

    @Override
    public void updatePlayer(Player player) {
        Player oldPlayer = players.get(player.getId());
        if(oldPlayer != null) {
            oldPlayer.setState(player);
        }
    }

    @Override
    public boolean hasPlayer(Player player) {
        if(player == null || player.getId() == null) return false;
        return this.players.containsKey(player.getId());
    }

    @Override
    public void addPlayer(Player player) {
        if(hasPlayer(player)){
            return;
        }
        this.players.put(player.getId(), player);
    }

    @Override
    public void removePlayer(Client client) {
        Player player = this.getPlayer(client);
        if(player != null){
            this.removePlayer(player);
        }
    }

    @Override
    public void removePlayer(Player player) {
        this.players.remove(player.getId(), player);
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

    public String getUniqueId(){
        return String.valueOf(atomic.getAndIncrement());
    }
}
