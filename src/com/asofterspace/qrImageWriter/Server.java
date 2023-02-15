/**
 * Unlicensed code created by A Softer Space, 2019
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.qrImageWriter;

import com.asofterspace.toolbox.web.WebServer;
import com.asofterspace.toolbox.web.WebServerRequestHandler;

import java.net.Socket;


public class Server extends WebServer {

	private ImageConverter imgConverter;


	public Server(ImageConverter imgConverter) {

		super(null);

		this.imgConverter = imgConverter;
	}

	protected WebServerRequestHandler getHandler(Socket request) {
		return new ServerRequestHandler(this, request, imgConverter);
	}

}
