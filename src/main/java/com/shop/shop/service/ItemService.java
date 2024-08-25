package com.shop.shop.service;

import com.shop.shop.dto.ItemFormDto;
import com.shop.shop.dto.ItemImgDto;
import com.shop.shop.dto.ItemSearchDto;
import com.shop.shop.dto.MainItemDto;
import com.shop.shop.entity.Item;
import com.shop.shop.entity.ItemImg;
import com.shop.shop.repository.ItemImgRepository;
import com.shop.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    // 상품 등록
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        var item = itemFormDto.createItem();
        itemRepository.save(item);

        // 이미지 등록
        for (int i=0; i<itemImgFileList.size(); i++) {
            var itemImg = new ItemImg();
            itemImg.setItem(item);

            // 대표 이미지 설정
            if (i == 0) {
                itemImg.setRepImgYn("Y");
            } else {
                itemImg.setRepImgYn("N");
            }

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();

    }

    // 상품 이미지 조회
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
        // id로 상품 이미지 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // 엔티티를 DTO로 변환
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            var itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 상품 FORM DTO 반환
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        var itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        // 상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        // 이미지 등록
        for (int i=0; i<itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();

    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto,
            Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
