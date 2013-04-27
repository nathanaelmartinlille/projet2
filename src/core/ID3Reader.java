package core;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;


public class ID3Reader {
	File sourcefile;
	MP3File mp3file;
	private String title="";
	private String artist="";
	private String album="";
	String genre="";
	private String year="";
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
		AbstractID3v2 tag = mp3file.getID3v2Tag();
		if(tag != null){
			setTitle(recupererStringEncodee(tag.getSongTitle()));
			setArtist(recupererStringEncodee(tag.getLeadArtist()));
			setAlbum(recupererStringEncodee(tag.getAlbumTitle()));
			setYear(recupererStringEncodee(tag.getYearReleased()));
			genre = recupererStringEncodee(tag.getSongGenre());
		}
	}	

	public String recupererStringEncodee(String s){
		String input = s; // my UTF-16 string
		StringBuilder sb = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if (ch <= 0xFF) {
				sb.append(ch);
			}
		}

		try {
			byte[] ascii = sb.toString().getBytes("ISO-8859-1");
			s = new String(ascii);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} // aka LATIN-1
		if(s.length()> 1)
			s = s.replaceAll(s.substring(1, 2), "");
		return s;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}