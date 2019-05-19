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
import com.asofterspace.toolbox.web.WebAccessor;

import java.util.List;


public class ImageConverter {

	public void convertOneFileBasedOnInput() {

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

		convertImage(baseurl, id, picturePath, logoPath, width_cm, height_cm, "output.png");
	}

	public void convertImage(String baseurl, String id, String picturePath, String logoPath, Integer width_cm, Integer height_cm, String outputPath) {

		String sizeText = width_cm + " x " + height_cm + " cm";

		// load the input pictures
		System.out.println("Getting the image...");
		Image canvas = ImageFile.readImageFromFile(WebAccessor.getLocalOrWebFile(picturePath));
		Image logo = ImageFile.readImageFromFile(new File(logoPath));

		// increase image size if necessary
		// 120 cm wants to be 3720 px
		// 100 cm wants to be 3130 px
		//  90 cm wants to be 2834 px
		//  80 cm wants to be 2539 px
		//  70 cm wants to be 2244 px
		//  60 cm wants to be 1949 px
		//  50 cm wants to be 1653 px
		// y = m x + n
		// 1653 = 50 m + n
		// 3720 = 120 m + n
		// 1653 - 3720 = (50 - 120) m
		// -2067 = -70 m
		// 2067 / 70 = m
		// 3720 = 120 (2067 / 70) + n
		// 3720 - (12 / 7) 2067 = n
		// m ~~  29.5285714
		// n ~~ 176.5714286
		// check:
		// y = 29.5285714 * 120 + 176.5714286 = 3719.9999966 CHECK
		// y = 29.5285714 * 100 + 176.5714286 = 3129.4285686 CHECK - if we are rounding up...
		// y = 29.5285714 *  90 + 176.5714286 = 2834.1428546 CHECK - if we are rounding down xD
		// y = 29.5285714 *  80 + 176.5714286 = 2538.8571406 CHECK
		// y = 29.5285714 *  70 + 176.5714286 = 2243.5714266 CHECK
		// y = 29.5285714 *  60 + 176.5714286 = 1948.2857126 CHECK - we again want to round up here
		// y = 29.5285714 *  50 + 176.5714286 = 1652.9999986 CHECK
		// soooo to get the rounding right, we want to add 0.3 to n, and then do mathematically sound rounding
		// after that, we will expand by 89 px on each side!
		System.out.println("Resizing the image...");
		/*
		while (canvas.getWidth() < width_cm * 20) {
			System.out.println("canvas.getWidth(): " + canvas.getWidth() + ", width_cm * 20: " + (width_cm * 20));
			canvas.resampleBy(2.0, 2.0);
		}
		*/
		// resample, keeping the aspect ratio
		int newWidth = (int) Math.round((29.5285714 * width_cm) + 176.8714286);
		int newHeight = (int) Math.round((29.5285714 * height_cm) + 176.8714286);
		canvas.resampleTo(newWidth, newHeight, true);

		// generate QR code
		System.out.println("Adding the QR codes and lines...");
		Image qrCode = QrCodeFactory.createWhitespacedImageFromString(baseurl + id);

		// calculate actual width etc.
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		/*
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
		*/
		int eH = 88;
		int eV = 88;
		int eHp = 89;
		int eVp = 89;
		int dH = 58;
		int dV = 58;
		int dHp = 59;
		int dVp = 59;

		// expand the base image
		ColorRGB white = new ColorRGB(255, 255, 255);
		canvas.expandBy(eVp, eHp, eVp, eHp, white);

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
		qrCode.expandTopBy(12, white);
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
		System.out.println("Writing the output file...");
		if (outputPath.endsWith(".ppm")) {
			PpmFile outputPpmFile = new PpmFile(outputPath);
			outputPpmFile.assign(canvas);
			outputPpmFile.save();
		}
		if (outputPath.endsWith(".png")) {
			DefaultImageFile outputPngFile = new DefaultImageFile(outputPath);
			outputPngFile.assign(canvas);
			outputPngFile.save();
		}

		System.out.println("The file " + outputPath + " has been written.");
	}

}
