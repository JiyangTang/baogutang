package top.baogutang.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author nikooh
 */
@Slf4j
@EnableScheduling
@SpringBootApplication(scanBasePackages = "top.baogutang.*")
@MapperScan(basePackages = {"top.baogutang.admin.dao.mapper", "top.baogutang.common.dao.mapper"})
public class BaogutangAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaogutangAdminApplication.class, args);
        log.info("<<<<<<<<<<<<<<<==BAO_GU_TANG后台管理服务启动成功!!!==>>>>>>>>>>>>>>>");
    }

}
