package dev.ghosty.gameapi.config;

public final class InvalidPathException extends RuntimeException {
	
	public final String message;
	
	protected InvalidPathException(String path, boolean defaultsException) {
		if(!defaultsException) 
			this.message = "Cannot get the Object at the path \""+path+"\" because it doesn't exist. It was changed to the default one.";
		else this.message = "Cannot get the Object at the path \""+path+"\" because it doesn't exist and it isn't in the default config.";
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
