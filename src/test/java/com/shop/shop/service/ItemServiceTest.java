package com.shop.shop.service;

import static org.junit.jupiter.api.Assertions.*;

import com.shop.shop.constant.ItemSellStatus;
import com.shop.shop.dto.ItemFormDto;
import com.shop.shop.entity.Item;
import com.shop.shop.entity.ItemImg;
import com.shop.shop.repository.ItemImgRepository;
import com.shop.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i=0; i<5; i++) {
            var path = "/Users/qwefghnm1212/desktop/shop/item/";
            var imgName = "image" + i + ".jpg";

            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imgName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void saveItem() throws Exception {
        // given
        var itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품 입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);


        var multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // when
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertEquals(itemFormDto.getItemNm(), item.getItemNm());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
    }
}