

public class ListeMusiquesControlleur {

	ListeMusiquesVue vue;
	ListeMusiquesModele modele;
	
	public ListeMusiquesControlleur() {
		
		this.modele = new ListeMusiquesModele();
		this.vue = new ListeMusiquesVue(this);
		modele.addObserver(vue);
	}
	
	public void demandeDeMiseAJour(){
		modele.majModele(null);
	}
	
}
