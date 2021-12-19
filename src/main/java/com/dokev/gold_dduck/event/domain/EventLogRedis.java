package com.dokev.gold_dduck.event.domain;

import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RedisHash("event_log")
public class EventLogRedis {

    @Id
    private Long id;

    @Indexed
    private Long eventId;

    private Long memberId;

    private Long giftId;

    private Long giftItemId;

    public EventLogRedis(Long eventId, Long memberId, Long giftId, Long giftItemId) {
        this.eventId = eventId;
        this.memberId = memberId;
        this.giftId = giftId;
        this.giftItemId = giftItemId;
    }
}
