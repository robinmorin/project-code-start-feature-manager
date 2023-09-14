package org.tools.dev.agile.projectfeaturestart.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.tools.dev.agile.projectfeaturestart.properties.PropertiesProcess;

public interface ProcessService {

    String execute(PropertiesProcess properties) throws GitAPIException;

}
