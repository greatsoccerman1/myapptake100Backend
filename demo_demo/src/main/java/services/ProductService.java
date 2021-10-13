package services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import models.productModel;

@Service
public class ProductService {
	
	public List<productModel> getAllProducts(){
		
		
		List<productModel> listOfProducts = new ArrayList<>();
		listOfProducts.add(new productModel("123", "Android"));
		listOfProducts.add(new productModel("451", "Iphone"));
		return listOfProducts;
	}
	
	
	public productModel getProductById(String id) {
		Predicate<productModel> byId = p-> p.getId().equals(id);
		return filterProducts(byId);
	}
	
	private productModel filterProducts(Predicate<productModel> strategy) {
		return getAllProducts().stream().filter(strategy).findFirst().orElse(null);
	}
	
}