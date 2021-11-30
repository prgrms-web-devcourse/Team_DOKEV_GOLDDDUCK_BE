package com.dokev.gold_dduck.event;

import com.dokev.gold_dduck.common.BaseEntity;
import com.dokev.gold_dduck.gift.Gift;
import com.dokev.gold_dduck.member.Member;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "event")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_type", length = 20)
    private String eventType;

    @Column(name = "start_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime startAt;

    @Column(name = "end_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime endAt;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "progress_status", nullable = false, length = 30)
    private EventProgressStatus eventProgressStatus;

    @Column(name = "main_template", length = 30)
    private String mainTemplate;

    @Column(name = "max_participant_count")
    private Integer maxParticipantCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "event")
    private List<Gift> gifts = new ArrayList<>();

    public void changeMember(Member member) {
        if (Objects.nonNull(this.member)) {
            this.member.getEvents().remove(this);
        }
        this.member = member;
        member.getEvents().add(this);
    }
}
