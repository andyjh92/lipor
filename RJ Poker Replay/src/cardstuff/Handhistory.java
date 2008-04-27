/**
 * Copyright (C) 2008  Ralf Joswig
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package cardstuff;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import rjPokerReplay.FileUtil;

import cardstuffExceptions.HandhistoryException;


/**
 * @author ralf
 *
 * Interface um die Handhistory verschiedener Pokerräume abzubilden
 */

public abstract class Handhistory {
	
	public final static int POKERSTARS = 1;
	public final static int UNKNOWN = -1;
	
	
	/**
	 * List eine Handhistorie aus einer Datei
	 * 
	 * @param file Die Datei mit der Handhistorie
	 * @return Eine ArrayList mit den einzelnen Händen
	 */
	public abstract ArrayList<Hand> importHistory(String path) throws HandhistoryException;
	
	/**
	 * Ermittelt um welchen bekannten Typ einer Handhistorie es sich handelt
	 * 
	 * @param path Pfad zur Handhistoriedatei
	 * @return Bei bekannten Handhistoriedateien der Typ
	 * @throws IOException 
	 */
	public static int getKind(String path) throws IOException {
		BufferedReader inputFile;
		int fileType = -1;
		// pr�fen ob die Datei existiert
		if (!FileUtil.checkFileExists(path))
		{
			// nein, 
			throw new FileNotFoundException();
		}
		
		try
		{
			inputFile = new BufferedReader( new FileReader(path) );
		} catch (IOException e){
			throw new IOException();
		}
		String line = "";
		while ("".equals(line)) {
			line = inputFile.readLine();
		}
		if (line.startsWith("PokerStars")) { 
			fileType = POKERSTARS;
		} else {
			fileType = UNKNOWN;
		}
		return fileType;
	}

}
