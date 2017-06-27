package com.world.bolandian.firebasewithdatabase.models;

/**
 * Created by Bolandian on 23/06/2017.
 */

public class ShoppingLists {
    private String ownerUID;
    private String listUID;
    private String name;

    public ShoppingLists(){

    }

    public ShoppingLists(String ownerUID, String listUID, String name) {
        this.ownerUID = ownerUID;
        this.listUID = listUID;
        this.name = name;
    }

    public String getOwnerUID() {
        return ownerUID;
    }

    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }

    public String getListUID() {
        return listUID;
    }

    public void setListUID(String listUID) {
        this.listUID = listUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ShoppingLists{" +
                "ownerUID='" + ownerUID + '\'' +
                ", listUID='" + listUID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
