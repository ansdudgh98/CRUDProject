package hoy.project;

import com.p6spy.engine.spy.P6SpyOptions;
import hoy.project.P6SpyPrettyFormatter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class P6SpyConfig {
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6SpyPrettyFormatter.class.getName());
    }
}
