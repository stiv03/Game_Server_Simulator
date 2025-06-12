package org.example.game.service;

import org.example.game.model.Item;
import org.example.game.model.Player;
import org.example.persistence.entity.GameSession;

public interface ItemService {

    Item spawnItemsForSession(GameSession session);
    void applyEffect(Item item, Player player);
}
