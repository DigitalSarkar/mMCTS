package com.mcts.app.model;

/**
 * Created by Raj on 10/2/2015.
 */
public class Religion {

    String id;
    String name;
    String isRisky;

    public String getIsRisky() {
        return isRisky;
    }

    public void setIsRisky(String isRisky) {
        this.isRisky = isRisky;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
