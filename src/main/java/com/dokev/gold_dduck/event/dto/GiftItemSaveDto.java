package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.gift.domain.GiftType;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GiftItemSaveDto {

    @NotNull
    private GiftType giftType;

    private String content;

    private MultipartFile image;

    public GiftItemSaveDto(GiftType giftType, String content) {
        this.giftType = giftType;
        this.content = content;
    }

    public GiftItemSaveDto(GiftType giftType, MultipartFile image) {
        this.giftType = giftType;
        this.image = image;
    }

    public void changedContent(String content) {
        this.content = content;
    }
}
