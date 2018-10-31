package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.PayInfoDo;

public interface PayInfoDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayInfoDo record);

    int insertSelective(PayInfoDo record);

    PayInfoDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayInfoDo record);

    int updateByPrimaryKey(PayInfoDo record);
}