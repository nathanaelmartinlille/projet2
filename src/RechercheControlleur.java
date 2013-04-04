import javax.swing.JTextField;


public class RechercheControlleur {

	RechercheVue vue;
	RechercheModele modele;
	
	public RechercheControlleur() {
		this.vue = new RechercheVue(this);
		this.modele = new RechercheModele();
		modele.addObserver(vue);
	}
	
	public void demandeDeMiseAJour(){
		modele.majModele(null);
	}

	public void demandeRecherche(JTextField aChercher)
	{
		modele.chercher(aChercher);
	}
	
	public String[][] avoirResultatsRecherche()
	{
		return modele.resultatsRecherche;
	}
}
