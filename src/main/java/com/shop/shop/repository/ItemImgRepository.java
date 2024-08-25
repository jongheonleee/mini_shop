package com.shop.shop.repository;

import com.shop.shop.entity.ItemImg;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
    ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn);
}
