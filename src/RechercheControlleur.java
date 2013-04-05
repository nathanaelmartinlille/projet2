import javax.swing.JTextField;


public class RechercheControlleur {

	RechercheVue vue;
	RechercheModele modele;
	
	public RechercheControlleur() {
		
		this.modele = new RechercheModele();
		this.vue = new RechercheVue(this);
		modele.addObserver(vue);
	}
	
	public void demandeDeMiseAJour(){
		modele.majModele(null);
	}

	public void demandeRecherche(JTextField aChercher)
	{
		System.out.println("demande rech");
		modele.chercher(aChercher);
	}
	
	public String[][] avoirResultatsRecherche()
	{
		return modele.resultatsRecherche;
	}

}
