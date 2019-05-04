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
import com.asofterspace.toolbox.io.JsonFile;
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

		// load the input JSON
		JsonFile inputJson = new JsonFile("input.json");
		if (!inputJson.exists()) {
			System.out.println("No input found!");
			System.out.println("Please put the input.json file in the folder of the QR Image Writer.");
			return;
		}
		String baseurl = inputJson.getValue("baseurl");
		String id = inputJson.getValue("id");
		String picturePath = inputJson.getValue("picture");
		String logoPath = inputJson.getValue("logo");

		// load the input pictures
		Image canvas = ImageFile.readImageFromFile(new File(picturePath));
		Image logo = ImageFile.readImageFromFile(new File(logoPath));

		// generate QR code
		Image qrCode = QrCodeFactory.createWhitespacedImageFromString(baseurl + id);

		// expand the base image
		ColorRGB white = new ColorRGB(255, 255, 255);
		canvas.expand(89, 89, 89, 89, white);

		// draw grid
		int w = canvas.getWidth() - 1;
		int h = canvas.getHeight() - 1;
		ColorRGB gray = new ColorRGB(121, 121, 121);

		// very outer perimeter
		canvas.drawLine(0, 0, w, 0, gray);
		canvas.drawLine(0, h, w, h, gray);
		canvas.drawLine(0, 0, 0, h, gray);
		canvas.drawLine(w, 0, w, h, gray);

		// inner perimeter
		canvas.drawLine(59, 58, w-59, 58, gray);
		canvas.drawLine(59, h-58, w-59, h-58, gray);
		canvas.drawLine(58, 59, 58, h-59, gray);
		canvas.drawLine(w-58, 59, w-58, h-59, gray);
		canvas.drawLine(58, 59, w-58, 59, gray);
		canvas.drawLine(58, h-59, w-58, h-59, gray);
		canvas.drawLine(59, 58, 59, h-58, gray);
		canvas.drawLine(w-59, 58, w-59, h-58, gray);

		// perpendicular cut marks
		canvas.drawLine(0, 88, 58, 88, gray);
		canvas.drawLine(0, h-88, 58, h-88, gray);
		canvas.drawLine(w-58, 88, w, 88, gray);
		canvas.drawLine(w-58, h-88, w, h-88, gray);
		canvas.drawLine(88, 0, 88, 58, gray);
		canvas.drawLine(w-88, 0, w-88, 58, gray);
		canvas.drawLine(88, h-58, 88, h, gray);
		canvas.drawLine(w-88, h-58, w-88, h, gray);
		canvas.drawLine(0, 89, 58, 89, gray);
		canvas.drawLine(0, h-89, 58, h-89, gray);
		canvas.drawLine(w-58, 89, w, 89, gray);
		canvas.drawLine(w-58, h-89, w, h-89, gray);
		canvas.drawLine(89, 0, 89, 58, gray);
		canvas.drawLine(w-89, 0, w-89, 58, gray);
		canvas.drawLine(89, h-58, 89, h, gray);
		canvas.drawLine(w-89, h-58, w-89, h, gray);

		// draw logo onto the canvas
		logo.resampleToHeight(59);
		canvas.draw(logo, 1, 1);

		// draw the QR code onto the canvas
		qrCode.resizeBy(2.0, 2.0);
		canvas.draw(qrCode, (canvas.getWidth() - qrCode.getWidth()) / 2, 4 + canvas.getHeight() - qrCode.getHeight());
		qrCode.rotateRight();
		canvas.draw(qrCode, -4, (canvas.getHeight() - qrCode.getHeight()) / 2);
		qrCode.rotateRight();
		canvas.draw(qrCode, (canvas.getWidth() - qrCode.getWidth()) / 2, -4);
		qrCode.rotateRight();
		canvas.draw(qrCode, 4 + canvas.getWidth() - qrCode.getWidth(), (canvas.getHeight() - qrCode.getHeight()) / 2);

		// write output
		PpmFile outputPpmFile = new PpmFile("output.ppm");
		outputPpmFile.assign(canvas);
		outputPpmFile.save();

		DefaultImageFile outputPngFile = new DefaultImageFile("output.png");
		outputPngFile.assign(canvas);
		outputPngFile.save();

		System.out.println("The files " + outputPpmFile.getCanonicalFilename() + " and " + outputPngFile.getCanonicalFilename() + " have been written.");
		System.out.println("Have a nice day! :)");
	}

}
