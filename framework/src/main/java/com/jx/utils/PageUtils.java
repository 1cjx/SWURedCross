package com.jx.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public class PageUtils {
    public static <T> Page listToPage(List<T> list, Long current, Long size){
        Page page = new Page();
        page.setCurrent(current);
        page.setSize(size);
        page.setTotal(list.size());
        int startIndex = (int)((current - 1)*size);
        if(list==null || list.isEmpty() || startIndex > list.size()){
            page.setRecords(null);
        }
        else {
            int toIndex = (int)(current*size);
            page.setRecords(list.subList(startIndex,toIndex > list.size() ? list.size() : toIndex));
        }
        return page;
    }
}
