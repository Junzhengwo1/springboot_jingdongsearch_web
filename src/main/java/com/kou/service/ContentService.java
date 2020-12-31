package com.kou.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Joaquin
 */
public interface ContentService {


    /**
     * 1.解析数据放入到es索引中
     *
     * @param keywords url后面跟得关键字，比如说我们在网页上搜索得关键字
     * @return
     */
    public Boolean parseContent(String keywords) throws IOException;

    /**
     * 获取这些数据实现搜索功能
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Map<String,Object>> searchPage(String keyword,Integer pageNo,Integer pageSize ) throws IOException;


}
