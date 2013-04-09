

public class PlayListMusiquesControlleur {

	PlayListMusique vue;
	PlayListMusiqueModele modele;
	
	public PlayListMusiquesControlleur() {
		
		this.modele = new PlayListMusiqueModele();
		this.vue = new PlayListMusique(this);
		modele.addObserver(vue);
	}
	
	public void demandeDeMiseAJour(){
		modele.majModele(null);
	}
	
}
