package dev.ghosty.gameapi.util;

import java.util.HashMap;
import java.util.function.Function;

public abstract class AbstractPlaceholder<T> {
	
	public String placehold(String base, T instance) {
		if(getPlaceholders() == null) return base;
		if(instance == null) throw new NullPointerException("instance cannot be null");
		String result = base;
		for(int i = 0; i < getPlaceholders().size(); i++) {
			String toReplace = (String) getPlaceholders().keySet().toArray()[i];
			Function<T, String> func = (Function<T, String>) getPlaceholders().values().toArray()[i];
			result = result.replace(toReplace, func.apply(instance));
		}
		return result;
	}
	
	public abstract HashMap<String, Function<T, String>> getPlaceholders();
	
	public void addPlaceholder(String it, Function<T, String> function) {
		if(getPlaceholders() == null) throw new NullPointerException("getPlaceholders() cannot be null");
		getPlaceholders().put(it, function);
	}

}
