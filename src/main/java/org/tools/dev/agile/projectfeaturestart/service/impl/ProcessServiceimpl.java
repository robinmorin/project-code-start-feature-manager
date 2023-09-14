package org.tools.dev.agile.projectfeaturestart.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.tools.dev.agile.projectfeaturestart.helper.FileManagerHelper;
import org.tools.dev.agile.projectfeaturestart.helper.RepositoryGitHelper;
import org.tools.dev.agile.projectfeaturestart.properties.PropertiesProcess;
import org.tools.dev.agile.projectfeaturestart.service.ProcessService;

import java.io.Console;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
@Slf4j
public class ProcessServiceimpl implements ProcessService {

    private static final String PATTERN_LIST_DIRECTORY = "\t[%2d] - %s";

    public String execute(PropertiesProcess properties) throws GitAPIException {
        //TODO: O log esta OFF, arrumar configuração pra criar arquivo de log em paralelo e não mostrar na consola, só mostrar na consola o SOUT
        log.info("{}",properties);
        log.info("Iniciando processo de gestão de branch(s) para dar inicio a implementação de uma feature de negocio");

        Console console = System.console();
        console.printf("%nGIT Credenciais [%s]%n","Connecting");
        var user = console.readLine( "User    : ");
        var passw =  new String(console.readPassword("Password: "));

        UsernamePasswordCredentialsProvider userCredentials = new UsernamePasswordCredentialsProvider(user,passw);

        Queue<File> listFolder = FileManagerHelper.getDirectoryProjectFolders(properties.getGitDirectory(),true);

        Map<Integer,File> fileMap = new HashMap<>();
        int indexFolder = 0;
        while (!listFolder.isEmpty()) fileMap.put(++indexFolder, listFolder.poll());

        if(fileMap.isEmpty())
            return "Não tem diretorio que sejam repositorios Git. Fim do processo!";

        System.out.printf("%n%nPor gentileza, informe os serviços que deseja utilzar na implementação da feature [%s] %n" +
                          "Escreva os numeros dos serviços separados por virgula, exemplo: 2,6,14%n%n", properties.getFeatureName());

        //Imprimindo diretorio pra selecionar os projetos a serem afetados
        fileMap.entrySet().stream()
                .map(entry -> String.format(PATTERN_LIST_DIRECTORY,entry.getKey(),entry.getValue().getName()))
                .forEach(System.out::println);

        System.out.printf("%n%nEscreva sua seleção: ");
        String selection =  new Scanner(System.in).next();
        if(selection.isEmpty())
            return "Não foi selecionado nenhum diretorio. Fim do processo!";

        Queue<RepositoryGitHelper> foldersToWork = Arrays.stream(selection.split(","))
                                            .map(Integer::new)
                                            .map(fileMap::get)
                                            .map(File::getPath)
                                            .map(RepositoryGitHelper::new)
                                        .collect(Collectors.toCollection(LinkedList::new));

        while (!foldersToWork.isEmpty()){
            var repo = foldersToWork.poll();
            if(repo.checkExistBranch(properties.getBranchSourceFeat())){
                String result = repo.createFeatureStart(properties.getBranchSourceFeat(), properties.getFeatureName(), properties.getFeatureType(), userCredentials);
                System.out.printf("%nCriando a feature [%s] para o projeto [%s] - Resultado: [%s]", properties.getFeatureType().concat("/").concat(properties.getFeatureName()),repo.getDirectory(), result);
                if(result.equalsIgnoreCase("NOK")){
                    var errorQueue = repo.getLastErrors();
                    while (!errorQueue.isEmpty()) {
                        var err = errorQueue.poll();
                        System.out.println(err);
                    }
                }
            }
        }

       return String.format("Foi iniciado corretamente a Feature [%s].",properties.getFeatureType().concat("/").concat(properties.getFeatureName()));
    }


}
