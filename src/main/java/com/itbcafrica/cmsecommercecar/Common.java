package com.itbcafrica.cmsecommercecar;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.itbcafrica.cmsecommercecar.models.data.Cart;
import com.itbcafrica.cmsecommercecar.models.data.Category;
import com.itbcafrica.cmsecommercecar.models.data.CategoryRepository;
import com.itbcafrica.cmsecommercecar.models.data.Page;
import com.itbcafrica.cmsecommercecar.models.data.PageRepository;

@ControllerAdvice
public class Common {

	@Autowired
	private PageRepository pageRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@ModelAttribute
	public void sharedData(Model model, HttpSession session, Principal principal) {

		if (principal != null) {
			model.addAttribute("principal", principal.getName());
		}

		List<Page> pages = pageRepo.findAllByOrderBySortingAsc();
		List<Category> categories = (List<Category>) categoryRepo.findAll();
		boolean cartActive = false;
		if (session.getAttribute("cart") != null) {

			HashMap<Long, Cart> cart = (HashMap<Long, Cart>) session.getAttribute("cart");
			int size = 0;
			double total = 0;
			for (Cart value : cart.values()) {
				size += value.getQuantity();
				total += value.getQuantity() * Double.parseDouble(value.getPrice());
			}
			model.addAttribute("csize", size);
			model.addAttribute("ctotal", total);
			cartActive = true;
		}
		model.addAttribute("cpages", pages);
		model.addAttribute("ccategories", categories);
		model.addAttribute("cartActive", cartActive);
	}
}
