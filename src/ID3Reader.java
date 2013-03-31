import java.io.File;
import java.io.IOException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;


public class ID3Reader {
	File sourcefile;
	MP3File mp3file;
	
	public ID3Reader(String filename) {
			sourcefile = new File(filename);
			try {
				mp3file = new MP3File(sourcefile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static void main( String[] args )
	{
		ID3Reader id3reader = new ID3Reader("ressources/backtoblack.mp3");
		id3reader.displayinformations();
	}

	private void displayinformations() {
		String title="", artist="", album="", genre="", year="";
		
		if(mp3file.hasID3v1Tag()){
			AbstractID3v1 tag = mp3file.getID3v1Tag();
			if(tag != null){
				try{ title = tag.getSongTitle(); }catch(Exception e){}
				try{ artist = tag.getLeadArtist(); }catch(Exception e){}
				try{ album = tag.getAlbumTitle(); }catch(Exception e){}
				try{ genre = tag.getSongGenre(); }catch(Exception e){}
				try{ year = tag.getYearReleased(); }catch(Exception e){}
			}
		}else if(mp3file.hasID3v2Tag()){
			AbstractID3v2 tag = mp3file.getID3v2Tag();
			if(tag != null){
				title = tag.getFrame("TT2").toString();
				artist = tag.getFrame("TP1").toString();
				album = tag.getFrame("TAL").toString();
				year = tag.getFrame("TYE").toString();
				genre = tag.getFrame("TCO").toString();
			}
		}	
		System.out.println("Titre : " + title);
		System.out.println("Artiste : " + artist);
		System.out.println("Album : " + album);
		System.out.println("Genre : " + genre);
		System.out.println("Annee : " + year);
	}		
}