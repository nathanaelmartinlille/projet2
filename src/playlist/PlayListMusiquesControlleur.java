package playlist;


public class PlayListMusiquesControlleur {

	private PlayListMusique vue;
	PlayListMusiqueModele modele;
	
	public PlayListMusiquesControlleur() {
		
		this.modele = new PlayListMusiqueModele();
		this.setVue(new PlayListMusique(this));
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
	
}
