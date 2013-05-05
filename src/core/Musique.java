package core;
/**
 * La classe musique va permettre de modeliser une musique.
 */
public class Musique {

	public String titre, album, artiste, genre, annee, duree, path;

	/**
	 * @param titre le titre de la musique
	 * @param album l'album qui correspond au morceau
	 * @param artiste l'artiste de la musique
	 * @param genre le genre de la musique
	 * @param annee l'année de creation
	 * @param duree la durée du morceau
	 * @param path le chemin où est stocké la musique
	 */
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
