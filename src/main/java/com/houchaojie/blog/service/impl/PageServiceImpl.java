package com.houchaojie.blog.service.impl;

import com.houchaojie.blog.entity.Page;
import com.houchaojie.blog.mapper.PageMapper;
import com.houchaojie.blog.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 侯超杰
 */
@Service
public class PageServiceImpl implements PageService {
    @Autowired(required = false)
    private PageMapper pageMapper;

    @Override
    public Page getPageByKey(Integer status, String key) {
        return pageMapper.getPageByKey(status, key);
    }

    @Override
    @Cacheable(value = "default", key = "'page:'+#id")
    public Page getPageById(Integer id) {
        return pageMapper.getPageById(id);
    }

    @Override
    public List<Page> listPage(Integer status) {
        return pageMapper.listPage(status);
    }


    @Override
    @CachePut(value = "default", key = "'page:'+#page.pageId")
    public void insertPage(Page page) {
        pageMapper.insert(page);
    }

    @Override
    @CacheEvict(value = "default", key = "'page:'+#id")
    public void deletePage(Integer id) {
        pageMapper.deleteById(id);
    }

    @Override
    @CacheEvict(value = "default", key = "'page:'+#page.pageId")
    public void updatePage(Page page) {
        pageMapper.update(page);
    }
}
