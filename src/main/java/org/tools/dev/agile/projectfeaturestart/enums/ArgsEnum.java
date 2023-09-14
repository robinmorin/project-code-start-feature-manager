package org.tools.dev.agile.projectfeaturestart.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.tools.dev.agile.projectfeaturestart.helper.BeanStaticHelper;
import org.tools.dev.agile.projectfeaturestart.properties.PropertiesDefault;
import org.tools.dev.agile.projectfeaturestart.utils.GlobalConstants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ArgsEnum {

//    BRANCH_DEV("--branchDev",
//            "Nome da branch de Develop",
//            false,
//            "<branchDev>",
//            BeanStaticHelper.getBean(PropertiesDefault.class).getBranchDevelop()),
//    BRANCH_RELEASE("--branchRel",
//            "Nome da branch de Release",
//            false,
//            "<branchDev>",
//            BeanStaticHelper.getBean(PropertiesDefault.class).getBranchRelease()),
    BRANCH_SOURCE("--branchSource",
                "Nome da branch Origem pra criar as feature",
                  true,
                     "<branchRel>",
              "release"),
    GIT_DIRECTORY("--gitDirectory",
                 "Diretorio base onde estão todos os projetos",
                  true,
                      "<gitDirectory>",
                ""),
    FEATURE_TYPE("--featureType",
                    "Identifica o tipo de Branch a criar, seja uma fix ou feature, por padrão se assume 'feature'",
                      false,
                        "<featureType>",
                        BeanStaticHelper.getBean(PropertiesDefault.class).getFeatureType()),
    FEATURE_NAME("--featureName",
                    "Id da feature de negocio que sera iniciada",
                      true,
                         "<featureName",
                   "");

    private final String argument;
    private final String description;
    private final boolean required;
    private final String tag;
    private final String defaultValue;

    public static ArgsEnum getArgumentByText(String argument){
        return Arrays.stream(ArgsEnum.values())
                .filter(argsEnum -> argsEnum.argument.equalsIgnoreCase(argument))
                .findAny().orElse(null);
    }

    public static List<String> getRequiredArgs(){
        return Arrays.stream(ArgsEnum.values())
                .filter(ArgsEnum::isRequired)
                .map(ArgsEnum::getArgument)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\tArgumento    : \t").append(argument).append(GlobalConstants.CRLF);
        sb.append("\tDescrição    : \t").append(description).append(GlobalConstants.CRLF);
        sb.append("\tValor Padrão : \t").append(defaultValue).append(GlobalConstants.CRLF);
        return sb.toString();
    }
}
