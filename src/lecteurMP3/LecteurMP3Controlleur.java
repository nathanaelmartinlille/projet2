package lecteurMP3;

import playlist.PlayListMusiqueModele;
import core.Musique;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.LillePlayer;


public class LecteurMP3Controlleur {

	private LecteurMP3Vue vue;
	LecteurMP3Modele modele;
	
	public LecteurMP3Controlleur() {
		this.setVue(new LecteurMP3Vue(this));
		this.modele = new LecteurMP3Modele(this);
		modele.addObserver(getVue());
	}
	
	public void demandeDeMiseAJour(){
		modele.majModele(null);
	}

	public void playPauseControlleur() throws JavaLayerException {
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
		modele.setVolume(volume);
	}
	
	public void afficherStop()
	{
		vue.lectureEnCours = false;
		vue.play.setIcon(vue.imgPlay);
		vue.sliderLecture.setValue(0);
	}
	
	public void lireMusique(Musique m)
	{
		vue.lectureEnCours = true;
		modele.majModele(m);
	}
	
}
