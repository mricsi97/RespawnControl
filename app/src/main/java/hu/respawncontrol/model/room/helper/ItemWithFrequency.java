package hu.respawncontrol.model.room.helper;

import androidx.room.Embedded;

import hu.respawncontrol.model.room.entity.Item;

public class ItemWithFrequency {
    @Embedded
    public Item item;
    public Integer frequency;
}
