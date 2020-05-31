/*
 * Created on 01.09.2005
 * 	 @author Ralf Joswig, 01.09.2005
 *   @version 1.0
 *
 * Sammlung von Methoden die immer mal wieder gebraucht werden koennen
 * 
 * Copyright (C) 2005  Ralf Joswig 
 * Dieses Programm ist freie Software. Sie k�nnen es unter den Bedingungen der GNU General Public License, wie von der Free Software Foundation ver�ffentlicht, weitergeben und/oder modifizieren, entweder gem�� Version 2 der Lizenz oder (nach Ihrer Option) jeder sp�teren Version. 
 * Die Ver�ffentlichung dieses Programms erfolgt in der Hoffnung, da� es Ihnen von Nutzen sein wird, aber OHNE IRGENDEINE GARANTIE, sogar ohne die implizite Garantie der MARKTREIFE oder der VERWENDBARKEIT F�R EINEN BESTIMMTEN ZWECK. Details finden Sie in der GNU General Public License. 
 * Sie sollten ein Exemplar der GNU General Public License zusammen mit diesem Programm erhalten haben. Falls nicht, schreiben Sie an die Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package net.sourceforge.rjPokerReplay.util;
 
import java.io.File;
import java.util.StringTokenizer;

public class FileUtil {

	/**
	 * * Gibt die Dateierweiterung zurueck. Uebergeben wird der Dateiname und
	 * das Trennzeichen. Zurueckgegeben wird der Teil des Dateinamen der sich
	 * rechts neben dem letzen Trennzeichen befindet. Wird kein Trennzeichen
	 * gefunden, fehlt der Dateiname oder das Trennzeichen, wird NULL
	 * zurueckgegeben.
	 * 
	 * @param String
	 *            Dateiname, Strind Tennzeichen
	 * @return String Dateierweiterung
	 */
	public static String getSuffix(String datei, String trenner) {
		String suffix = null;

		// Dateiname und Trennzeichen sind Muss
		if ((datei != null) && (trenner != null)) {
			StringTokenizer st = new StringTokenizer(datei, trenner);
			// Dateinamen Trennzeichen fuer Trennzeichen durchgehen
			while (st.hasMoreTokens()) {
				suffix = st.nextToken();
			}
		}
		return suffix;
	} // getSuffix
	
	/**
	 * Gibt die Dateierweiterung zurueck. Uebergeben wird der Dateiname. Als
	 * Trennzeichen wird feste ein Punkt verwendet. Zurueckgegeben wird der
	 * Teil des Dateinamen der sich rechts neben dem letzen Trennzeichen
	 * befindet. Wird kein Trennzeichen gefunden, fehlt der Dateiname oder
	 * das Trennzeichen, wird NULL zurueckgegeben.
	 * 
	 * @param String
	 *            Dateiname
	 * @return String Dateierweiterung
	 */
	public static String getSuffix(String datei) {
		return getSuffix(datei, "."); //$NON-NLS-1$
	} // getSuffix

	/**
	 * * Gibt den Dateinamen ohne Erweiterung zurueck. Uebergeben wird der
	 * Dateiname und das Trennzeichen. Zurueckgegeben wird der Teil des
	 * Dateinamen der sich links neben dem letzen Trennzeichen befindet. Wird
	 * kein Trennzeichen gefunden wird er kpl. Dateiname, fehlt der Dateiname
	 * oder das Trennzeichen, wird NULL zurueckgegeben.
	 * 
	 * @param String
	 *            Dateiname, Strind Tennzeichen
	 * @return String Dateiname ohne Erweiterung
	 */

	public static String getBasename(String datei, String trenner) {
		String basename = null;

		if ((datei != null) && (trenner != null)) {
			// Erweiterung ermitteln
			String suffix = getSuffix(datei, trenner);
			// Erweiterung abschneiden
			int suffixLength = suffix.length() + trenner.length();
			basename = datei.substring(0, datei.length() - suffixLength);
		}
		return basename;
	} // getBasename

	/**
	 * * Prueft ob eine Datei vorhanden ist. Uebergeben werden muss der kpl.
	 * Dateiname mit Pfad.
	 * 
	 * @param String
	 *            Dateiname
	 * @return true wenn Datei vorhanden, false wenn nicht
	 */
	public static boolean checkFileExists(String datei) {
		if (datei != null) {
			File f_datei = new File(datei);
			// Pruefen ob Datei noch vorhanden
			try {
				if (f_datei.exists()) {
					return true;
				} else {
					return false;
				}
			} catch (NullPointerException e) {
				// Der Dateiname ist leer !?!
				return false;
			} // try
		} else {
			return false;
		} // if(datei != null)
	} // checkFileExists(String)

	/**
	 * Prueft ob eine Datei vorhanden ist. Uebergeben werden muss der kpl.
	 * Dateiname mit Pfad.
	 * 
	 * @param File
	 *            Filehandel auf Datei
	 * @return true wenn Datei vorhanden, false wenn nicht
	 */
	public static boolean checkFileExists(File datei) {
		if (datei != null) {
			// Pruefen ob Datei noch vorhanden
			try {
				if (datei.exists()) {
					return true;
				} else {
					return false;
				}
			} catch (NullPointerException e) {
				// Der Dateihandle ist leer !?!
				return false;
			} // try
		} else {
			return false;
		} // if(datei != null)
	} // checkFileExists(File)
}
