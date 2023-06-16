package top.baogutang.business;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 11:22
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "top.baogutang.*")
@MapperScan(basePackages = {"top.baogutang.business.dao.mapper", "top.baogutang.common.dao.mapper"})
public class BaogutangBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaogutangBusinessApplication.class, args);
        log.info("<<<<<<<<<<<<<<<==BAO_GU_TANG客户端服务启动成功!!!==>>>>>>>>>>>>>>>");
    }
}
