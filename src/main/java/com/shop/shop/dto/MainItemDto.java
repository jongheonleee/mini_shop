package com.shop.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {

    private Long id;
    private String itemNm;
    private String itemDetail;
    private String imgUrl;
    private Integer price;


    // 해당 어노테이션을 선언하여 Querydsl로 결과 조회시 바로 DTO로 받아오도록 처리
    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
