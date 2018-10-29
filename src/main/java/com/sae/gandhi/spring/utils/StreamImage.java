package com.sae.gandhi.spring.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.flow.server.InputStreamFactory;

public class StreamImage implements InputStreamFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	byte[] byteImage;
	
	public StreamImage(byte[] byteImage){
		this.byteImage = byteImage;
	}
	
	@Override
	public InputStream createInputStream() {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteImage);
		return bis;
	}

}
