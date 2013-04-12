package core;
public class Musique {

	public String titre, album, artiste, genre, annee, duree, path;

	public Musique(String titre,  String album, String artiste, String genre,String annee, String duree, String path) {
		super();
		this.titre = titre;
		this.artiste = artiste;
		this.album = album;
		this.annee = annee;
		this.path = path;
		this.genre = genre;
		this.duree = duree;
	}
@Override
public String toString() {
	return titre + " de " + artiste + " sur " + album + " en " + annee;
}
	
}
