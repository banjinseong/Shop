package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ItemImage {

    @Id
    @GeneratedValue
    @Column(name = "itemImg_id")
    private Long id;

    //원본 이미지 이름
    private String originName;

    //저장되는 이미지 이름
    private String imgName;

    //파일경로
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public static ItemImage createImage(String originName, String imgName, String imgUrl){
        ItemImage itemImage = new ItemImage();
        itemImage.originName = originName;
        itemImage.imgName = imgName;
        itemImage.imgUrl = imgUrl;
        return itemImage;
    }
}
