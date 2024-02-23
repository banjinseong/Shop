package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Clothes;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.ItemImage;
import jpabook.jpashop.dto.ClothesDTO;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ResourceLoader resourceLoader;


    @Transactional
    public Long saveItem(ClothesDTO dto, MultipartFile[] multipartFiles) throws IOException {
        Clothes item = (Clothes) dto.toEntity();
        Resource resourcePath = resourceLoader.getResource("classpath:/static/img/");

        //해당 path에 폴더가 없으면 읽지를 못한다(리소스경로를우째절대경로로..)
        File directory = resourcePath.getFile();
        if (!directory.exists()) {
            directory.mkdirs(); // 폴더 생성
        }
        String directoryPath = directory.getAbsolutePath();
        System.out.println("*******");

        if(!Objects.isNull(multipartFiles)){
            for(MultipartFile file : multipartFiles){
                if(file.isEmpty()){
                    break;
                }
                String originName = file.getOriginalFilename();

                //식별자 번호 추가
                UUID uuid = UUID.randomUUID();
                String imgName = uuid + "_" + originName;

                String imgUrl = directoryPath + "/" + imgName;

                //이미지 저장
                file.transferTo(new File(imgUrl));

                //url호출할때는 시작점을 상대주소로 호출(전체경로로 하면 안나와서 일단 이렇게)
                ItemImage itemImage = ItemImage.createImage(originName,imgName,"/img/" + imgName);
                item.addImages(itemImage);
            }
        }


        itemRepository.save(item);

        return item.getId();
    }

    @Transactional
    public void updateItem(Long id, ClothesDTO dto){
        Item item = itemRepository.findById(id).get();
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setStockQuantity(dto.getStockQuantity());
        item.setModified_date(LocalDateTime.now());
    }


    public ClothesDTO findOne(Long itemId){
        Item id = itemRepository.findById(itemId).get();
        ClothesDTO dto = new ClothesDTO();
        dto.setName(id.getName());
        dto.setPrice(id.getPrice());
        dto.setStockQuantity(id.getStockQuantity());
        dto.setImages(id.getImages());
        return dto;
    }

    public List<Item> findAll(){
        return itemRepository.findAll();
    }
}
