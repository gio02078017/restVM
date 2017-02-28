package co.com.une.appmovilesune.adapters;

public class ItemKeyValue {
    private String Key, Values;

    protected long id;

    public long getId() {
        return id;
    }

    public ItemKeyValue(String key, String values) {
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

    public String getValues() {
        return Values;
    }

    public void setValues(String values) {
        Values = values;
    }

    public void setId(long id) {
        this.id = id;
    }

}
