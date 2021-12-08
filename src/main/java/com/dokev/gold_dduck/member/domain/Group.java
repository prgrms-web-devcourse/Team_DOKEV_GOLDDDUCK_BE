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
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@Table(name = "groups")
@Entity
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "group")
    private List<GroupPermission> groupPermissions = new ArrayList<>();

    public List<GrantedAuthority> getAuthorities() {
        return groupPermissions.stream()
            .map(gp -> new SimpleGrantedAuthority(gp.getPermission().getName()))
            .collect(toList());
    }
}
