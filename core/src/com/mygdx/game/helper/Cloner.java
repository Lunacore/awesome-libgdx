package com.mygdx.game.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

public class Cloner {

	public static void main(String[] args) {
		
		File defaultFolder = new File(System.getProperty("user.dir").substring(0, System.getProperty("user.dir").length() - 4));
		File currentDirectory = defaultFolder.getParentFile();
				
		String projectName = JOptionPane.showInputDialog(null, "Digite o nome do projeto");
		if(projectName == null) System.exit(0);
		
		JFileChooser tgFolder = new JFileChooser();
		tgFolder.setDialogTitle("Select output Directory");
		tgFolder.setCurrentDirectory(currentDirectory);
		tgFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = tgFolder.showOpenDialog(null);
				
		if(result != JFileChooser.APPROVE_OPTION) return;
		
		File targetFolder = new File(tgFolder.getSelectedFile().getAbsolutePath() + "/" + projectName);
		
		String defaultProjectName = "awesome-libgdx";
		
		try {
			System.out.println("Cloning...");
			FileUtils.copyDirectory(defaultFolder, targetFolder);
			//Troca o nome do projeto
			System.out.println("Changing project name");
			File projectConfig = new File(targetFolder.getAbsolutePath() + "/.project");
			replaceInFile(defaultProjectName, projectName, projectConfig);
			//Troca o nome no Build.grade
			System.out.println("Replacing build.gradle project name");
			projectConfig = new File(targetFolder.getAbsolutePath() + "/build.gradle");
			replaceInFile(defaultProjectName, getGroupName(projectName), projectConfig);
			//troca o nome do projeto-core
			System.out.println("Updating -core name");
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/.project");
			replaceInFile(defaultProjectName + "-core", getGroupName(projectName) + "-core", projectConfig);
			//troca o nome do projeto-desktop
			System.out.println("Updating -desktop name");
			projectConfig = new File(targetFolder.getAbsolutePath() + "/desktop/.project");
			replaceInFile(defaultProjectName + "-desktop", getGroupName(projectName) + "-desktop", projectConfig);
			//troca o nome do Launcher
			System.out.println("Changing launcher name");
			projectConfig = new File(targetFolder.getAbsolutePath() + "/desktop/src/com/mygdx/game/desktop/DesktopLauncher.java");
			replaceInFile("DesktopLauncher", removeAllSpaces(projectName), projectConfig);
			projectConfig.renameTo(
					new File(targetFolder.getAbsolutePath() + "/desktop/src/com/mygdx/game/desktop/" + removeAllSpaces(projectName) + ".java"));
			//troca a referencia ao commons.io
			System.out.println("Updating reference to commons.io");
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/.classpath");
			replaceInFile("src/com/mygdx/game/helper/commons-io-2.6.jar", "/awesome-libgdx-core/src/com/mygdx/game/helper/commons-io-2.6.jar", projectConfig);
			//coloca a referencia a biblioteca awesome-libgdx
			System.out.println("Referencing libraries to awesome-libgdx");
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/.classpath");
			replaceInFile("</classpath>", "\t<classpathentry kind=\"src\" path=\"/awesome-libgdx-core\"/>\r\n" + 
					"</classpath>", projectConfig);
			projectConfig = new File(targetFolder.getAbsolutePath() + "/desktop/.classpath");
			replaceInFile("</classpath>", "\t<classpathentry kind=\"src\" path=\"/awesome-libgdx-core\"/>\r\n" + 
					"</classpath>", projectConfig);
			//Apaga todos os arquivos da biblioteca
			System.out.println("Deleting old library clones");
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/src/com/mygdx/game/helper");
			deleteFolder(projectConfig);
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/src/com/mygdx/game/objects");
			deleteFolder(projectConfig);
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/src/com/mygdx/game/states");
			deleteFolder(projectConfig);
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/src/com/mygdx/game/test");
			deleteFolder(projectConfig);
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/src/com/mygdx/game/structs");
			deleteFolder(projectConfig);
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/src/com/mygdx/game/utils");
			deleteFolder(projectConfig);
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/src/com/mygdx/game/AwesomeLibGDX.java");
			deleteFolder(projectConfig);
			
			//Apaga o arquivo do git
			System.out.println("Deleting old git configuration");
			deleteFolder(new File(targetFolder.getAbsolutePath() + "/.git"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished");
	}
	
	static void deleteFolder(File folder) {
		if(folder.isDirectory()) {
			for(File f : folder.listFiles()) {
				deleteFolder(f);
			}
			folder.delete();
		}
		else {
			folder.delete();
		}
	}
	
	public static void replaceInFile(String original, String target, File file) throws IOException {

		List<String> lines = new ArrayList<String>();			
		FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        
        String line;
        
        while((line = br.readLine()) != null) {
        	lines.add(line.replaceAll(original, target));
        }
        fr.close();
        br.close();
        FileWriter fw = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fw);
        for(String s : lines) {
             out.write(s);
        	out.newLine();
        }
        out.flush();
        out.close();
	}
	
	public static String getGroupName(String name) {
		return name.toLowerCase().trim().replace(" ", "-");
	}
	
	public static String removeAllSpaces(String string) {
		return string.replaceAll(" ", "");
	}
	


}
