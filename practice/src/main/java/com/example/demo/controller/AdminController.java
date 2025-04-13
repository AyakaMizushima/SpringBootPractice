package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.AdminSignup;
import com.example.demo.entity.Contact;
import com.example.demo.form.LoginForm;
import com.example.demo.repository.AdminSignupRepository;
import com.example.demo.repository.ContactRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@GetMapping("/signup")
	public String showAdminSignupForm(Model model) {
		model.addAttribute("contactForm", new AdminSignup());
		return "admin/signup";
	}
	@PostMapping("/signup/confirm")
	public String confirmSignup(@ModelAttribute ("ContactForm") AdminSignup contactForm, Model model) {
		model.addAttribute("contactForm", contactForm);
		return "admin/signup_confirmation";
	}
	@Autowired
	private AdminSignupRepository adminSignupRepository;
	@Autowired
	private ContactRepository contactRepository;
	
	@PostMapping("/signup/register")
	public String registerAdmin(@ModelAttribute("contactForm") AdminSignup contactForm) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		contactForm.setPassword(encoder.encode(contactForm.getPassword()));

		adminSignupRepository.save(contactForm);
		return "admin/signup_completion";
	}
	@GetMapping("/signin")
	public String showSigninForm(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", "ログインIDまたはパスワードが間違っています");
		}
		model.addAttribute("loginForm", new LoginForm());
		return "admin/signin";
	}
	@GetMapping("/contacts_list")
	public String showContactList(Model model) {
		List<Contact> contactList = contactRepository.findAll();
		model.addAttribute("userList" ,contactList);
		return "admin/contacts_list";
	}
	@GetMapping("/contacts/{id}")
	public String showContactDetail(@PathVariable Long id, Model model) {
		Optional<Contact> contactOpt = contactRepository.findById(id);
		if (contactOpt.isPresent()) {
			model.addAttribute("contact", contactOpt.get());
			return "admin/contact_detail";
		} else {
			return "redirect:/admin/contacts_list";
		}
	}
	@GetMapping("/contacts/{id}/edit")
	public String editContact(@PathVariable Long id, Model model, HttpServletRequest request) {
		Contact contact = contactRepository.findById(id).orElseThrow();
		model.addAttribute("contact", contact);
		
		CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
		model.addAttribute("_csrf", csrfToken);
		
		return "admin/contact_edit";
	}
	@GetMapping("/contacts/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {
		Optional<Contact> contactOpt = contactRepository.findById(id);
		if (contactOpt.isPresent()) {
			model.addAttribute("contact", contactOpt.get());
			
			CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
			model.addAttribute("_csrf", csrfToken);
			
			return "admin/contact_edit";
		} else {
			return "redirect:/admin/contacts_list";
		}
	}
	@PostMapping("/contacts/update")
	public String updateContact(@ModelAttribute Contact contact) {
		Optional<Contact> existingOpt = contactRepository.findById(contact.getId());

		if (existingOpt.isPresent()) {
			Contact existing = existingOpt.get();

			existing.setFirstName(contact.getFirstName());
			existing.setLastName(contact.getLastName());
			existing.setEmail(contact.getEmail());
			existing.setPhone(contact.getPhone());
			existing.setZipCode(contact.getZipCode());
			existing.setAddress(contact.getAddress());
			existing.setBuildingName(contact.getBuildingName());
			existing.setContactType(contact.getContactType());
			existing.setBody(contact.getBody());

			existing.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

			contactRepository.saveAndFlush(existing);
		}
		return "redirect:/admin/contacts/" + contact.getId();
	}
	@PostMapping("/contacts/delete")
	public String deleteContact(@RequestParam("id") Long id) {
		contactRepository.deleteById(id);
		return "redirect:/admin/contacts_list";
	}
}
