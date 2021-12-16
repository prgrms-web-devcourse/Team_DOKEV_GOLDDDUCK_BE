package com.dokev.gold_dduck.event.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GiftSaveDto {

    @NotNull
    private String category;

    @NotNull
    @NotEmpty
    private List<GiftItemSaveDto> giftItems;

}
