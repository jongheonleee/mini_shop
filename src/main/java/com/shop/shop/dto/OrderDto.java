package com.shop.shop.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderDto {

    @NotNull(message = "상품 아이디는 필수 입력값입니다.")
    private Long itemId;

    @Min(value = 1, message = "상품 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 999, message = "상품 수량은 최대 999개 이하이어야 합니다.")
    private int count;


}
