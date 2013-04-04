public class Musique {

	String titre, album, artiste, annee;

	public Musique(String titre,  String album, String artiste, String annee) {
		super();
		this.titre = titre;
		this.artiste = artiste;
		this.album = album;
		this.annee = annee;
	}
@Override
public String toString() {
	return titre + " de " + artiste + " sur " + album + " en " + annee;
}
	
}
