package com.dokev.gold_dduck.member;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.event.Event;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Email
    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "social_id", nullable = false, length = 30)
    private String socialId;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @OneToMany(mappedBy = "member")
    @JoinColumn(name = "event_id")
    private List<Event> events = new ArrayList<>();

    public Optional<String> getProfileImage() {
        return Optional.ofNullable(profileImage);
    }
}
