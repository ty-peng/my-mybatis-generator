package com.typeng.generator.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

import com.google.common.base.CaseFormat;

/**
 * 重命名工具类.
 *
 * @author ty-peng
 * @date 2020-09-28 11:16
 */
public class RenameUtils {

    /**
     * UPPER_CAMEL -> LOWER_UNDERSCORE.
     *
     * @param path path
     * @throws IOException e
     */
    public static void renameSqlMapXml(Path path) throws IOException {
        FileVisitor<Path> renameVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                Objects.requireNonNull(path);
                Objects.requireNonNull(attrs);
                String parentPath = path.getParent().toString() + "/";
                String oldFileName = path.getFileName().toString();
                String newFileName = oldFileName.replaceAll("^I(?=(?!^)[A-Z])|(([Dd]+ao)*\\.xml$)", "");
                newFileName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, newFileName);
                newFileName = newFileName + "_sqlmap.xml";
                File dest = new File(parentPath + newFileName);
                File file = path.toFile();
                boolean renamed = file.renameTo(dest);
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(path, renameVisitor);
    }
}
