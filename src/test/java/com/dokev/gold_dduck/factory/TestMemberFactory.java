package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.member.domain.Group;
import com.dokev.gold_dduck.member.domain.GroupPermission;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.domain.Permission;
import com.dokev.gold_dduck.member.domain.RoleGroupType;
import com.dokev.gold_dduck.member.domain.RoleType;
import javax.persistence.EntityManager;

public class TestMemberFactory {

    public static Member createTestMember(EntityManager entityManager) {
        Group group = entityManager.find(Group.class, 1L);
        return new Member("dokev", "kakao", "id123", "http://dokev/image.jpg", group);
    }

    public static Member getUserMember(EntityManager entityManager) {
        return entityManager.find(Member.class, 1L);
    }

    public static Member getAdminMember(EntityManager entityManager) {
        return entityManager.find(Member.class, 2L);
    }
}
