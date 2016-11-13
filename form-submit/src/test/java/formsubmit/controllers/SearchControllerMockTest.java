package formsubmit.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import formsubmit.Application;
import formsubmit.service.SearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SearchControllerMockTest {
	@Mock
	private SearchService searchService;

	@InjectMocks
	private SearchController searchController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(searchController).setRemoveSemicolonContent(false).build();
	}

	@Test
	public void should_search() throws Exception {

		when(searchService.search(anyString(), anyListOf(String.class)))
				.thenReturn(Arrays.asList(new Tweet(0, "tweetText", null, null, null, null, 0, null, null)));

		this.mockMvc.perform(get("/search/mixed;keywords=spring")).andExpect(status().isOk())
				.andExpect(view().name("searchtweets"))
				.andExpect(model().attribute("tweets", everyItem(hasProperty("text", is("tweetText")))));

		verify(searchService, times(1)).search(anyString(), anyListOf(String.class));
	}

}
