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
	public final static String VERSION_NUMBER = "0.0.0.2(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
	public final static String VERSION_DATE = "4. May 2019 - 7. May 2019";

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
		Integer width_cm = inputJson.getInteger("width");
		Integer height_cm = inputJson.getInteger("height");
		String sizeText = width_cm + " x " + height_cm + " cm";

		// load the input pictures
		Image canvas = ImageFile.readImageFromFile(new File(picturePath));
		Image logo = ImageFile.readImageFromFile(new File(logoPath));

		// generate QR code
		Image qrCode = QrCodeFactory.createWhitespacedImageFromString(baseurl + id);

		// calculate actual width etc.
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		// take width and height in cm and expand by 3 cm on each side
		int eH = (width * 3) / width_cm; // expand horz
		int eV = (height * 3) / height_cm; // expand vert
		int eHp = eH + 1;
		int eVp = eV + 1;
		// take width and height in cm and get where the gray line goes by adding 2 cm everywhere
		int dH = (width * 2) / width_cm; // dist horz
		int dV = (height * 2) / height_cm; // dist vert
		int dHp = dH + 1;
		int dVp = dV + 1;

		// expand the base image
		ColorRGB white = new ColorRGB(255, 255, 255);
		canvas.expand(eV, eH, eV, eH, white);

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
		canvas.drawLine(  dHp,   dV , w-dHp,   dV , gray);
		canvas.drawLine(  dHp, h-dV , w-dHp, h-dV , gray);
		canvas.drawLine(  dH ,   dVp,   dH , h-dVp, gray);
		canvas.drawLine(w-dH ,   dVp, w-dH , h-dVp, gray);

		canvas.drawLine(  dH ,   dVp, w-dH ,   dVp, gray);
		canvas.drawLine(  dH , h-dVp, w-dH , h-dVp, gray);
		canvas.drawLine(  dHp,   dV ,   dHp, h-dV , gray);
		canvas.drawLine(w-dHp,   dV , w-dHp, h-dV , gray);

		// perpendicular cut marks
		canvas.drawLine(   0,   eV, dH,   eV, gray);
		canvas.drawLine(   0, h-eV, dH, h-eV, gray);
		canvas.drawLine(w-dH,   eV,  w,   eV, gray);
		canvas.drawLine(w-dH, h-eV,  w, h-eV, gray);

		canvas.drawLine(  eH,	 0,   eH, dV, gray);
		canvas.drawLine(w-eH,	 0, w-eH, dV, gray);
		canvas.drawLine(  eH, h-dV,   eH,  h, gray);
		canvas.drawLine(w-eH, h-dV, w-eH,  h, gray);

		canvas.drawLine(   0,   eVp, dH,   eVp, gray);
		canvas.drawLine(   0, h-eVp, dH, h-eVp, gray);
		canvas.drawLine(w-dH,   eVp,  w,   eVp, gray);
		canvas.drawLine(w-dH, h-eVp,  w, h-eVp, gray);

		canvas.drawLine(  eHp,	  0,   eHp, dV, gray);
		canvas.drawLine(w-eHp,	  0, w-eHp, dV, gray);
		canvas.drawLine(  eHp, h-dV,   eHp,  h, gray);
		canvas.drawLine(w-eHp, h-dV, w-eHp,  h, gray);

		// draw logo onto the canvas
		logo.resampleToHeight(dVp);
		canvas.draw(logo, 1, 1);

		// draw picture size onto the canvas
		canvas.drawText(sizeText, null, canvas.getWidth() - (dH * 4), canvas.getHeight() - 10, null, "Arial", dV - 8, true);

		// draw the QR code onto the canvas
		qrCode.resizeBy(2, 2);
		qrCode.rotateRight();
		qrCode.rotateRight();
		qrCode.expandTop(12, white);
		qrCode.drawText(id, 3, null, null, 12, "Arial", 10, true);
		qrCode.rotateLeft();
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
