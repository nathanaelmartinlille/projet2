package recherche;
import javax.swing.JTextField;


public class RechercheControlleur {

	private RechercheVue vue;
	RechercheModele modele;
	
	/**
	 * Constructeur par defaut
	 * Par defaut on fait une recherche fictive des morceaux.
	 */
	public RechercheControlleur() {
		
		this.modele = new RechercheModele(false);
		this.setVue(new RechercheVue(this));
		modele.addObserver(getVue());
	}
	
	public void demandeRecherche(JTextField aChercher)
	{
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
