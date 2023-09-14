package org.tools.dev.agile.projectfeaturestart.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManagerHelper {

    private FileManagerHelper() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static Queue<File> getDirectoryProjectFolders(String directoryProject, boolean onlyFoldersRepo) {
        try (Stream<Path> listFiles = Files.list(Paths.get(directoryProject))) {
            Predicate<Path> evalPathDirectory =
                    !onlyFoldersRepo ? Files::isDirectory :
                                       path -> {
                                           try {
                                               return Files.isDirectory(path) && Files.list(Paths.get(path.toUri()))
                                                       .anyMatch(pathRepo -> pathRepo.endsWith(".git"));
                                           } catch (IOException e) {
                                               throw new RuntimeException(e);
                                           }
                                       };

            return listFiles
                    .filter(evalPathDirectory)
                    .map(Path::toFile).distinct()
                    .collect(Collectors.toCollection(LinkedList::new));

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
