package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
    private static final long serialVersionUID = -1911986622892245171L;
    private Integer totalPages;
    private Long recourdCount;
    private List<ItemSearch> itemList;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getRecourdCount() {
        return recourdCount;
    }

    public void setRecourdCount(Long recourdCount) {
        this.recourdCount = recourdCount;
    }

    public List<ItemSearch> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemSearch> itemList) {
        this.itemList = itemList;
    }
}
