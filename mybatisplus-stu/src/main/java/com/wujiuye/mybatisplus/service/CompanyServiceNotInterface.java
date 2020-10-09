package com.wujiuye.mybatisplus.service;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wujiuye.mybatisplus.dao.CompanyMapper;
import com.wujiuye.mybatisplus.model.Company;
import org.springframework.stereotype.Service;

import java.util.List;

// 非接口方式，直接继承ServiceImpl类即可。
@Service
public class CompanyServiceNotInterface extends ServiceImpl<CompanyMapper, Company> {

    public void testBuildSqlQueue() {
        List<Company> companyList = this.selectList(new EntityWrapper<Company>()
                .between("id", 3, 6));
        companyList.stream().forEach(System.out::println);
    }

    public void testBuildSqlQueue2() {
        List<Company> companyList = this.selectList(new EntityWrapper<Company>()
                .between("create_tm", "2020-01-01", "2019-03-05"));
        companyList.stream().forEach(System.out::println);
    }

    public void testBuildSqlQueuePage() {
        Page<Company> pageResult = this.selectPage(new Page(1,2),
                new EntityWrapper<Company>().between("id", "1", "100"));
        System.out.println("current page:"+pageResult.getCurrent());
        System.out.println("page size:"+pageResult.getSize());
        System.out.println("total records:"+pageResult.getTotal());
        System.out.println("page count:"+pageResult.getPages());
        pageResult.getRecords().stream().forEach(System.out::println);
    }

}
