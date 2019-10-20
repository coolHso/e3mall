package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

/**
 * @author 10642
 */
public interface SearchService {
    SearchResult search(String keyword,int page, int rows);
}
