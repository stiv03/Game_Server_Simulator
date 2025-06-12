package org.example.persistence.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.example.persistence.entity.Users;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsersRepository {

    private static final String FIND_ALL_QUERY = "SELECT u FROM Users u";
    private static final String FIND_BY_USERNAME = "SELECT u FROM Users u WHERE u.{0}= :{0}";
    private static final String COUNT_USERS_BY_COLUMN = "SELECT COUNT(u) FROM Users u WHERE u.{0} = :{0}";

    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Users save(Users user) {
        entityManager.persist(user);
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<Users> findById(UUID id) {
        Users user = entityManager.find(Users.class, id);
        return Optional.ofNullable(user);
    }

    @Transactional(readOnly = true)
    public Optional<Users> findByUsername(String username) {
        return entityManager.createQuery(MessageFormat.format(FIND_BY_USERNAME, COLUMN_USERNAME), Users.class)
                .setParameter(COLUMN_USERNAME, username)
                .getResultStream()
                .findFirst();
    }

    @Transactional(readOnly = true)
    public List<Users> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, Users.class)
                .getResultList();
    }

    @Transactional
    public void delete(Users user) {
        Users foundUser = entityManager.find(Users.class, user.getId());
        if (foundUser != null) {
            entityManager.remove(foundUser);
        }
    }

    @Transactional
    public Users update(Users user) {
        return entityManager.merge(user);
    }

    @Transactional(readOnly = true)
    public boolean isEmailRegistered(String email) {
        String query = MessageFormat.format(COUNT_USERS_BY_COLUMN, COLUMN_EMAIL);
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter(COLUMN_EMAIL, email)
                .getSingleResult();
        return count > 0;
    }

    @Transactional(readOnly = true)
    public boolean isUsernameRegistered(String username) {
        String query = MessageFormat.format(COUNT_USERS_BY_COLUMN, COLUMN_USERNAME);
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter(COLUMN_USERNAME, username)
                .getSingleResult();
        return count > 0;
    }
}