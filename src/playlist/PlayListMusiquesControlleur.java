package playlist;


import lecteurMP3.LecteurMP3Controlleur;
import core.Musique;


public class PlayListMusiquesControlleur {

	private LecteurMP3Controlleur lecteurMP3Controlleur;
	
	private PlayListMusique vue;
	PlayListMusiqueModele modele;
	
	public PlayListMusiquesControlleur(LecteurMP3Controlleur lecteurMP3Controlleur) {
		
		this.modele = new PlayListMusiqueModele();
		this.setVue(new PlayListMusique(this));
		this.lecteurMP3Controlleur = lecteurMP3Controlleur;
		modele.addObserver(getVue());
	}
	
	public void demandeDeMiseAJour(){
		modele.majModele(null);
	}

	public PlayListMusique getVue() {
		return vue;
	}

	public void setVue(PlayListMusique vue) {
		this.vue = vue;
	}
	
	public String[][] getMusiques()
	{
		return modele.getMusiques();
		
	}
	
	public void jouerMusique(int row)
	{
		// On met ˆ jour le nombre d'Žcoutes
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
