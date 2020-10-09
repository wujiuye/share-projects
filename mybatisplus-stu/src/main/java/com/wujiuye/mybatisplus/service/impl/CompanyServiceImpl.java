package com.wujiuye.mybatisplus.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wujiuye.mybatisplus.dao.CompanyMapper;
import com.wujiuye.mybatisplus.model.Company;
import com.wujiuye.mybatisplus.service.CompanyService;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

}
