package com.duzj.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;

import java.io.*;

public class FrontPackage {
    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        if(path.startsWith("/")){
            packageMac(path);
        }else {
            packageWindow(path);
        }
    }


    public static void replaceTextInFile(String filePath, String oldText, String newText) {
        // 读取文件内容并替换
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".tmp"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace(oldText, newText);
                writer.write(line + System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将临时文件替换回原始文件
        try {
            java.nio.file.Files.move(
                    java.nio.file.Paths.get(filePath + ".tmp"),
                    java.nio.file.Paths.get(filePath),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void packageMac(String path){
        System.out.println("Project path: " + path);

        String exec = "pnpm --prefix " + path + "/front" + " run build";
        System.out.println(exec);
        String s = RuntimeUtil.execForStr(exec);
        System.out.println(s);

        String indexHtmlPath = path + "/front/dist/index.html";
        String assetsPath = path + "/front/dist/assets";
        String svgPath = path + "/front/dist/navigation.svg";

        String indexHtmlPathTarget = path + "/src/main/resources/templates/back";
        String assetsPathTarget = path + "/src/main/resources/static/";
        String svgPathTarget = path + "/src/main/resources/static/";

        File indexHtmlFileTarget = new File(indexHtmlPathTarget + "/index.html");
        if (indexHtmlFileTarget.exists()) {
            FileUtil.del(indexHtmlFileTarget);
        }
        FileUtil.copy(new File(indexHtmlPath).toPath(), new File(indexHtmlPathTarget).toPath());


        File assetsFileTarget = new File(assetsPathTarget + "/assets");
        if (assetsFileTarget.exists()) {
            FileUtil.del(assetsFileTarget);
        }
        FileUtil.copy(new File(assetsPath).toPath(), new File(assetsPathTarget).toPath());

        File svgFileTarget = new File(svgPathTarget + "/navigation.svg");
        if (svgFileTarget.exists()) {
            FileUtil.del(svgFileTarget);
        }
        FileUtil.copy(new File(svgPath).toPath(), new File(svgPathTarget).toPath());

        replaceTextInFile(indexHtmlPathTarget + "/index.html", "/navigation.svg", "/back/navigation.svg");

    }

    public static void packageWindow(String path){
        System.out.println("Project path: " + path);

        String exec = "D:\\nodenpm\\node_global\\pnpm.cmd --prefix " + path + "\\front" + " run build";
        System.out.println(exec);
        String s = RuntimeUtil.execForStr(exec);
        System.out.println(s);

        String indexHtmlPath = path + "\\front\\dist\\index.html";
        String assetsPath = path + "\\front\\dist\\assets";
        String svgPath = path + "\\front\\dist\\navigation.svg";

        String indexHtmlPathTarget = path + "\\src\\main\\resources\\templates\\back";
        String assetsPathTarget = path + "\\src\\main\\resources\\static\\";
        String svgPathTarget = path + "\\src\\main\\resources\\static\\";

        File indexHtmlFileTarget = new File(indexHtmlPathTarget + "\\index.html");
        if (indexHtmlFileTarget.exists()) {
            FileUtil.del(indexHtmlFileTarget);
        }
        FileUtil.copy(new File(indexHtmlPath).toPath(), new File(indexHtmlPathTarget).toPath());


        File assetsFileTarget = new File(assetsPathTarget + "\\assets");
        if (assetsFileTarget.exists()) {
            FileUtil.del(assetsFileTarget);
        }
        FileUtil.copy(new File(assetsPath).toPath(), new File(assetsPathTarget).toPath());

        File svgFileTarget = new File(svgPathTarget + "\\navigation.svg");
        if (svgFileTarget.exists()) {
            FileUtil.del(svgFileTarget);
        }
        FileUtil.copy(new File(svgPath).toPath(), new File(svgPathTarget).toPath());

        replaceTextInFile(indexHtmlPathTarget + "\\index.html", "/navigation.svg", "/back/navigation.svg");

    }
}
