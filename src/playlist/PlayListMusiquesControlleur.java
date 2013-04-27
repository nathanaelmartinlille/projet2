package playlist;


import lecteurMP3.LecteurMP3Controlleur;
import core.Musique;


public class PlayListMusiquesControlleur {

	private LecteurMP3Controlleur lecteurMP3Controlleur;
	
	private PlayListMusiqueVue vue;
	PlayListMusiqueModele modele;
	
	public PlayListMusiquesControlleur(LecteurMP3Controlleur lecteurMP3Controlleur) {
		
		this.modele = new PlayListMusiqueModele();
		this.setVue(new PlayListMusiqueVue(this));
		this.lecteurMP3Controlleur = lecteurMP3Controlleur;
		modele.addObserver(getVue());
	}
	
	public void demandeDeMiseAJour(){
		modele.majModele(null);
	}

	public PlayListMusiqueVue getVue() {
		return vue;
	}

	public void setVue(PlayListMusiqueVue vue) {
		this.vue = vue;
	}
	
	public String[][] getMusiques()
	{
		return modele.getMusiques();
		
	}
	
	public void jouerMusique(int row) throws Exception
	{
		// On met � jour le nombre d'�coutes
		Musique musique = modele.getMusique(row+1);
		modele.addOneLecture(row+1);
		lecteurMP3Controlleur.lireMusique(musique);
	}
	
	public void supprimerMusique(int row)
	{
		modele.supprimerMusique(row);
	}
	
	public void ajouterMusique(Musique musique)
	{
		modele.ajouterMusique(musique);
	}
}
