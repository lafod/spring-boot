package formsubmit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import formsubmit.domain.Greeting;

@Controller
public class GreetingController {
	@RequestMapping(value="/greeting", method=RequestMethod.GET)
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @RequestMapping(value="/greeting", method=RequestMethod.POST)
    public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
        model.addAttribute("greeting", greeting);
        return "result";
    }
    @RequestMapping(value="/results", method=RequestMethod.GET)
    public String showResults(@RequestParam(value="name",defaultValue = "world") String userName, Model model) {        
    	 model.addAttribute("message", "Hello, " + userName);
    	 model.addAttribute("lang", "Java");
    	return "home/resultPage";
    }
    
}
