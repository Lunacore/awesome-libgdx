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
		//TODO: criar um clonador de projetos usando o nome que a pessoa quiser
		
		File defaultFolder = new File(System.getProperty("user.dir").substring(0, System.getProperty("user.dir").length() - 4));
		
		String projectName = JOptionPane.showInputDialog(null, "Digite o nome do projeto");
		JFileChooser tgFolder = new JFileChooser();
		tgFolder.setDialogTitle("Select output Directory");
		tgFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = tgFolder.showOpenDialog(null);
		
		if(result != JFileChooser.APPROVE_OPTION) return;
		
		File targetFolder = new File(tgFolder.getSelectedFile().getAbsolutePath() + "/" + projectName);
		
		String defaultProjectName = "awesome-libgdx";
		
		try {
			System.out.println("Working");
			FileUtils.copyDirectory(defaultFolder, targetFolder);
			//Troca o nome do projeto
			File projectConfig = new File(targetFolder.getAbsolutePath() + "/.project");
			replaceInFile(defaultProjectName, projectName, projectConfig);
			System.out.print(".");
			//Troca o nome no Build.grade
			projectConfig = new File(targetFolder.getAbsolutePath() + "/build.gradle");
			replaceInFile(defaultProjectName, getGroupName(projectName), projectConfig);
			System.out.print(".");
			//troca o nome do projeto-core
			projectConfig = new File(targetFolder.getAbsolutePath() + "/core/.project");
			replaceInFile(defaultProjectName + "-core", getGroupName(projectName) + "-core", projectConfig);
			System.out.print(".");
			//troca o nome do projeto-desktop
			projectConfig = new File(targetFolder.getAbsolutePath() + "/desktop/.project");
			replaceInFile(defaultProjectName + "-desktop", getGroupName(projectName) + "-desktop", projectConfig);
			System.out.print(".");
			//troca o nome do Launcher
			projectConfig = new File(targetFolder.getAbsolutePath() + "/desktop/src/com/mygdx/game/desktop/DesktopLauncher.java");
			replaceInFile("DesktopLauncher", removeAllSpaces(projectName), projectConfig);
			projectConfig.renameTo(
					new File(targetFolder.getAbsolutePath() + "/desktop/src/com/mygdx/game/desktop/" + removeAllSpaces(projectName) + ".java"));
			//Apaga o arquivo do git
			System.out.println();
			deleteFolder(new File(targetFolder.getAbsolutePath() + "/.git"));
			
			System.out.print(".");
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
        System.out.print(".");
        FileWriter fw = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fw);
        for(String s : lines) {
             out.write(s);
        	out.newLine();
        }
        out.flush();
        out.close();
        System.out.print(".");
	}
	
	public static String getGroupName(String name) {
		return name.toLowerCase().trim().replace(" ", "-");
	}
	
	public static String removeAllSpaces(String string) {
		return string.replaceAll(" ", "");
	}
	


}
