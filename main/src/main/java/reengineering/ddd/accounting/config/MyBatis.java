package reengineering.ddd.accounting.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"reengineering.ddd.accounting.mybatis", "reengineering.ddd.mybatis.support"})
public class MyBatis {
}
