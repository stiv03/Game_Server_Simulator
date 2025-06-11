package org.example.game.model;


import lombok.Getter;
import lombok.Setter;
import org.example.game.enums.Direction;
import org.example.game.enums.NpcType;
import org.example.game.event.AttackEvent;
import org.example.game.event.DamageEvent;
import org.example.game.event.MoveEvent;
import org.example.persistence.entity.GameSession;

import java.util.UUID;

@Getter
@Setter
public class Npc implements Entity {

    private final String id = UUID.randomUUID().toString();
    private final String name;

    private final NpcType type;

    private Position position;
    private int health = 50;
    @Getter
    private boolean defending = false;

    private final GameSession session;

    public Npc(NpcType type, Position position,GameSession session) {
        this.type = type;
        this.position = position;
        name = type.toString().toLowerCase() + id;
        this.session = session;
    }

    public void move(Direction direction){
        session.getEventQueue().offer(new MoveEvent(this, direction));

    }

    public void attack(Entity target) {
        session.getEventQueue().offer(new AttackEvent(this, target));
    }

    public void defend() {
        defending = true;
    }

    public synchronized void takeDamage(int amount) {
        session.getEventQueue().offer(new DamageEvent(this, amount));
    }

    public void onDeath() {

        //GAME OVER
    }

    public boolean isAlive() {
        return health > 0;
    }
}
