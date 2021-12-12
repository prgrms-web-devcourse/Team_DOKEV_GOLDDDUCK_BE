package com.dokev.gold_dduck.common;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PaginationDto {

    private int totalPages;

    private long totalElements;

    private int currentPage;

    private long offset;

    private int size;

    public PaginationDto(Page page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getPageable().getPageNumber();
        this.offset = page.getPageable().getOffset();
        this.size = page.getSize();
    }
}
