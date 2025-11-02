package com.fiap.techchallenge14.user.specification;

import com.fiap.techchallenge14.user.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filterUsers(String name) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(
                        builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%")
                );
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
