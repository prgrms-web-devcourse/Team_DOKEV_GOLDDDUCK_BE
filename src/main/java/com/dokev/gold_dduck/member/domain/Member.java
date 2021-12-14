package com.dokev.gold_dduck.member.domain;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.event.domain.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(of = {"id", "name", "email"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Column(name = "email", length = 30)
    private String email;

    @Column(name = "provider", nullable = false, length = 30)
    private String provider;

    @Column(name = "social_id", nullable = false, length = 30)
    private String socialId;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @OneToMany(mappedBy = "member")
    private final List<Event> events = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public Member(String name, String email, String socialId, String profileImage) {
        this.name = name;
        this.email = email;
        this.socialId = socialId;
        this.profileImage = profileImage;
    }

    public Member(String name, String provider, String socialId, String profileImage, Group group) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(provider, "provider must not be null");
        Objects.requireNonNull(socialId, "socialId must not be null");
        Objects.requireNonNull(group, "group must not be null");

        this.name = name;
        this.provider = provider;
        this.socialId = socialId;
        this.profileImage = profileImage;
        this.group = group;
    }

    public Optional<String> getProfileImage() {
        return Optional.ofNullable(profileImage);
    }
}
