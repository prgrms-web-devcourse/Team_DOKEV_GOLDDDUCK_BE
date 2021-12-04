package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.member.domain.Member;

public class TestMemberFactory {

    public static Member createTestMember() {
        return new Member("dokev", "dokev@gmail.com", "id123", "http://dokev/image.jpg");
    }
}
