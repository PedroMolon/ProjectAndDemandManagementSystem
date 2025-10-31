package com.pedromolon.demoprojectanddemandmanagementsystem.entity.specification;

import com.pedromolon.demoprojectanddemandmanagementsystem.entity.Task;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Priority;
import com.pedromolon.demoprojectanddemandmanagementsystem.entity.enums.Status;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<Task> filterTasks(
            Status status,
            Priority priority,
            Long projectId
    ) {
        return (root, query, criterialBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criterialBuilder.equal(root.get("status"), status));
            }

            if (priority != null) {
                predicates.add(criterialBuilder.equal(root.get("priority"), priority));
            }

            if (projectId != null) {
                predicates.add(criterialBuilder.equal(root.get("project").get("id"), projectId));
            }

            return criterialBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
