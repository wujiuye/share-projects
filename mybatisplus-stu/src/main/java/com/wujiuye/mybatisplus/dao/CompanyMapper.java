package com.wujiuye.mybatisplus.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wujiuye.mybatisplus.model.Company;
import org.apache.ibatis.annotations.Mapper;

/**
 * 需要继承BaseMapper接口
 */
@Mapper
public interface CompanyMapper extends BaseMapper<Company> {

    void selectBy(String name);

}
