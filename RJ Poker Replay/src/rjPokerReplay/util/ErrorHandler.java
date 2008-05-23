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
 *
 */
package rjPokerReplay.util;

/**
 * @author Ralf Joswig
 * 
 * Klasse zur Kontrollierten Fehlerbehandlung
 */
public class ErrorHandler {

	public static void handleError(Exception e, String message, boolean exit) {
		if (message != null) {
			System.out.println(message);
		}
		e.printStackTrace();
		if (exit) {
			System.exit(1);
		}
	}
}
