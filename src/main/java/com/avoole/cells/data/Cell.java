package com.avoole.cells.data;

public class Cell {

    public String id;

    public float hp;

    public int color;

    public Vec2 position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public void setState(Cell cell){
        this.hp = cell.hp;
        this.color = cell.color;
        this.position = cell.position;
    }
}
