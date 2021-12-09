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

    public static TestMembers givenMembers(EntityManager entityManager) {
        Permission roleUser = new Permission(RoleType.USER.getCode());
        Permission roleAdmin = new Permission(RoleType.ADMIN.getCode());
        Group userGroup = new Group(RoleGroupType.USER.getCode());
        Group adminGroup = new Group(RoleGroupType.ADMIN.getCode());
        GroupPermission userGroupPermission = new GroupPermission(userGroup, roleUser);
        GroupPermission adminGroupPermission = new GroupPermission(adminGroup, roleUser);
        GroupPermission adminGroupPermission2 = new GroupPermission(adminGroup, roleAdmin);
        Member userMember = new Member("user_dokev", "kakao", "id123", "http://dokev/image.jpg", userGroup);
        Member adminMember = new Member("admin_dokev", "kakao", "id456", "http://dokev/image.jpg", adminGroup);
        entityManager.persist(roleUser);
        entityManager.persist(roleAdmin);
        entityManager.persist(userGroup);
        entityManager.persist(adminGroup);
        entityManager.persist(userGroupPermission);
        entityManager.persist(adminGroupPermission);
        entityManager.persist(adminGroupPermission2);
        entityManager.persist(userMember);
        entityManager.persist(adminMember);
        return new TestMembers(userMember, adminMember);
    }


    public static class TestMembers {

        private final Member userMember;

        private final Member adminMember;

        public TestMembers(Member userMember, Member adminMember) {
            this.userMember = userMember;
            this.adminMember = adminMember;
        }

        public Member getUserMember() {
            return userMember;
        }

        public Member getAdminMember() {
            return adminMember;
        }
    }
}
