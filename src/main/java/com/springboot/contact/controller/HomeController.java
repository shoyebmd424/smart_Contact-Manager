package com.springboot.contact.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.contact.Entity.User;
import com.springboot.contact.dao.UserRepository;
import com.springboot.contact.helper.Message;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userrepo;

	@RequestMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home Contact Manager");
		return "home";
	}

	@RequestMapping("/about")
	public String About(Model m) {
		m.addAttribute("title", "About Contact Manager");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "Sign Up Contact Manager");
		m.addAttribute("user", new User());
		return "signup";
	}
  
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agree", defaultValue = "false") boolean agree, Model m,
			HttpSession session) {
		try {
			if (!agree) {
				System.out.println("please agree of agree TM&C");
				session.setAttribute("message", new Message("please agree TM&C!! ", "alert-danger"));
//				throw new Exception("please agree of agree TM&C");
				m.addAttribute("user", user);
				return "signup";
			}
			if(result.hasErrors()) {
				System.out.println("Errors "+result.toString());
				m.addAttribute("user",user);
				return "signup";
			}
			user.setImageulr("bg.jpg");
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			System.out.println("agreement" + agree);
			System.out.println("User" + user);
			
			if(this.userrepo.existsByEmail(user.getEmail())) {
				session.setAttribute("message", new Message("Email already exists!  please Sign In ", "alert-danger"));
				m.addAttribute("user", user);
				return "signup";
			}
			
			
			this.userrepo.save(user);
			m.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered!! ", "alert-success"));
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!! ", "alert-danger"));
			System.out.println(e.getMessage());
			return "signup";
		}
	}

	// login 
	@GetMapping("/signin")
	public String LoginHandler() {
		return "login";
	}
	@PostMapping("/login")
	public String login() {
		return "login";
	}
}
