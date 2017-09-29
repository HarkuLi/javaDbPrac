package controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class ShowUsers {
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String get(ModelMap model) {
		System.out.println("spring controller");
		return "user";
	}
}