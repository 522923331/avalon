package iplay.cool.utils.projectGenDirTree;

import java.io.File;

/**
 * @author wu.dang
 * @since 2023/12/14
 */
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenDirTree {

    public static void main(String[] args) {
        String projectPath = "/Users/yp-23040708/IdeaProjects/explorer-server/src";
        System.out.println(projectPath);
        genDirTree(projectPath, 0, false);
    }

    public static void genDirTree(String path, int level, boolean printFiles) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在");
            return;
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        // 按主次顺序排列目录
        List<File> sortedFiles = sortFiles(Arrays.asList(files));

        // 反转目录列表
        Collections.reverse(sortedFiles);

        for (int i = 0; i < sortedFiles.size() - 1; i++) {
            printFileInfo(sortedFiles.get(i), level, false, printFiles);
            if (sortedFiles.get(i).isDirectory()) {
                genDirTree(sortedFiles.get(i).getAbsolutePath(), level + 1, printFiles);
            }
        }

        // 处理最后一个文件（夹）的输出
        printFileInfo(sortedFiles.get(sortedFiles.size() - 1), level, true, printFiles);
        if (sortedFiles.get(sortedFiles.size() - 1).isDirectory()) {
            genDirTree(sortedFiles.get(sortedFiles.size() - 1).getAbsolutePath(), level + 1, printFiles);
        }
    }

    private static void printFileInfo(File file, int level, boolean isLast, boolean printFiles) {
        if (!printFiles && !file.isDirectory()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            if (i == level - 1) {
                sb.append(isLast ? " └─" : " ├─");
            } else {
                sb.append("    ");
            }
        }
        sb.append(file.getName());
        System.out.println(sb.toString());
    }

    private static List<File> sortFiles(List<File> files) {
        // 按主次顺序排列目录，主要目录在前，次要目录在后
        return files;
    }
}



