package recherche;
import javax.swing.JTextField;


public class RechercheControlleur {

	private RechercheVue vue;
	RechercheModele modele;
	
	public RechercheControlleur() {
		
		this.modele = new RechercheModele();
		this.setVue(new RechercheVue(this));
		modele.addObserver(getVue());
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

	public RechercheVue getVue() {
		return vue;
	}

	public void setVue(RechercheVue vue) {
		this.vue = vue;
	}

}
