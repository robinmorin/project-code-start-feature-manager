package org.tools.dev.agile.projectfeaturestart.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties("app.default")
public class PropertiesDefault{
        private String featureType;
        private String branchDevelop;
        private String branchRelease;
}
