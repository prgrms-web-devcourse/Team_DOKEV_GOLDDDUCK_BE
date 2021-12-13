package com.dokev.gold_dduck.common;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PaginationDto {

    private final int totalPages;

    private final long totalElements;

    private final int currentPage;

    private final long offset;

    private final int size;

    public PaginationDto(Page page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getPageable().getPageNumber();
        this.offset = page.getPageable().getOffset();
        this.size = page.getSize();
    }
}
