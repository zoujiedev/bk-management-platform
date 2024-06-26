package com.newsoft1024.mybatis.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.newsoft1024.mybatis.base.request.PageRequest;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * service基类
 * @param <T>  model类
 */
public abstract class BaseService<T> implements IBaseService<T> {

    public abstract BaseMapper<T> getBaseDao();

    @Override
    public T getById(Long id) {
        return getBaseDao().selectByPrimaryKey(id);
    }

    @Override
    public List<T> getAll() {
        return getBaseDao().selectAll();
    }

    public abstract Example getExample(T entity);

    @Override
    public PageInfo<T> getPageList(PageRequest<T> request) {
        if (request == null) {
            request = new PageRequest<>();
        }

        if (request.getPage() == 0) {
            request.setPage(1);
        }

        if (request.getPageSize() == 0) {
            request.setPageSize(20);
        }

        if (request.getOrderType() == null) {
            request.setOrderType(OrderType.DESC);
        }

        if (StringUtils.isEmpty(request.getOrderName())) {
            request.setOrderName("id");
        }

        PageHelper.orderBy(request.getOrderName() + " " + request.getOrderType());
        PageHelper.startPage(request.getPage(), request.getPageSize());
        List<T> list = getBaseDao().selectByExample(getExample(request.getEntity()));
        return new PageInfo<>(list);
    }

    @Override
    public void insert(T model) {
        getBaseDao().insert(model);
    }

    @Override
    public void update(T model) {
        getBaseDao().updateByPrimaryKey(model);
    }

    @Override
    public void delete(T model) {
        getBaseDao().delete(model);
    }

    @Override
    public List<T> selectByExample(T entity) {
        return getBaseDao().selectByExample(getExample(entity));
    }
}
