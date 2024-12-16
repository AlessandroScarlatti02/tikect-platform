package it.milestone.exam.tikect_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.milestone.exam.tikect_platform.model.Category;
import it.milestone.exam.tikect_platform.repository.CategoryRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepo;

    @GetMapping()
    public String categories(Model model) {

        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("newCategory", new Category());

        return "categories/index";
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute Category categoryForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttribute, Model model) {

        List<Category> optionalList = categoryRepo.findByName(categoryForm.getName());

        if (!optionalList.isEmpty()) {
            bindingResult.addError(new ObjectError("unique", "This Category already exists"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("newCategory", categoryForm);
            return "categories/index";
        }
        categoryRepo.save(categoryForm);
        return "redirect:/categories";

    }
}
