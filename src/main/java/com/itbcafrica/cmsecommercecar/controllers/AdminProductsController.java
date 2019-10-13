package com.itbcafrica.cmsecommercecar.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itbcafrica.cmsecommercecar.models.data.Category;
import com.itbcafrica.cmsecommercecar.models.data.CategoryRepository;
import com.itbcafrica.cmsecommercecar.models.data.Product;
import com.itbcafrica.cmsecommercecar.models.data.ProductRepository;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {
	@Autowired
	ProductRepository productRepo;

	@Autowired
	CategoryRepository categoryRepo;

	@GetMapping
	public String index(Model model, @RequestParam(value = "page", required = false) Integer p) {
		int perPage = 2;
		int page = (p != null) ? p : 0;
		Pageable pagable = PageRequest.of(page, perPage);
		Page<Product> products = productRepo.findAll(pagable);
		List<Category> categories = (List<Category>) categoryRepo.findAll();
		HashMap<Long, String> cats = new HashMap<>();
		for (Category cat : categories) {
			cats.put(cat.getId(), cat.getName());
		}
		model.addAttribute("products", products);
		model.addAttribute("cats", cats);
		long count = productRepo.count();
		double pageCount = Math.ceil((double) count / (double) perPage);
		model.addAttribute("pageCount", (int) pageCount);
		model.addAttribute("perpPage", perPage);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		return "admin/products/index";
	}

	@GetMapping("/add")
	public String add(Product product, Model model) {

		List<Category> categories = (List<Category>) categoryRepo.findAll();
		model.addAttribute("categories", categories);

		return "admin/products/add";
	}

	@PostMapping("/add")
	public String add(@Valid @ModelAttribute Product product, BindingResult bindingResult, MultipartFile file,
			RedirectAttributes redirectAttributes, Model model) {
		List<Category> categories = (List<Category>) categoryRepo.findAll();
		model.addAttribute("categories", categories);
		if (bindingResult.hasErrors()) {

			return "admin/products/add";
		}
		boolean fileOk = false;
		byte[] bytes = null;
		try {
			bytes = file.getBytes();
		} catch (IOException e) {

			e.printStackTrace();
		}
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/media/" + filename);
		if (filename.endsWith("jpg") || filename.endsWith("png")) {
			fileOk = true;
		}

		redirectAttributes.addFlashAttribute("message", "Product added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		String slug = product.getName().toLowerCase().replace(" ", "-");

		Product productExists = productRepo.findBySlug(slug);
		if (!fileOk) {
			redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("produts", product);
		} else if (productExists != null) {
			redirectAttributes.addFlashAttribute("message", "Product  exists ,choose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("produts", product);

		} else {
			product.setSlug(slug);
			product.setImage(filename);
			productRepo.save(product);
			try {
				Files.write(path, bytes);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return "redirect:/admin/products/add";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		Product product = productRepo.findById(id).orElse(new Product());
		List<Category> categories = (List<Category>) categoryRepo.findAll();
		model.addAttribute("product", product);
		model.addAttribute("categories", categories);
		return "admin/products/edit";
	}

	@PostMapping("/edit")
	public String edit(@Valid Product product, BindingResult bindingResult, MultipartFile file,
			RedirectAttributes redirectAttributes, Model model) throws IOException {
		Product currentProduct = productRepo.findById(product.getId()).orElse(new Product());
		List<Category> categories = (List<Category>) categoryRepo.findAll();
		model.addAttribute("categories", categories);
		if (bindingResult.hasErrors()) {

			return "redirect:/admin/products/edit/" + currentProduct.getId();
		}
		/*
		 * if (product.getCategoryId().equals("0") || product.getDescription().length()
		 * < 5 || product.getName().length() < 2) {
		 * redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
		 * redirectAttributes.addFlashAttribute("message", "error appear");
		 * redirectAttributes.addFlashAttribute("productName", product.getName());
		 * return "redirect:/admin/products/edit/" + currentProduct.getId(); }
		 */
		boolean fileOk = false;
		byte[] bytes = null;

		bytes = file.getBytes();

		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/media/" + filename);
		if (!file.isEmpty()) {
			if (filename.endsWith("jpg") || filename.endsWith("png")) {
				fileOk = true;
			}
		} else {
			fileOk = true;
		}

		redirectAttributes.addFlashAttribute("message", "Product edited");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		String slug = product.getName().toLowerCase().replace(" ", "-");

		Product productExists = productRepo.findBySlugAndIdNot(slug, product.getId());
		if (!fileOk) {
			redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("produts", product);
		} else if (productExists != null) {
			redirectAttributes.addFlashAttribute("message", "Product  exists ,choose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("produts", product);

		} else {
			product.setSlug(slug);
			if (!file.isEmpty()) {
				Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
				Files.delete(path2);
				product.setImage(filename);
				Files.write(path, bytes);
			} else {
				product.setImage(currentProduct.getImage());
			}

			productRepo.save(product);

		}
		return "redirect:/admin/products/edit/" + product.getId();
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) throws IOException {
		Product product = productRepo.findById(id).orElse(new Product());
		Product currentProduct = productRepo.findById(product.getId()).orElse(new Product());
		Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
		Files.delete(path2);
		productRepo.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "product deleted");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/admin/products";
	}

}
