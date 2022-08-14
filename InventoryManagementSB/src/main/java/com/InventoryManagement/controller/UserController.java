package com.InventoryManagement.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.InventoryManagement.dao.BrandRepository;
import com.InventoryManagement.dao.OrderItemsRepository;
import com.InventoryManagement.dao.OrdersRepository;
import com.InventoryManagement.dao.ProductRepository;
import com.InventoryManagement.dao.UserRepository;
import com.InventoryManagement.dto.OrderItemsDTO;
import com.InventoryManagement.entities.Brand;
import com.InventoryManagement.entities.OrderItems;
import com.InventoryManagement.entities.Orders;
import com.InventoryManagement.entities.Product;
import com.InventoryManagement.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderItemsRepository orderItemsRepository;
	
	List<Product> products = new ArrayList<Product>();
	List<OrderItems> orderItems = new ArrayList<OrderItems>();
	
	@RequestMapping("/index")
	public String dashboard() {
		return "normal/user_dashboard";
	}
	
	@RequestMapping(value = "/brandList", method = RequestMethod.GET)
	public String carsPage(Model model) {
		List<Brand> brands = brandRepository.getBrands();
		model.addAttribute("brands", brands);
		List<Product> products = productRepository.getProducts();
		model.addAttribute("products", products);
		return "normal/selectBrand";
	}
	
	@RequestMapping(value = "/brandcarslist", method = RequestMethod.GET)
	public String brandCarsPage(@RequestParam(name = "brand_name") String brand_name,
			Model model) {
		
		int id = brandRepository.getBrandId(brand_name);
		List<Product> products = productRepository.getProductInfoByBrandId(id);
		model.addAttribute(brand_name);
		model.addAttribute("products", products);
		return "normal/buyCarsList";
	}
	
	@RequestMapping(value = "/showcart", method = RequestMethod.GET)
	public String goToCartPage(
			@RequestParam(name = "values") List<Boolean> booleanValues,
			@RequestParam(name = "id") List<Integer> id,
			@RequestParam(name = "quantity") List<Integer> productQuantity,
			Model model) {
		
		int j=0;
		int total = 0;
		
		System.out.println(booleanValues);
		System.out.println(id);
		System.out.println(productQuantity);
		
		for(int i=0;i<booleanValues.size();i++) {
			if(booleanValues.get(i) != null) {
				products.add(productRepository.getProductFromId(id.get(i)));
				j=i;
			}
		}
		
		for(int i=0;i<products.size();i++) {
			products.get(i).setQuantity(productQuantity.get(j));
			total += products.get(i).getPrice() * products.get(i).getQuantity();
		}
		
		System.out.println(total);
		model.addAttribute("products", products);
		model.addAttribute("total",total);		
		return "normal/cart";
	}
	
	@RequestMapping(value = "buyConfirmation", method = RequestMethod.POST)
	public String buyCars(@ModelAttribute("orders") Orders orders, Model model) 	
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Date date = new Date();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		orders.setDate(sqlDate);
		User user = new User();
		user = this.userRepository.getUserByEmail(auth.getName());
		orders.setUserId(user.getUser_id());
		this.ordersRepository.save(orders);
		int price = 0;
		int order_id = ordersRepository.getLatestOrderId();
		for(int i=0;i<products.size();i++) {
			OrderItems oi = new OrderItems();
			oi.setOrder_id(order_id);
			oi.setProduct_id(products.get(i).getProduct_id());
			oi.setQuantity(products.get(i).getQuantity());
			price = price + products.get(i).getPrice() * products.get(i).getQuantity();
			orderItems.add(i, oi);
			System.out.println("{ " + orderItems.get(i).getOrder_id() + " " + orderItems.get(i).getProduct_id() + " }");
		}
		this.ordersRepository.setPrice(price, order_id);
		this.orderItemsRepository.saveAll(orderItems);
		model.addAttribute("orderId",ordersRepository.getLatestOrderId());
		model.addAttribute("orderPrice", price);
		model.addAttribute("totalCost", price+115);
		return "normal/buyConfirmation";
	}
	
	@RequestMapping(value = "yourOrders", method = RequestMethod.GET)
	public String orders(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Orders> orders = new ArrayList<Orders>();
		orders = ordersRepository.getOrdersByUserId(userRepository.getUserIdByEmail(auth.getName()));		
		model.addAttribute("orders", orders);	
		return "normal/orders";
	}
	
	@RequestMapping(value = "orderDetails", method = RequestMethod.GET)
	public String orderDetails(@RequestParam(name = "order_id") int order_id, Model model) {
		List<OrderItems> orderitems = new ArrayList<OrderItems>();
		List<OrderItemsDTO> oid = new ArrayList<OrderItemsDTO>();
		orderitems = orderItemsRepository.getOrderItemsFromOrderID(order_id);
		System.out.println(orderitems);
		for(int i=0;i<orderitems.size();i++) {
			OrderItemsDTO orderitemsdto = new OrderItemsDTO();
			orderitemsdto.setProduct_name(productRepository.getProductName(orderitems.get(i).getProduct_id()));
			orderitemsdto.setAmount(productRepository.getProductPrice(orderitems.get(i).getPrice()));
			oid.add(orderitemsdto);
		}
		model.addAttribute("orderitems", oid);	
		return "normal/orderDetails";
	}
}