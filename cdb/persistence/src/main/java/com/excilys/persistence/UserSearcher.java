package com.excilys.persistence;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.model.User;

@Repository
public class UserSearcher {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Optional<User> getUserDetails(@NonNull final String username) throws PersistanceException {
        try {
            CriteriaBuilder cb = this.em.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> userRoot = query.from(User.class);
            query.where(cb.equal(userRoot.get("username"), username));

            List<User> users = this.em.createQuery(query).getResultList();
            if (users.isEmpty()) {
                return Optional.empty();
            }

            User user = users.get(0);
            return Optional.of(user);
        } catch (RuntimeException e) {
            throw new PersistanceException(e);
        }
    }
}
