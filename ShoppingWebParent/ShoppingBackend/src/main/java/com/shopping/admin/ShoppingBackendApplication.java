package com.shopping.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Arrays;

@SpringBootApplication
@EntityScan({"com.shopping.library.entity", "com.shopping.admin.user", "com.shopping.admin.category"})
public class ShoppingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingBackendApplication.class, args);

//		Playlist studyPlaylist = new Playlist("Study");
//
//		// Make "Synth Pop" playlist and add 2 songs to it.
//		Playlist synthPopPlaylist = new Playlist("Synth Pop");
//		Song synthPopSong1 = new Song("Girl Like You", "Toro Y Moi" );
//		Song synthPopSong2 = new Song("Outside", "TOPS");
//		synthPopPlaylist.add(synthPopSong1);
//		synthPopPlaylist.add(synthPopSong2);
//
//		// Make "Experimental" playlist and add 3 songs to it,
//		// then set playback speed of the playlist to 0.5x
//		Playlist experimentalPlaylist = new Playlist("Experimental");
//		Song experimentalSong1 = new Song("About you", "XXYYXX");
//		Song experimentalSong2 = new Song("Motivation", "Clams Casino");
//		Song experimentalSong3 = new Song("Computer Vision", "Oneohtrix Point Never");
//		experimentalPlaylist.add(experimentalSong1);
//		experimentalPlaylist.add(experimentalSong2);
//		experimentalPlaylist.add(experimentalSong3);
//		float slowSpeed = 0.5f;
//		experimentalPlaylist.setPlaybackSpeed(slowSpeed);
//
//		// Add the "Synth Pop" playlist to the "Experimental" playlist
//		experimentalPlaylist.add(synthPopPlaylist);
//
//		// Add the "Experimental" playlist to the "Study" playlist
//		studyPlaylist.add(experimentalPlaylist);
//
//		// Create a new song and set its playback speed to 1.25x, play this song,
//		// get the name of glitchSong → "Textuell", then get the artist of this song → "Oval"
//		Song glitchSong = new Song("Textuell", "Oval");
//		float fasterSpeed = 1.25f;
//		glitchSong.setPlaybackSpeed(fasterSpeed);
//		glitchSong.play();
//		String name = glitchSong.getName();
//		String artist = glitchSong.getArtist();
//		System.out.println ("The song name is " + name );
//		System.out.println ("The song artist is " + artist );
//
//		// Add glitchSong to the "Study" playlist
//		studyPlaylist.add(glitchSong);
//
//		// Play "Study" playlist.
//		studyPlaylist.play();
//
//		// Get the playlist name of studyPlaylist → "Study"
//		System.out.println ("The Playlist's name is " + studyPlaylist.getName() );
	}

	public static String longestCommonPrefix(String[] strs) {
		if (strs.length == 1) return strs[0];

		int len = getSmallestStringLength(strs);
		if (len == 0) return "";

		int index = 0;

		for (int i=strs.length-1; i>=0 ; i++){
			int finalI = i;
			if (Arrays.stream(strs).allMatch(str-> strs[0].substring(0,finalI).equals(str)))
				return strs[0].substring(i);
		}

		return "";

	}

	public static int getSmallestStringLength(String[] strings) {
		String smallestString = strings[0];

		for (int i = 1; i < strings.length; i++) {
			if (strings[i].compareTo(smallestString) < 0) {
				smallestString = strings[i];
			}
		}

		return smallestString.length();
	}

	public static String checkAllSameFirstChar(String[] strings) {
		char firstChar = strings[0].charAt(0);

		int i;
		int index =   0;
		for (i = 1; i < strings.length; i++) {
			if (strings[i].charAt(index) != strings[0].charAt(index)) {
				break;
			}
			index++;
		}
		return strings[0].substring(0,index);
	}

	private boolean hasSameChar(String [] strList, char letter, int position) {
		return true;
	}

}
