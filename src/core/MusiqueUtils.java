package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**Cette classe permet d'analyser un repertoire pour ainsi recuperer les fichiers MP3 de ce dossier. 
 */
public class MusiqueUtils {

	public List<File> recupererMusique(String path){
		File[] listFiles;
		File repertoire = new File(path);
		List<File> fichierTrouves = new ArrayList<File>();
		
		
		listFiles = repertoire.listFiles();
		for(int i=0;i<listFiles.length;i++){ 
			if(listFiles[i].getName().endsWith(".mp3")){ 
				fichierTrouves.add(listFiles[i]);
			} 
		}
		return fichierTrouves; 
	}

	public static void main(String[] args) {
		MusiqueUtils musiqueUtils = new MusiqueUtils();
		List<File> recupererMusique = musiqueUtils.recupererMusique("./ressources/Musiques");
		for (File file : recupererMusique) {
			System.out.println("fichier trouvé : " + file.getName());
		}
		
	}
}
