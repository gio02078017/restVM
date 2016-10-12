package co.com.une.appmovilesune.adapters;

public class ItemKeyValue2 {
	private String Key;
	private int Values;

	protected long id;

	public long getId() {
		return id;
	}

	public ItemKeyValue2(String key, int values) {
		super();
		Key = key;
		Values = values;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public int getValues() {
		return Values;
	}

	public void setValues(int values) {
		Values = values;
	}

	public void setId(long id) {
		this.id = id;
	}

}
