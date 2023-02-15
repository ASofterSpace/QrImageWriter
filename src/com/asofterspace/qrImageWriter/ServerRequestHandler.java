/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.qrImageWriter;

import com.asofterspace.toolbox.io.BinaryFile;
import com.asofterspace.toolbox.io.File;
import com.asofterspace.toolbox.web.WebRequestFormData;
import com.asofterspace.toolbox.web.WebRequestFormDataBlock;
import com.asofterspace.toolbox.web.WebServer;
import com.asofterspace.toolbox.web.WebServerAnswer;
import com.asofterspace.toolbox.web.WebServerAnswerInJson;
import com.asofterspace.toolbox.web.WebServerRequestHandler;

import java.io.IOException;
import java.net.Socket;


public class ServerRequestHandler extends WebServerRequestHandler {

	private ImageConverter imgConverter;


	public ServerRequestHandler(WebServer server, Socket request, ImageConverter imgConverter) {

		super(server, request, null);

		this.imgConverter = imgConverter;
	}

	@Override
	protected void handlePost(String fileLocation) throws IOException {

		WebServerAnswer answer = null;

		System.out.println("POST received for: " + fileLocation);

		if (fileLocation.endsWith("/qrimagewriter/convertImage")) {
			try {
				String filename = convertImage();
				answer = new WebServerAnswerInJson("{\"success\": true, \"file\": \"" + filename + "\"}");
			} catch (IOException e) {
				respond(500);
				return;
			}
		} else {
			respond(404);
			return;
		}

		respond(200, answer);
	}

	private String convertImage() throws IOException {

		WebRequestFormData data = receiveFormDataContent();

		Integer width_cm = 0;
		try {
			WebRequestFormDataBlock width_block = data.getByName("width");
			if (width_block != null) {
				System.out.println("DEBUG :: width: " + width_block.getContent());
				width_cm = Integer.valueOf(width_block.getContent().trim());
			} else {
				System.out.println("DEBUG :: width is null!");
			}
		} catch (NumberFormatException e) {
		}
		Integer height_cm = 0;
		try {
			WebRequestFormDataBlock height_block = data.getByName("height");
			if (height_block != null) {
				height_cm = Integer.valueOf(height_block.getContent().trim());
			}
		} catch (NumberFormatException e) {
		}
		String picture = data.getByName("picture").getContent();

		// TODO :: randomize!
		String inputFileName = "input.png";

		// TODO :: save picture locally as input picture
		BinaryFile pictureFile = new BinaryFile(inputFileName);
		pictureFile.saveContentStr(picture);

		// TODO :: randomize!
		String outputFileName = "output.png";

		System.out.println("pic: " + pictureFile + "\n" + "width: " + width_cm + "\nheight: " + height_cm + "\n");

		imgConverter.convertImage("http://www.ideanding.com/etiquetadora/?id=", "exampleid", inputFileName, "logo.png", width_cm, height_cm, outputFileName);

		return outputFileName;
	}

	@Override
	protected File getFileFromLocation(String location, String[] arguments) {

		System.out.println("GET received for: " + location);

		if (location.startsWith("/") && location.endsWith(".png")) {

			// TODO :: create answer based on inputs

			/*
			File result = new File(db.getDataDirectory(), location.substring(1));

			if (result.exists()) {
				return result;
			}
			*/
		}

		return super.getFileFromLocation(location, arguments);
	}
}
