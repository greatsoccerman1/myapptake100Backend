package models;

public class productModel {
	
	private String id;
	private String name;
	
	public productModel(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

}
