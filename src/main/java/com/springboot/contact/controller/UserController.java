package com.springboot.contact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.protobuf.Option;
import com.springboot.contact.Entity.Contact;
import com.springboot.contact.Entity.User;
import com.springboot.contact.dao.ContactReposetory;
import com.springboot.contact.dao.UserRepository;
import com.springboot.contact.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepository userrepo;
	@Autowired
	ContactReposetory contactrepo;

	@ModelAttribute
	public void addcommon(Model m, Principal p) {
		String username = p.getName();
		User user = userrepo.findByEmail(username);
		m.addAttribute("user", user);
		System.out.println(user);
	}

	@RequestMapping("/index")
	public String dashbord(Model m, Principal p) {

		m.addAttribute("title", "User Home page");
		return "normal/dashboard";
	}

	@GetMapping("/add-contact")
	public String openadd(Model m) {
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());

		return "normal/add_contact";
	}

	// processing
	@PostMapping("/contact-process")
	public String process(@ModelAttribute("contact") Contact contact, @RequestParam("profileimage") MultipartFile file,
			Principal p , HttpSession session) {
		try {

			/* file processing */
			if (file.isEmpty()) {
				System.out.println("your file is empry");
			} else {
				contact.setImage(file.getOriginalFilename());
				File savefile = new ClassPathResource("static/image/").getFile();
				Path path =Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("uploaded"+path);
			}

			String name = p.getName();
			User user = userrepo.findByEmail(name);

			contact.setUser(user);
			user.getContact().add(contact);
			this.userrepo.save(user);
			System.out.println(contact);
			session.setAttribute("message", new Message("success fully uploaded/added", "success"));
		} catch (Exception e) {
			System.out.println("error is :" + e.getMessage());
			session.setAttribute("message", new Message("some thing went Wrong", "danger"));
			e.printStackTrace();
		}
		return "normal/add_contact";
	}
	@GetMapping("/show-contacts/{page}")
	public String contactshow( @PathVariable("page") Integer page,Model m,Principal p) {
		m.addAttribute("title","All Contacts");
		String username=p.getName();
		User user=userrepo.findByEmail(username);
		// peageable = current + perpage contact
		 Pageable peageable=PageRequest.of(page, 5);
		Page<Contact> contacts=this.contactrepo.FindContactsbyUser(user.getId(),peageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("cpage",page);
		m.addAttribute("total", contacts.getTotalPages());
		return "normal/show_contact";
	}
	@GetMapping("/{id}/contacts/")
	public String contactDetails(@PathVariable("id") Integer id,Model m ,Principal p) {
		m.addAttribute("title", " Contact Detail");
	Optional<Contact> contactop=contactrepo.findById(id);
	Contact contact=contactop.get();
	String username=p.getName();
	User user=userrepo.findByEmail(username);
	if(user.getId()==contact.getUser().getId()) {
	m.addAttribute("contact",contact);
	}
		return "normal/details";
	}
	@GetMapping("/delete/{cid}")
	public String delete(@PathVariable("cid") Integer cid,HttpSession session ,Model m,Principal p) {
		m.addAttribute("title", "delete page");
		Contact contact=contactrepo.findById(cid).get();
//		contact.setUser(null);
//		this.contactrepo.delete(contact);
		User user=this.userrepo.findByEmail(p.getName());
		user.getContact().remove(contact);
		this.userrepo.save(user);
		session.setAttribute("message", new Message("Your Contact succesfully deleted.....", "success"));
		return "redirect:/user/show-contacts/0";
	}
	// update handler
	@PostMapping("/update-contact/{cid}")
	public String updateform(Model m,@PathVariable("cid") Integer cid) {
		Contact contact=this.contactrepo.findById(cid).get();
		m.addAttribute("contact",contact);
		m.addAttribute("title","Update");
		return "normal/update";
	}
	// update process handler
	@PostMapping("/update-process")
	public String updatehandle(@ModelAttribute Contact contact ,Principal p,@RequestParam("profileimage") MultipartFile file,Model m ,HttpSession session) {
		
		try {
			Contact oldcontact=this.contactrepo.findById(contact.getCid()).get();
			if(!file.isEmpty()) {
				
//				delete old 
				File deletefile=new ClassPathResource("static/image").getFile();
				
				File file1=new File(deletefile ,oldcontact.getImage());
				file1.delete();
				// new photo
				contact.setImage(file.getOriginalFilename());
				File f = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(f.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename()); 
			}else {
				contact.setImage(oldcontact.getImage());
			}
			User user=this.userrepo.findByEmail(p.getName());
			contact.setUser(user);
			this.contactrepo.save(contact);
			session.setAttribute("message", new Message("Your Contact is Updated", "success"));
			
		}catch(Exception e){
		
		}
		
		
		
		return "redirect:/user/"+contact.getCid()+"/contacts/";
	}
	@GetMapping("/profile")
	public String profile(Model m) {
		m.addAttribute("title","Profile");
		return "normal/profile";
	}

}
