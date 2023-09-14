package org.tools.dev.agile.projectfeaturestart;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tools.dev.agile.projectfeaturestart.enums.ArgsEnum;
import org.tools.dev.agile.projectfeaturestart.properties.PropertiesProcess;
import org.tools.dev.agile.projectfeaturestart.service.ProcessService;
import org.tools.dev.agile.projectfeaturestart.utils.GlobalConstants;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@SpringBootApplication
public class ProjectFeatureStartApp implements CommandLineRunner {

    private static final String REFS_HEADS = "refs/heads/";
    private final ProcessService processService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectFeatureStartApp.class, args);
    }

    public void run(String... args) throws Exception {
        if(args == null || args.length == 0){
            System.out.println("Insuficientes argumentos pra iniciar a Feature.");
            System.out.println("Pode enviar --help para mais informações.");
        } else if (Arrays.stream(args).anyMatch(s -> s.contains("--help"))){
            System.out.printf("%nDocumentação dos argumentos pra iniciar as Feature.%n");
            showDocArgs();
            showDocArqsRequired();
        } else {

            var propertiesProcess = processArgs(args);

            String results = processService.execute(propertiesProcess);

            System.out.println(GlobalConstants.CRLF + "Resultado:" + GlobalConstants.CRLF);
            System.out.println(results);
        }
    }

    private PropertiesProcess processArgs(String...args) {
        var argsList = List.of(args);
        validateRequiredArgs(argsList);
        var propProcess = new PropertiesProcess();
//        propProcess.setBranchDevelop(REFS_HEADS + assignValue(argsList, ArgsEnum.BRANCH_DEV));
//        propProcess.setBranchRelease(REFS_HEADS + assignValue(argsList, ArgsEnum.BRANCH_RELEASE));
        propProcess.setBranchSourceFeat(REFS_HEADS + assignValue(argsList, ArgsEnum.BRANCH_SOURCE));
        propProcess.setGitDirectory(assignValue(argsList, ArgsEnum.GIT_DIRECTORY));
        propProcess.setFeatureType(assignValue(argsList, ArgsEnum.FEATURE_TYPE));
        propProcess.setFeatureName(assignValue(argsList, ArgsEnum.FEATURE_NAME));
        return propProcess;
    }

    private void validateRequiredArgs(List<String> args) {
        if(!ArgsEnum.getRequiredArgs().stream()
                                        .allMatch(argsItem ->
                                                Stream.of(args).flatMap(Collection::stream)
                                                      .anyMatch(argsIn -> argsIn.contains(argsItem)))){
            showDocArqsRequired();
            System.exit(1);
        }
    }

    private static void showDocArqsRequired() {
        System.out.println("Os seguintes argumentos são obrigatorios:");
        ArgsEnum.getRequiredArgs().stream().map("\t"::concat).forEach(System.out::println);
    }

    private static void showDocArgs() {
        Arrays.stream(ArgsEnum.values()).forEach(System.out::println);
    }

    private String assignValue(List<String> argsList, ArgsEnum argsEnum) {
        return argsList.stream()
                .filter(s -> s.contains(argsEnum.getArgument()))
                .findAny()
                .map(arg-> arg.split("=")[1])
                .orElse(argsEnum.getDefaultValue());
    }
}
