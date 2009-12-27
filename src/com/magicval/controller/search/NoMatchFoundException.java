package com.magicval.controller.search;

/**
 * NoMatchFoundException is thrown when a search fails to match a search query.
 * @author Paul Gibler
 *
 */
public class NoMatchFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NoMatchFoundException(){
		super();
	}
	
	public NoMatchFoundException(String error) {
		super(error);
	}
	
	public NoMatchFoundException(Throwable throwable) {
		super(throwable);
	}
	
	public NoMatchFoundException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
