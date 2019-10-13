package com.itbcafrica.cmsecommercecar.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itbcafrica.cmsecommercecar.models.data.Page;
import com.itbcafrica.cmsecommercecar.models.data.PageRepository;

@Controller
@RequestMapping("/admin/pages")
public class AdminPagesController {

	@Autowired
	private PageRepository pageRepo;

	/*
	 * public AdminPagesController(PageRepository repository) { this.pageRepo =
	 * repository; }
	 */

	@GetMapping
	public String index(Model model) {

		List<Page> pages = (List<Page>) pageRepo.findAllByOrderBySortingAsc();

		model.addAttribute("pages", pages);

		return "admin/pages/index";
	}

	@GetMapping("/add")
	public String add(@ModelAttribute Page page) {
		// model.addAttribute("page",new Page())
		return "admin/pages/add";
	}

	@PostMapping("/add")
	public String add(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "admin/pages/add";
		}

		redirectAttributes.addFlashAttribute("message", "Page added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
				: page.getSlug().toLowerCase().replace(" ", "-");
		Page slugExists = pageRepo.findBySlug(slug);
		if (slugExists != null) {
			redirectAttributes.addFlashAttribute("message", "Slug exists ,choose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("page", page);
		} else {
			page.setSlug(slug);
			page.setSorting(100);
			Page pageToSave = pageRepo.save(page);
			System.out.println(pageToSave);

		}
		return "redirect:/admin/pages/add";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		Page page = pageRepo.findById(id).orElse(new Page());
		model.addAttribute("page", page);
		return "admin/pages/edit";
	}

	@PostMapping("/edit")
	public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes,
			Model model) {
		Page currentPage = pageRepo.findById(page.getId()).orElse(new Page());

		if (bindingResult.hasErrors()) {
			model.addAttribute("pageTitle", currentPage.getTitle());
			return "admin/pages/edit";
		}

		redirectAttributes.addFlashAttribute("message", "Page edited");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
				: page.getSlug().toLowerCase().replace(" ", "-");
		Page slugExists = pageRepo.findBySlugAndIdNot(slug, page.getId());
		if (slugExists != null) {
			redirectAttributes.addFlashAttribute("message", "Slug exists ,choose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("page", page);
		} else {
			page.setSlug(slug);

			Page savedPage = pageRepo.save(page);
			System.out.println(savedPage);
		}
		return "redirect:/admin/pages/edit/" + page.getId();
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		pageRepo.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "Page deleted");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/admin/pages";
	}

}