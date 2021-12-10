package com.dokev.gold_dduck.event.dto;

import com.dokev.gold_dduck.gift.domain.GiftType;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GiftItemSaveDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private GiftType giftType;

    private String content;

    private String fileName;

    private byte[] file;

    private MultipartFile image;

    public GiftItemSaveDto(GiftType giftType, String fileName, byte[] file) {
        this.giftType = giftType;
        this.fileName = fileName;
        this.file = file;
    }

    public GiftItemSaveDto(GiftType giftType, String content) {
        this.giftType = giftType;
        this.content = content;
    }

    public GiftItemSaveDto(GiftType giftType, MultipartFile image) {
        this.giftType = giftType;
        this.image = image;
        this.fileName = image.getOriginalFilename();
    }

    public void changedContent(String content) {
        this.content = content;
    }
}
