package com.wujiuye.mybatisplus;

import com.wujiuye.mybatisplus.dao.CompanyMapper;
import com.wujiuye.mybatisplus.model.Company;
import com.wujiuye.mybatisplus.service.CompanyService;
import com.wujiuye.mybatisplus.service.CompanyServiceNotInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class MybatisplusStuApplication {

    public static void main(String[] args) {
        try {
            ApplicationContext applicationContext = SpringApplication.run(MybatisplusStuApplication.class);
            CompanyMapper companyMapper = applicationContext.getBean(CompanyMapper.class);
            System.out.println(companyMapper.selectById(3));
            companyMapper.selectBatchIds(Arrays.asList(3,4,5));

//            CompanyService companyService = applicationContext.getBean(CompanyService.class);
//            CompanyServiceNotInterface companyServiceNotInterface = applicationContext.getBean(CompanyServiceNotInterface.class);
//            Company company = companyService.selectById(4);
//            System.out.println(company);
//            Company company1 = companyServiceNotInterface.selectById(3);
//            System.out.println(company1);
//            companyServiceNotInterface.testBuildSqlQueue();
//            companyServiceNotInterface.testBuildSqlQueue2();
//            companyServiceNotInterface.testBuildSqlQueuePage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
