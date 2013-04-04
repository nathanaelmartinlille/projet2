import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;


public class ID3Reader {
	File sourcefile;
	MP3File mp3file;
	String title="", artist="", album="", genre="", year="";
	public ID3Reader(String filename) {
		sourcefile = new File(filename);
		try {
			mp3file = new MP3File(sourcefile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TagException e) {
			e.printStackTrace();
		}
		enregistrerInformations();

	}

	private void enregistrerInformations() {
		//FIXME consulter le tag en UTF16
		AbstractID3v2 tag = mp3file.getID3v2Tag();
		if(tag != null){
			title = recupererStringEncodee(tag.getSongTitle());
			artist = recupererStringEncodee(tag.getLeadArtist());
			album = recupererStringEncodee(tag.getAlbumTitle());
			year = recupererStringEncodee(tag.getYearReleased());
			genre = recupererStringEncodee(tag.getSongGenre());
		}
	}	

	public String recupererStringEncodee(String s){
		try {
			// Convert from Unicode to UTF-8
			byte[] utf8 = s.getBytes("UTF-8");

			// Convert from UTF-8 to Unicode
			return new String(utf8, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return s;
	}
}