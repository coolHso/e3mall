package cn.e3mall.item.pojo;

import cn.e3mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;

public class Item extends TbItem {

    public Item() {
    }

    public Item(TbItem tbItem){
        setId(tbItem.getId());
        setImage(tbItem.getImage());
        setSellPoint(tbItem.getSellPoint());
        setTitle(tbItem.getTitle());
        setCreated(tbItem.getCreated());
        setUpdated(tbItem.getUpdated());
        setStatus(tbItem.getStatus());
        setCid(tbItem.getCid());
        setNum(tbItem.getNum());
        setPrice(tbItem.getPrice());
        this.setNum(tbItem.getNum());
        this.setBarcode(tbItem.getBarcode());
    }

    public String[] getImages() {
        if (StringUtils.isNotBlank(this.getImage())){
            return getImage().split(",");
        }
        return null;
    }

}
