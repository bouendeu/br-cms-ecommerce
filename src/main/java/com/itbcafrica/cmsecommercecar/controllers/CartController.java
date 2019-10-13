package com.itbcafrica.cmsecommercecar.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itbcafrica.cmsecommercecar.models.data.Cart;
import com.itbcafrica.cmsecommercecar.models.data.Product;
import com.itbcafrica.cmsecommercecar.models.data.ProductRepository;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private ProductRepository productRepo;

	@RequestMapping("/add/{id}")
	public String add(@PathVariable Long id, HttpSession session, Model model,
			@RequestParam(value = "cartPage", required = false) String cartPage) {
		Product product = productRepo.findById(id).orElse(new Product());
		if (session.getAttribute("cart") == null) {

			HashMap<Long, Cart> cart = new HashMap<>();
			cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
			session.setAttribute("cart", cart);
		} else {
			HashMap<Long, Cart> cart = (HashMap<Long, Cart>) session.getAttribute("cart");
			if (cart.containsKey(id)) {
				int qty = cart.get(id).getQuantity();
				cart.put(id, new Cart(id, product.getName(), product.getPrice(), ++qty, product.getImage()));

			} else {
				cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
				session.setAttribute("cart", cart);
			}
		}
		HashMap<Long, Cart> cart = (HashMap<Long, Cart>) session.getAttribute("cart");
		int size = 0;
		double total = 0;
		for (Cart value : cart.values()) {
			size += value.getQuantity();
			total += value.getQuantity() * Double.parseDouble(value.getPrice());
		}
		model.addAttribute("size", size);
		model.addAttribute("total", total);
		if (cartPage != null) {
			return "redirect:/cart/view";
		}
		return "cart_view";
	}

	@GetMapping("/view")
	public String view(HttpSession session, Model model) {

		if (session.getAttribute("cart") == null) {
			return "redirect:/";
		}
		HashMap<Long, Cart> cart = (HashMap<Long, Cart>) session.getAttribute("cart");
		model.addAttribute("cart", cart);
		model.addAttribute("notCarViewPage", true);
		return "cart";
	}

	@RequestMapping("/sub/{id}")
	public String sub(@PathVariable Long id, HttpSession session, Model model, HttpServletRequest httpServletRequest) {

		Product product = productRepo.findById(id).orElse(new Product());
		HashMap<Long, Cart> cart = (HashMap<Long, Cart>) session.getAttribute("cart");
		int qty = cart.get(id).getQuantity();
		if (qty == 1) {
			cart.remove(id);
			if (cart.size() == 0) {
				session.removeAttribute("cart");
			}
		} else {
			cart.put(id, new Cart(id, product.getName(), product.getPrice(), --qty, product.getImage()));
		}
		String refererLink = httpServletRequest.getHeader("referer");
		return "redirect:" + refererLink;
	}

	@RequestMapping("/remove/{id}")
	public String remove(@PathVariable Long id, HttpSession session, Model model,
			HttpServletRequest httpServletRequest) {

		HashMap<Long, Cart> cart = (HashMap<Long, Cart>) session.getAttribute("cart");

		cart.remove(id);
		if (cart.size() == 0) {
			session.removeAttribute("cart");
		}

		String refererLink = httpServletRequest.getHeader("referer");
		return "redirect:" + refererLink;
	}

	@RequestMapping("/clear")
	public String clear(HttpSession session, HttpServletRequest httpServletRequest) {

		session.removeAttribute("cart");

		String refererLink = httpServletRequest.getHeader("referer");
		return "redirect:" + refererLink;
	}
}
