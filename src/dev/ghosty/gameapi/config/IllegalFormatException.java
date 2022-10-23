package dev.ghosty.gameapi.config;

public final class IllegalFormatException extends RuntimeException {
	
	public final String message;
	
	protected IllegalFormatException(String required, String current) {
		boolean with_a_N = false;
		for(String s : new String[] {"a","e","i","o","u","y"})
			if(required.toLowerCase().startsWith(s))
				with_a_N = true;
		if(with_a_N)
			this.message = "Cannot get an "+required+" from \""+current+"\"";
		else this.message = "Cannot get a "+required+" from \""+current+"\"";
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
