package com.InventoryManagement.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.InventoryManagement.dao.BrandRepository;
import com.InventoryManagement.dao.ProductRepository;
import com.InventoryManagement.entities.Brand;
import com.InventoryManagement.entities.Product;
import com.InventoryManagement.helper.Message;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	public ProductRepository productRepository;
	
	@Autowired
	public BrandRepository brandRepository;
	
	@RequestMapping("/index")
	public String adminDashboard() {
		return "admin/dashboard";
	}
	
	@RequestMapping("/add_under_brand")
	public String addToUnderBrand(Model model) {
		List<Brand> brands = brandRepository.getBrands();
		model.addAttribute("brands", brands);
		return "admin/addProduct";
	}
	
	@RequestMapping(value = "/do_add_products",method = RequestMethod.POST)
	public String submitAddProducts(@Valid @ModelAttribute("product") Product product,
			@RequestParam(name="brand_name") String brand_name,
			Model model, BindingResult result, HttpSession session) {
		
		try {
			
			if(result.hasErrors()) {
				System.out.println("ERROR "+result.toString());
				model.addAttribute("product",product);
				return "admin/addProduct";
			}
			
			int id = brandRepository.getBrandId(brand_name);
			
			product.setBrand_id(id);
			
			this.productRepository.save(product);
			
			session.setAttribute("message", new Message("successfully added","alert-success"));
			
			return "admin/addProduct";
			
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("product",product);
			session.setAttribute("message", new Message("Something went wrong"+e.getMessage(),"alert-danger"));
			return "admin/addProduct";
		}
	}
	
	@RequestMapping("/delete_a_product")
	public String deleteAProduct(Model model) {
		List<Brand> brands = this.brandRepository.getBrands();
		model.addAttribute("brands", brands);
		return "admin/deleteProduct";
	}
	
	@RequestMapping(value = "/do_delete_product", method = RequestMethod.POST)
	public String deleteAction(@Valid @ModelAttribute("product") Product product,
			@RequestParam(name = "brand_name") String brand_name, 
			@RequestParam(name = "product_name") String product_name,
			Model model, BindingResult result, HttpSession session) 
	{
		
		try {
			if(result.hasErrors()) {
				System.out.println("ERROR "+result.toString());
				model.addAttribute("product",product);
				return "admin/deleteProduct";
			}
			List<Brand> brands = this.brandRepository.getBrands();
			model.addAttribute("brands", brands);
			int product_id = productRepository.getProductId(product_name);
			int brand_id = brandRepository.getBrandId(brand_name);
			this.productRepository.deleteProduct(product_id, brand_id);
			
			session.setAttribute("message", new Message("successfully Deleted","alert-success"));
			
			return "admin/deleteProduct";
			
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("product",product);
			session.setAttribute("message", new Message("Something went wrong"+e.getMessage(),"alert-danger"));
			return "admin/deleteProduct";
		}
	}
	
	@RequestMapping(value = "/update_inventory")
	public String updateInInventory(Model model) {
		List<Product> products = this.productRepository.getProducts();
		model.addAttribute("products", products);
		return "admin/updateInventory";
	}

	@RequestMapping(value = "/do_update_inventory", method = RequestMethod.POST)
	public String submitAddToExisting(
			@ModelAttribute("product") Product product,
			Model model, BindingResult result, HttpSession session) {
		 
		try {
			if(result.hasErrors()) {
				System.out.println("ERROR "+result.toString());
				model.addAttribute("product",product);
				return "admin/updateInventory";
			}
			
			List<Product> products = this.productRepository.getProducts();
			
			model.addAttribute("products", products);
			
			System.out.println(product.getProduct_name() + " " + product.getQuantity() + " " + product.getPrice());
			
			int product_id = (Integer) this.productRepository.getProductId(product.getProduct_name());
			
			int updatedQuantity = this.productRepository.getProductQuantity(product_id) + product.getQuantity();
			
			int updatedPrice = this.productRepository.getProductPrice(product_id) + product.getPrice();
						
			this.productRepository.updateProductQuantity(product_id, updatedQuantity, updatedPrice);
			
			session.setAttribute("message", new Message("successfully added","alert-success"));
			
		} catch(Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong"+e.getMessage(),"alert-danger"));
			return "admin/updateInventory";
		}
		
		return "admin/updateInventory";
	}
		
}