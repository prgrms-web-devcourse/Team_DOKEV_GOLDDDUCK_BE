package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.member.domain.Group;
import com.dokev.gold_dduck.member.domain.GroupPermission;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.domain.Permission;
import com.dokev.gold_dduck.member.domain.RoleGroupType;
import com.dokev.gold_dduck.member.domain.RoleType;
import javax.persistence.EntityManager;

public class TestMemberFactory {

    public static Member createTestMember() {
        return new Member("dokev", "dokev@gmail.com", "id123", "http://dokev/image.jpg");
    }

    public static Member getUserMember(EntityManager entityManager) {
        return entityManager.find(Member.class, 1L);
    }

    public static Member getAdminMember(EntityManager entityManager) {
        return entityManager.find(Member.class, 2L);
    }
}
