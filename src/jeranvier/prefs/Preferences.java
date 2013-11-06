package jeranvier.prefs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jeranvier.io.FileLoader;
import jeranvier.io.FileWriter;

public class Preferences{
	private String path;
	private Map<String,String> preferences;

	public Preferences(String path){
		this.path = path;
		preferences= new HashMap<String, String>();
		FileLoader<Preference> fl = new FileLoader<Preference>(path, Preference.class);
		try {
			convertToMap(fl.loadFileInList());
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | IOException e) {
			System.err.println("Could not load preferences from: "+path);
			e.printStackTrace();
		}
	}

	private void convertToMap(List<Preference> preferencesList) {
		for(Preference preference : preferencesList)
			preferences.put(preference.getKey(),preference.getValue());
	}

	public String setPreference(String key, String value) {
		String currentValue = preferences.put(key, value);
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return currentValue;
	}

	private void save() throws IOException {
		FileWriter fw = new FileWriter(path);
		fw.clear();
		for(java.util.Map.Entry<String, String> preference : preferences.entrySet()){
			fw.appendln(new Preference(preference.getKey(), preference.getValue()));
		}
		fw.close();
	}
	
	public String getPreference(String key, String defaultValue){
		String preference = preferences.get(key);
		return preference != null ? preference : defaultValue;
	}
	
	public String getPreference(String key) throws NullPointerException{
		String preference = preferences.get(key);
		if(preference == null)
			throw new NullPointerException();
		return preference;
	}
	
	
}
