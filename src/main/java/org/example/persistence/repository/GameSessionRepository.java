package org.example.persistence.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.persistence.entity.GameSession;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GameSessionRepository {

    private static final String FIND_ALL_QUERY = "SELECT gs FROM GameSession gs";
    private static final String FIND_ALL_RUNNING_QUERY = "SELECT gs FROM GameSession gs WHERE gs.endedAt IS NULL";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public GameSession save(GameSession session) {
        entityManager.persist(session);
        return session;
    }

    @Transactional
    public GameSession update(GameSession session) {
        return entityManager.merge(session);
    }

    @Transactional(readOnly = true)
    public Optional<GameSession> findById(UUID id) {
        GameSession session = entityManager.find(GameSession.class, id);
        return Optional.ofNullable(session);
    }

    @Transactional(readOnly = true)
    public List<GameSession> findAllRunning() {
        return entityManager.createQuery(FIND_ALL_RUNNING_QUERY, GameSession.class).getResultList();
    }


    @Transactional(readOnly = true)
    public List<GameSession> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, GameSession.class).getResultList();
    }

    @Transactional
    public void delete(GameSession session) {
        GameSession found = entityManager.find(GameSession.class, session.getId());
        if (found != null) {
            entityManager.remove(found);
        }
    }
}
