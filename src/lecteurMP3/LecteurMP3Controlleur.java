package lecteurMP3;

import javazoom.jl.player.LillePlayer;
import core.Musique;


/**Controlleur qui permet de gerer les evenements liés au panel du lecteur de MP3.
 */
public class LecteurMP3Controlleur {

	private LecteurMP3Vue vue;
	LecteurMP3Modele modele;
	
	/**
	 * COnstructeur par defaut.
	 */
	public LecteurMP3Controlleur() {
		this.setVue(new LecteurMP3Vue(this));
		this.modele = new LecteurMP3Modele(this);
		modele.addObserver(getVue());
	}
	
	/**
	 * Methode qui est appellée lorsqu'il y a une mise à jour à effectuer sur la vue
	 * @throws Exception 
	 */
	public void demandeDeMiseAJour() throws Exception{
		modele.majModele(null);
	}

	/**Methode qui va permettre de mettre en pause ou lire la musique courante.
	 * @throws Exception
	 */
	public void playPauseControlleur() throws Exception {
		modele.playPause();
	}
	
	/**Methode qui permet de modifier la position actuelle de la musique.
	 * @param positionPiste la position de la piste.
	 */
	public void setPosition(int positionPiste){
		modele.setPosition(positionPiste);
	}
	
	public LillePlayer getPlayer(){
		return modele.getMediaPlayer();
	}
	
	public int getStatus(){
		return modele.getStatus();
	}
	public void setStatus(int status){
		modele.setStatus(status);
	}

	public LecteurMP3Vue getVue() {
		return vue;
	}

	public void setVue(LecteurMP3Vue vue) {
		this.vue = vue;
	}
	
	public void setVolume(float volume)
	{
		System.out.println("volume : "+volume);
		modele.setVolume(volume);
	}
	
	/**Methode qui va passer la chanson suivante de la playlist.
	 * @throws Exception
	 */
	public void chansonSuivante() throws Exception
	{
		modele.passerChansonSuivante();
	}
	
	/**Methode qui va permettre de passer à la musique precedente dans la liste de lecture.
	 * @throws Exception
	 */
	public void chansonPrecedente() throws Exception
	{
		modele.passerChansonPrecedente();
	}
	
	public void afficherStop()
	{
		vue.lectureEnCours = false;
		vue.play.setIcon(vue.imgPlay);
		vue.sliderLecture.setValue(0);
	}
	
	public void lireMusique(Musique m) throws Exception
	{
		vue.lectureEnCours = true;
		modele.majModele(m);
	}
	
}
