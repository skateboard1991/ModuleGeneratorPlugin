package com.skateboard.modulegenerator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.io.*;


public class GeneratorAction extends AnAction {


    private final String LIBS_DIR = "libs";

    private final String SRC_DIR = "src";

    private final String MAIN_DIR = "main";

    private final String JAVA_DIR = "java";

    private final String MANIFEST_DIR = "manifest";

    private final String MANIFEST_FILE = "AndroidManifest.xml";

    private final String RES_DIR = "res";

    private final String DRAWABLE_DIR = "drawable";

    private final String LAYOUT_DIR = "layout";

    private final String VALUES_DIR = "values";

    private final String APPLICATION_DIR = "applicaton";

    private final String BEAN_DIR = "bean";

    private final String PAGING_DIR = "paging";

    private final String REPOSSITORY_DIR = "repository";

    private final String SERVICE_DIR = "service";

    private final String UI_DIR = "ui";

    private final String VIEWMODEL_DIR = "viewmodel";

    private final String ACTIVITY_DIR = "activity";

    private final String FRAGMENT_DIR = "fragment";


    private final String GITGNORE_FILE = ".gitgnore";

    private final String BUILD_FILE = "build.gradle";

    private final String GRADLE_PROPERTIES_FILE = "gradle.properties";

    private final String PROGUARD_RULES_FILE = "proguard-rules.pro";

    private final String GRADLE_PROPERTIES_CONTENT = "isRunAlone=false";

    private final String MANIFEST_CONTENT = "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    package=$packageName>\n" +
            "\n" +
            "</manifest>";


    private final String GRADLE_CONTENT = "if (isRunAlone.toBoolean()) {\n" +
            "    apply plugin: 'com.android.application'\n" +
            "} else {\n" +
            "    apply plugin: 'com.android.library'\n" +
            "}\n" +
            "\n" +
            "apply plugin: 'kotlin-android'\n" +
            "\n" +
            "apply plugin: 'kotlin-android-extensions'\n" +
            "\n" +
            "apply plugin: 'kotlin-kapt'\n" +
            "\n" +
            "android {\n" +
            "    compileSdkVersion 28\n" +
            "\n" +
            "    defaultConfig {\n" +
            "        if (isRunAlone.toBoolean()) {\n" +
            "            applicationId \"$packageName\"\n" +
            "        }\n" +
            "        minSdkVersion 15\n" +
            "        targetSdkVersion 28\n" +
            "        versionCode 1\n" +
            "        versionName \'1.0\'\n" +
            "        testInstrumentationRunner \"androidx.test.runner.AndroidJUnitRunner\"\n" +
            "    }\n" +
            "\n" +
            "    buildTypes {\n" +
            "        release {\n" +
            "            minifyEnabled false\n" +
            "            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    sourceSets {\n" +
            "        main {\n" +
            "            if (isRunAlone.toBoolean()) {\n" +
            "                manifest.srcFile 'src/main/manifest/AndroidManifest.xml'\n" +
            "            } else {\n" +
            "                manifest.srcFile 'src/main/AndroidManifest.xml'\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    kapt {\n" +
            "        arguments {\n" +
            "            arg(\"AROUTER_MODULE_NAME\", project.getName())\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    compileOptions {\n" +
            "        sourceCompatibility JavaVersion.VERSION_1_8\n" +
            "        targetCompatibility JavaVersion.VERSION_1_8\n" +
            "    }\n" +
            "resourcePrefix \"$moduleName\""+
            "\n" +
            "}\n" +
            "\n" +
            "dependencies {\n" +
            "    implementation fileTree(dir: 'libs', include: ['*.jar'])\n" +
            "}\n";


    @Override
    public void actionPerformed(AnActionEvent e) {

        ModuleInfoDialog.pop(new ModuleInfoDialog.DialogCallback() {
            @Override
            public void onOkClicked(String moduleName, String packageName) {
                generateModuleFiles(moduleName, packageName, e.getProject().getBasePath());
                e.getProject().getBaseDir().refresh(true, true);

            }

            @Override
            public void onCancelClicked() {

            }
        });

    }

    private void generateModuleFiles(String moduleName, String packageName, String basePath) {
        String modulePath = createModuleDir(basePath, moduleName);
        createLibDir(modulePath);
        createSrc(modulePath, packageName);
        createBuildFiles(modulePath, packageName,moduleName);
    }


    private String createDir(String path, String fileName) {

        File file = new File(path, fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    private String createFile(String path, String name) {

        File file = new File(path, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getPath();
    }


    private String createModuleDir(String path, String name) {

        return createDir(path, name);
    }

    private void createLibDir(String modulePath) {

        createDir(modulePath, LIBS_DIR);
    }

    private void createSrc(String modulePath, String packageName) {

        String srcPath = createDir(modulePath, SRC_DIR);
        String mainPath = createDir(srcPath, MAIN_DIR);
        String javaPath = createDir(mainPath, JAVA_DIR);

        createDir(javaPath, packageName.replace(".", File.separator));
        String packagePath = createDir(javaPath, packageName.replace(".", File.separator));
        createCodePackage(packagePath);
        createManifestFile(mainPath, packageName);

        createRes(mainPath);
    }

    private void createCodePackage(String packagePath) {

        createDir(packagePath, APPLICATION_DIR);
        createDir(packagePath, BEAN_DIR);
        createDir(packagePath, PAGING_DIR);
        createDir(packagePath, SERVICE_DIR);
        createDir(packagePath, REPOSSITORY_DIR);
        createDir(packagePath, VIEWMODEL_DIR);
        String uiPath = createDir(packagePath, UI_DIR);
        createDir(uiPath, ACTIVITY_DIR);
        createDir(uiPath, FRAGMENT_DIR);
    }

    private void createManifestFile(String mainPath, String packageName) {

        String manifestPath = createDir(mainPath, MANIFEST_DIR);
        String manifestFile=createFile(manifestPath, MANIFEST_FILE);
        String outerManifestFile=createFile(mainPath,MANIFEST_FILE);
        writeContentToFile(MANIFEST_CONTENT.replace("$packageName", "\"" + packageName + "\""), outerManifestFile);
        writeContentToFile(MANIFEST_CONTENT.replace("$packageName", "\"" + packageName + "\""), manifestFile);
    }

    private void createRes(String mainPath) {
        String resPath = createDir(mainPath, RES_DIR);
        createDir(resPath, DRAWABLE_DIR);
        createDir(resPath, LAYOUT_DIR);
        createDir(resPath, VALUES_DIR);
    }


    private void createBuildFiles(String modulePath, String packageName,String moduleName) {
        createFile(modulePath, GITGNORE_FILE);
        createFile(modulePath, PROGUARD_RULES_FILE);
        createBuildFile(modulePath, packageName,moduleName);
        createGradlePropertiesFile(modulePath);
    }

    private void createBuildFile(String modulePath, String packageName,String moduleName) {
        String buildFile = createFile(modulePath, BUILD_FILE);
        writeContentToFile(GRADLE_CONTENT.replace("$packageName", packageName).replace("$moduleName",moduleName), buildFile);
    }

    private void createGradlePropertiesFile(String modulePath) {

        String propertiesFile = createFile(modulePath, GRADLE_PROPERTIES_FILE);
        writeContentToFile(GRADLE_PROPERTIES_CONTENT, propertiesFile);

    }

    private void writeContentToFile(String content, String filePath) {


        try {
            BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
            fw.write(content);
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
