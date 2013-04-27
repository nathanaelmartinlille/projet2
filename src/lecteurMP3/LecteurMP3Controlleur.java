package lecteurMP3;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.LillePlayer;
import core.Musique;


/**Controlleur qui permet de gerer les evenements liés au panel du lecteur de MP3.
 * @author nath
 *
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

	public void playPauseControlleur() throws Exception {
		modele.playPause();
	}
	
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
	
	public void chansonSuivante() throws Exception
	{
		modele.passerChansonSuivante();
	}
	
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
