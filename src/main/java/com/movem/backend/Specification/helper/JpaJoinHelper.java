package com.movem.backend.Specification.helper;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class JpaJoinHelper {


    public static Join<?,?> joinActivity(
            Root<?> root
    ){
        return root.join(
                "activity",
                JoinType.INNER
        );
    }

}
