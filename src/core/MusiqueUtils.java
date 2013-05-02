package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**Cette classe permet d'analyser un repertoire pour ainsi recuperer les fichiers MP3 de ce dossier. 
 */
public class MusiqueUtils {

	/**
	 * @param path
	 * @return
	 */
	public static List<File> recupererMusique(String path){
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
		List<File> recupererMusique = recupererMusique(Constantes.CHEMIN_MUSIQUE);
		for (File file : recupererMusique) {
			System.out.println("fichier trouvé : " + file.getName());
		}
		
	}
}
