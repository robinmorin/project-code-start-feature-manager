package org.tools.dev.agile.projectfeaturestart.helper;

import lombok.Getter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.tools.dev.agile.projectfeaturestart.utils.GlobalConstants;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class RepositoryGitHelper {

    private Repository repo;
    private Git gitManager;

    @Getter
    private String directory;

    private Queue<String> errors = new LinkedList<>();

    public RepositoryGitHelper(String directory) {
        try {
            repo = new FileRepositoryBuilder()
                    .setGitDir(new File(directory.concat("\\.git")))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.directory = directory;
        gitManager = new Git(repo);
    }

    private RepositoryGitHelper()  throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public String createFeatureStart(String branchSource, String featureName, String featureType, CredentialsProvider credentialsProvider){
        try {
            var newBranchName = featureType.concat("/").concat(featureName);
            gitManager.checkout().setName(branchSource).call();
            gitManager.pull().setCredentialsProvider(credentialsProvider).call();
            gitManager.branchCreate().setName(newBranchName).call();
            gitManager.checkout().setName(newBranchName).call();
            return "OK";
        } catch (GitAPIException e) {
            addError(GlobalConstants.CRLF + "\tMessage: ".concat(e.getMessage()));
            addError(GlobalConstants.CRLF + "\tCause  : ".concat(e.getCause().getMessage()));
            return "NOK";
        }

    }

    public boolean checkExistBranch(String branchName) throws GitAPIException {
        return gitManager.branchList().call().stream()
                .anyMatch(ref -> ref.getName().equalsIgnoreCase(branchName));
    }

    private void addError(String error){
        errors.add(error);
    }

    public Queue<String> getLastErrors(){
        return errors;
    }

}
