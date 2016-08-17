package formsubmit.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TweetControllers {
	
	@Autowired
    private Twitter twitter;
	
	@RequestMapping("/tweetsearch")
    public String tweetsearchPage() {
        return "tweetsearchPage";
    }
	@RequestMapping("/searchtweets")
    public String search(@RequestParam(name="search",defaultValue = "lafod") String search,
    		@RequestParam(name="starting",defaultValue = "0") int fromIndex,
    		@RequestParam(name="ending",defaultValue = "10") int toIndex,
    		Model model) {
		SearchResults searchResults = twitter.searchOperations().search(search,toIndex);
		List<Tweet> tweets = searchResults.getTweets()
				.stream()
				.collect(Collectors.toList());
		for(Tweet t : tweets){
			t.getUser().getName();
		}
		
        model.addAttribute("tweets", tweets);
        model.addAttribute("search", search);
        return "searchtweets";
    }
	@RequestMapping(value = "/postsearchtweets", method = RequestMethod.POST)
	public String postSearch(HttpServletRequest request,
	    RedirectAttributes redirectAttributes) {
	        String search = request.getParameter("search");
	        if (search.toLowerCase().contains("struts")) {
	                redirectAttributes.addFlashAttribute("error", "Try using spring instead!");
	                return "redirect:tweetsearch";
	        }
	        redirectAttributes.addAttribute("search", search);
	        return "redirect:searchtweets";
	}
}
