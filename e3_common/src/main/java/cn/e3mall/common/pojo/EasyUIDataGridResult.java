package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author 10642
 */
public class EasyUIDataGridResult implements Serializable{
    private static final long serialVersionUID = 6850234479872067759L;
    private Long total;
    private List rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
