package com.dokev.gold_dduck.common;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.domain.GiftType;
import com.dokev.gold_dduck.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("local")
public class LocalTestDBInitializer implements ApplicationRunner {

    private final InitDBService initDBService;

    public LocalTestDBInitializer(InitDBService initDBService) {
        this.initDBService = initDBService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initDBService.init();
        initDBService.init2();
    }

    @Service
    @Transactional
    static class InitDBService {

        private final EntityManager entityManager;

        InitDBService(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        public void init() {
            Member userMember = entityManager.find(Member.class, 1L);
            System.out.println("userMember = " + userMember);
            Event fifoEvent = Event.builder(
                "데브코스 1기 수료를 축하합니다",
                GiftChoiceType.FIFO,
                LocalDateTime.now().plusMinutes(1),
                LocalDateTime.now().plusYears(1),
                EventProgressStatus.RUNNING,
                "template1",
                5,
                3,
                2,
                userMember
            ).build();
            Gift gift = new Gift("커피", 3);
            gift.changeEvent(fifoEvent);

            List<GiftItem> giftItems = new ArrayList<>();
            for (int i = 0; i < 100; i++) {

                GiftItem giftItemText = new GiftItem(GiftType.TEXT,
                    "선물코드를 등록하여 선물을 받아보세요. (마르코 타운) ∙ 선물코드 : 3TLVAY2538 ∙ 선물명 : 카페아메리카노 ICED ∙ 코드등록 유효기간 : 2021.11.02 ∙ 코드등록 방법 : 카카오톡 > 선물하기 > 선물함 > 선물코드 등록 ∙ 등록 URL : http://kko.to/Aikttag4o",
                    false);
                GiftItem giftItemImage = new GiftItem(GiftType.IMAGE,
                    "https://dokev-gold-dduck.s3.ap-northeast-2.amazonaws.com/giftItemTest.jfif", false);

                giftItems.add(giftItemText);
                giftItems.add(giftItemImage);
            }

            giftItems.forEach(giftItem -> {
                giftItem.changeGift(gift);
            });

            entityManager.persist(fifoEvent);
        }

        public void init2() {
            Member userMember = entityManager.find(Member.class, 1L);
            Member adminMember = entityManager.find(Member.class, 2L);

            Event event1 = createEvent(adminMember, "데브코스 1기 수료를 축하합니다 선착순 이벤트!", "커피");
            Event event2 = createEvent(adminMember, "테스트 선착순 이벤트!", "치킨");
            Event event3 = createEvent(adminMember, "금나와라 선착순 이벤트!", "피자");
            Event event4 = createEvent(adminMember, "뚝딱 선착순 이벤트!", "케이크");

            Gift gift = event1.getGifts().get(0);
            GiftItem giftItem = gift.getGiftItems().get(0);
            giftItem.allocateMember(userMember);
            entityManager.persist(new EventLog(event1, userMember, gift, giftItem));
            Gift gift2 = event2.getGifts().get(0);
            GiftItem giftItem2 = gift2.getGiftItems().get(1);
            giftItem2.allocateMember(userMember);
            entityManager.persist(new EventLog(event2, userMember, gift2, giftItem2));
            Gift gift3 = event3.getGifts().get(0);
            GiftItem giftItem3 = gift3.getGiftItems().get(2);
            giftItem3.allocateMember(userMember);
            entityManager.persist(new EventLog(event3, userMember, gift3, giftItem3));
            Gift gift4 = event4.getGifts().get(0);
            GiftItem giftItem4 = gift4.getGiftItems().get(0);
            giftItem4.allocateMember(userMember);
            entityManager.persist(new EventLog(event4, userMember, gift4, giftItem4));

            Event randomEvent = Event.builder(
                "데브코스 1기 수료를 축하합니다 랜덤 이벤트!",
                GiftChoiceType.RANDOM,
                LocalDateTime.now().plusMinutes(1),
                LocalDateTime.now().plusMinutes(10),
                EventProgressStatus.RUNNING,
                "template2",
                60,
                6,
                54,
                adminMember
            ).build();
            entityManager.persist(randomEvent);

            Gift coffeeGift = new Gift("커피", 3);
            coffeeGift.changeEvent(randomEvent);
            GiftItem coffeeGiftItem = new GiftItem(GiftType.TEXT,
                "선물코드를 등록하여 선물을 받아보세요. (마르코 타운) ∙ 선물코드 : 3TLVAY2538 ∙ 선물명 : 카페아메리카노 ICED ∙ 코드등록 유효기간 : 2021.11.02 ∙ 코드등록 방법 : 카카오톡 > 선물하기 > 선물함 > 선물코드 등록 ∙ 등록 URL : http://kko.to/Aikttag4o",
                false);
            GiftItem coffeeGiftItem2 = new GiftItem(GiftType.TEXT, "http://kko.to/Aikttag4o", false);
            GiftItem coffeeGiftItem3 = new GiftItem(GiftType.IMAGE,
                "https://dokev-gold-dduck.s3.ap-northeast-2.amazonaws.com/giftItemTest.jfif", false);
            coffeeGiftItem.changeGift(coffeeGift);
            coffeeGiftItem2.changeGift(coffeeGift);
            coffeeGiftItem3.changeGift(coffeeGift);
            entityManager.persist(coffeeGift);
            entityManager.persist(coffeeGiftItem);
            entityManager.persist(coffeeGiftItem2);
            entityManager.persist(coffeeGiftItem3);

            Gift chickenGift = new Gift("치킨", 3);
            chickenGift.changeEvent(randomEvent);
            GiftItem chickenGiftItem = new GiftItem(GiftType.TEXT, "맛 좋은 치킨", false);
            GiftItem chickenGiftItem2 = new GiftItem(GiftType.TEXT,
                "https://dokev-gold-dduck.s3.ap-northeast-2.amazonaws.com/giftItemTest2.jfif", false);
            GiftItem chickenGiftItem3 = new GiftItem(GiftType.IMAGE,
                "https://dokev-gold-dduck.s3.ap-northeast-2.amazonaws.com/giftItemTest2.jfif", false);
            chickenGiftItem.changeGift(chickenGift);
            chickenGiftItem2.changeGift(chickenGift);
            chickenGiftItem3.changeGift(chickenGift);
            entityManager.persist(randomEvent);
            entityManager.persist(chickenGift);
            entityManager.persist(chickenGiftItem);
            entityManager.persist(chickenGiftItem2);
            entityManager.persist(chickenGiftItem3);

            Gift gift5 = randomEvent.getGifts().get(0);
            GiftItem giftItem5 = gift5.getGiftItems().get(0);
            giftItem5.allocateMember(userMember);
            entityManager.persist(new EventLog(randomEvent, userMember, gift5, giftItem5));

        }

        private Event createEvent(Member createMember, String eventTitle, String category) {
            Event fifoEvent = Event.builder(
                eventTitle,
                GiftChoiceType.FIFO,
                LocalDateTime.now().plusMinutes(1),
                LocalDateTime.now().plusMinutes(10),
                EventProgressStatus.RUNNING,
                "template1",
                5,
                3,
                2,
                createMember
            ).build();
            entityManager.persist(fifoEvent);

            Gift pizzaGift = new Gift(category, 3);
            pizzaGift.changeEvent(fifoEvent);
            GiftItem pizzGiftItem = new GiftItem(GiftType.TEXT,
                "선물코드를 등록하여 선물을 받아보세요. (마르코 타운) ∙ 선물코드 : 3TLVAY2538 ∙ 선물명 : 카페아메리카노 ICED ∙ 코드등록 유효기간 : 2021.11.02 ∙ 코드등록 방법 : 카카오톡 > 선물하기 > 선물함 > 선물코드 등록 ∙ 등록 URL : http://kko.to/Aikttag4o",
                false);
            GiftItem pizzGiftItem2 = new GiftItem(GiftType.TEXT, "http://kko.to/Aikttag4o", false);
            GiftItem pizzGiftItem3 = new GiftItem(GiftType.IMAGE,
                "https://dokev-gold-dduck.s3.ap-northeast-2.amazonaws.com/giftItemTest.jfif", false);
            pizzGiftItem.changeGift(pizzaGift);
            pizzGiftItem2.changeGift(pizzaGift);
            pizzGiftItem3.changeGift(pizzaGift);
            entityManager.persist(pizzaGift);
            entityManager.persist(pizzGiftItem);
            entityManager.persist(pizzGiftItem2);
            entityManager.persist(pizzGiftItem3);
            return fifoEvent;
        }
    }
}
