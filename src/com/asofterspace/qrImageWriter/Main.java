/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.qrImageWriter;

import com.asofterspace.toolbox.images.Image;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.Utils;


public class Main {

	public final static String PROGRAM_TITLE = "QR Image Writer";
	public final static String VERSION_NUMBER = "0.0.0.5(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "4. May 2019 - 23. May 2019";

	public static void main(String[] args) {

		// let the Utils know in what program it is being used
		Utils.setProgramTitle(PROGRAM_TITLE);
		Utils.setVersionNumber(VERSION_NUMBER);
		Utils.setVersionDate(VERSION_DATE);

		if (args.length > 0) {
			if (args[0].equals("version_for_zip")) {
				System.out.println("version " + Utils.getVersionNumber());
				return;
			}
		}

		ImageConverter imgConverter = new ImageConverter();

		/*
		System.out.println("Starting the server...");

		Server server = new Server(imgConverter);

		server.serve();

		System.out.println("The " + Utils.getFullProgramIdentifierWithDate() + " has been stopped.");
		*/

		imgConverter.convertOneFileBasedOnInput();

		System.out.println("Have a nice day! :)");
	}
}
