/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.qrImageWriter;

import com.asofterspace.toolbox.barcodes.QrCode;
import com.asofterspace.toolbox.barcodes.QrCodeFactory;
import com.asofterspace.toolbox.io.BinaryFile;
import com.asofterspace.toolbox.io.DefaultImageFile;
import com.asofterspace.toolbox.io.Directory;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.io.ImageFile;
import com.asofterspace.toolbox.io.PpmFile;
import com.asofterspace.toolbox.io.SimpleFile;
import com.asofterspace.toolbox.utils.ColorRGB;
import com.asofterspace.toolbox.utils.Image;
import com.asofterspace.toolbox.Utils;

import java.util.List;


public class Main {

	public final static String PROGRAM_TITLE = "QR Image Writer";
	public final static String VERSION_NUMBER = "0.0.0.1(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "4. May 2019";

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

		// load input
		Image canvas;
		DefaultImageFile inputJpg = new DefaultImageFile("input.jpg");
		if (inputJpg.exists()) {
			canvas = inputJpg.getImage();
		} else {
			PpmFile inputPpm = new PpmFile("input.ppm");
			if (!inputPpm.exists()) {
				System.out.println("No input found!");
				System.out.println("Please put an image into the folder of the QR Image Writer and rename it to input.jpg or input.ppm.");
				return;
			}
			canvas = inputPpm.getImage();
		}

		// generate QR code
		Image qrCode = QrCodeFactory.createWhitespacedImageFromString("this is the link");

		// draw the QR code onto the canvas
		qrCode.resample(2.0, 2.0);
		ColorRGB white = new ColorRGB(255, 255, 255);
		canvas.expand(100, 100, 100, 100, white);
		canvas.draw(qrCode, (canvas.getWidth() - qrCode.getWidth()) / 2, 0);
		canvas.draw(qrCode, (canvas.getWidth() - qrCode.getWidth()) / 2, canvas.getHeight() - qrCode.getHeight());
		canvas.draw(qrCode, 0, (canvas.getHeight() - qrCode.getHeight()) / 2);
		canvas.draw(qrCode, canvas.getWidth() - qrCode.getWidth(), (canvas.getHeight() - qrCode.getHeight()) / 2);

		// write output
		PpmFile outputFile = new PpmFile("output.ppm");
		outputFile.assign(canvas);
		outputFile.save();

		System.out.println("The file " + outputFile.getCanonicalFilename() + " has been written.");
		System.out.println("Have a nice day! :)");
	}

}
