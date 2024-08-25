package com.shop.shop.controller;



import com.shop.shop.dto.ItemFormDto;
import com.shop.shop.dto.ItemSearchDto;
import com.shop.shop.entity.Item;
import com.shop.shop.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
            Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList) {

        // 유효성 검증
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        // 첫번째 상품 등록은 이미지 필수로 넣어야함
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
            return "item/itemForm";
        }

        // 상품 저장
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록에 실패했습니다.");
            return "item/itemForm";
        }


        return "redirect:/";
    }

    // 상품 수정
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        // 조회한 상품 데이터를 모델에 담아서 뷰로 전달
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) { // 상품 엔티티가 없는 경우 에러 메시지 담아서 상품 등록 페이지로 이동
            model.addAttribute("errorMessage", "상품 정보가 없습니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
            Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정에 실패했습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer> page, Model model) {
        var pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    }


    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }

}
