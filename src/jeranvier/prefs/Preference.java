package jeranvier.prefs;

public class Preference{

	private String key;
	private String value;
	
	public Preference(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public static Preference parsePreference(String string){
		String[] stringSplit = string.split(" = ");
		return new Preference(stringSplit[0].replaceAll("\\=", "="), stringSplit[1].replaceAll("\\=", "="));
	}
	
	public String toString(){
		String escapedKey = key.replaceAll("=", "\\=");
		String escapedValue = value.replaceAll("=", "\\=");
		return escapedKey+" = "+escapedValue;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
}
