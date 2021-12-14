package com.dokev.gold_dduck.member.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "role_group")
@Entity
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "group")
    private final List<GroupPermission> groupPermissions = new ArrayList<>();

    public List<GrantedAuthority> getAuthorities() {
        return groupPermissions.stream()
            .map(gp -> new SimpleGrantedAuthority(gp.getPermission().getName()))
            .collect(toList());
    }

    public Group(String name) {
        this.name = name;
    }
}
