package com.itbcafrica.cmsecommercecar.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itbcafrica.cmsecommercecar.models.data.Category;
import com.itbcafrica.cmsecommercecar.models.data.CategoryRepository;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesController {
	@Autowired
	private CategoryRepository categoryRepo;

	@GetMapping
	public String index(Model model) {
		List<Category> categories = (List<Category>) categoryRepo.findAll();
		model.addAttribute("categories", categories);
		return "admin/categories/index";
	}

	@GetMapping("/add")
	public String add(Category category) {
		return "admin/categories/add";
	}

//
//	@ModelAttribute("category")
//	public Category getCategory() {
//		return new Category();
//	}
//
//	@GetMapping("/add")
//	public String add() {
//		return "admin/categories/add";
//	}

	@PostMapping("/add")
	public String add(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "admin/categories/add";
		}

		redirectAttributes.addFlashAttribute("message", "Category added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		String slug = category.getName().toLowerCase().replace(" ", "-");
		Category CategoryExists = categoryRepo.findByName(category.getName());
		if (CategoryExists != null) {
			redirectAttributes.addFlashAttribute("message", "Category exists ,choose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

		} else {
			category.setSlug(slug);
			category.setSorting(100);
			categoryRepo.save(category);

		}
		return "redirect:/admin/categories/add";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		Category category = categoryRepo.findById(id).orElse(new Category());
		model.addAttribute("category", category);
		return "admin/categories/edit";
	}

	@PostMapping("/edit")
	public String edit(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes,
			Model model) {
		Category currentPage = categoryRepo.findById(category.getId()).orElse(new Category());

		if (bindingResult.hasErrors()) {
			model.addAttribute("categoryName", currentPage.getName());
			return "admin/categories/edit";
		}

		redirectAttributes.addFlashAttribute("message", "Category edited");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		String slug = category.getName().toLowerCase().replace(" ", "-");
		Category slugExists = categoryRepo.findBySlugAndIdNot(slug, category.getId());
		if (slugExists != null) {
			redirectAttributes.addFlashAttribute("message", "Slug exists ,choose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("category", category);
		} else {
			category.setSlug(slug);

			categoryRepo.save(category);

		}
		return "redirect:/admin/categories/edit/" + category.getId();
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		categoryRepo.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "Category deleted");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/admin/categories";
	}

	@PostMapping("/reorder")
	public @ResponseBody String reorder(@RequestParam("id[]") Long[] id) {

		int count = 1;

		for (Long i : id) {
			Category category = categoryRepo.findById(i).orElse(new Category());
			category.setSorting(count);
			categoryRepo.save(category);
			count++;
		}

		return "ok";
	}
}
