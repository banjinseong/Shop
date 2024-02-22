package jpabook.jpashop.service;

import jakarta.transaction.Transactional;
import jpabook.jpashop.domain.item.Clothes;
import jpabook.jpashop.domain.item.ItemImage;
import jpabook.jpashop.dto.ClothesDTO;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ResourceLoader resourceLoader;


    @Test
    public void 이미지_삽입() throws Exception {
        //given
        ClothesDTO clothesDTO = new ClothesDTO();
        clothesDTO.setName("하이");
        clothesDTO.setStockQuantity(10);
        clothesDTO.setPrice(10000);
       MultipartFile file = createMockMultipartFile("C:/img/dog.jpg");
        MultipartFile[] multipartFiles = new MultipartFile[1];
        multipartFiles[0] = file;
        //when


        Long itemId = itemService.saveItem(clothesDTO);
        ItemImage itemImage = itemRepository.findById(itemId).get().getImages().get(0);
        //then
        System.out.println("*****"+itemImage.getId());
        System.out.println("*****"+itemImage.getImgUrl());
        System.out.println("*****"+itemImage.getOriginName());
        System.out.println("*****"+itemImage.getImgName());
    }

    public MultipartFile createMockMultipartFile(String filePath) throws IOException {
        // 파일 경로로부터 파일을 읽어들입니다.
        InputStream inputStream = new FileInputStream(filePath);

        // 파일 이름 추출
        String fileName = Paths.get(filePath).getFileName().toString();

        // MockMultipartFile 객체 생성
        String contentType = Files.probeContentType(Paths.get(filePath));
        MultipartFile multipartFile = new MockMultipartFile("file", fileName, contentType, inputStream);
        return multipartFile;
    }
}