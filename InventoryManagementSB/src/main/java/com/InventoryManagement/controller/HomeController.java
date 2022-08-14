package com.InventoryManagement.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.InventoryManagement.dao.BrandRepository;
import com.InventoryManagement.dao.ProductRepository;
import com.InventoryManagement.dao.UserRepository;
import com.InventoryManagement.entities.Brand;
import com.InventoryManagement.entities.Product;
import com.InventoryManagement.entities.User;
import com.InventoryManagement.helper.Message;


@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private BrandRepository brandRepository;
	
	@Autowired 
	private ProductRepository productRepository;
	
	@RequestMapping("/")
	public String home(Model model) {
		
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		//model.addAttribute("title","About - Mobile Store");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		//model.addAttribute("title","Register - Mobile Store");
		//model.addAttribute("user",new User());
		return "signup";
	}
	
	@RequestMapping(value = "/carList", method = RequestMethod.GET)
	public String productList(Model model) {
		//System.out.println(brand);
		//System.out.println(brand_name);
		List<Brand> brands = brandRepository.getBrands();
		model.addAttribute("brands", brands);
		List<Product> products = productRepository.getProducts();
		model.addAttribute("products", products);
		return "carsList";
	}
	
	@RequestMapping("/brandcarslist")
	public String brandCarList(@RequestParam(name = "name") String brand_name, Model model) {
		int id = brandRepository.getBrandId(brand_name);
		List<Product> products = productRepository.getProductInfoByBrandId(id);
		model.addAttribute("products", products);
		return "brandCarsList";
	}
	
	@RequestMapping(value = "/do_register",method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, Model model, BindingResult result1 ,HttpSession session) {
		try {
			
			if(result1.hasErrors()) {
				System.out.println("ERROR "+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			user.setUser_role("ROLE_USER");
			user.setEnabled(true);
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			model.addAttribute("user",new User());
			
			session.setAttribute("message", new Message("successfully Registered","alert-success"));
			
			System.out.println("USER "+user);
			
			User result = this.userRepository.save(user);
			
			return "signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	}
}
