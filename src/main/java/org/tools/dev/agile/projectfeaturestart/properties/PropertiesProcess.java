package org.tools.dev.agile.projectfeaturestart.properties;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertiesProcess {
    private String featureType;
    private String featureName;
//    private String branchDevelop;
//    private String branchRelease;
    private String branchSourceFeat;
    private String gitDirectory;

    @Override
    public String toString() {
        return "Variaveis para processamento. PropertiesProcess [" +
                "featureType='" + featureType + '\'' +
//                ", branchDevelop='" + branchDevelop + '\'' +
//                ", branchRelease='" + branchRelease + '\'' +
                ", branchSourceFeat='" + branchSourceFeat + '\'' +
                ", gitDirectory='" + gitDirectory + '\'' +
                '}';
    }

}
