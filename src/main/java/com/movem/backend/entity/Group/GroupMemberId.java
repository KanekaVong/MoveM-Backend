package com.movem.backend.entity.Group;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class GroupMemberId implements Serializable {

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "user_id")
    private Integer userId;

}