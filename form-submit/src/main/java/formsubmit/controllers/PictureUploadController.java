package formsubmit.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import formsubmit.config.PictureUploadProperties;
import formsubmit.domain.ProfileForm;
import formsubmit.domain.UserProfileSession;

@Controller
@SessionAttributes("picturePath")
public class PictureUploadController {
	private final Resource picturesDir;
	private final Resource anonymousPicture;
	private final MessageSource messageSource;
	private UserProfileSession userProfileSession;

	@Autowired
	public PictureUploadController(PictureUploadProperties uploadProperties, MessageSource message,UserProfileSession userProfileSession) {
	    picturesDir = uploadProperties.getUploadPath();
	    anonymousPicture = uploadProperties.getAnonymousPicture();
	    messageSource = message;	
	    this.userProfileSession = userProfileSession;
	}	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleIOException(Locale locale) {
	    ModelAndView modelAndView = new ModelAndView("error");
	    modelAndView.addObject("error", messageSource.getMessage("upload.io.exception", null, locale));
	    return modelAndView;
	}
	
	@ModelAttribute("picturePath")
	public Resource picturePath() {
	  return anonymousPicture;
	}
	@ModelAttribute
    public ProfileForm getProfileForm() {
        return userProfileSession.toForm();
    }
	@RequestMapping("uploadError")
	public ModelAndView onUploadError(HttpServletRequest request) {
	    ModelAndView modelAndView = new ModelAndView("profile/profilePage");
	    modelAndView.addObject("error", request.getAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE));
	    return modelAndView;
	}
	
	@RequestMapping(value = "/uploadedPicture")
	public void getUploadedPicture(HttpServletResponse response) throws IOException {
		Resource picturePath = userProfileSession.getprofilePicture();
        if (picturePath == null) {
            picturePath = anonymousPicture;
        }
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));
	    //Files.copy(picturePath., response.getOutputStream());
	    //throw new IOException();
	    IOUtils.copy(picturePath.getInputStream(), response.getOutputStream());
	}

	@RequestMapping("upload")
	public String uploadPage() {
		return "profile/uploadPage";
	}

	@RequestMapping(value = "/profile", params = {"upload"},method = RequestMethod.POST)
	public String onUpload(MultipartFile file, RedirectAttributes redirectAttrs, Model model) throws IOException {

		if (file.isEmpty() || !isImage(file)) {
			redirectAttrs.addFlashAttribute("error", "Incorrect file. Please upload a picture.");
			return "redirect:/profile";
		}

		Resource picturePath = copyFileToPictures(file);
		userProfileSession.setProfilePicture(picturePath);		
		return "profile/profilePage";
	}

	private Resource copyFileToPictures(MultipartFile file) throws IOException {
		String fileExtension = getFileExtension(file.getOriginalFilename());
		File tempFile = File.createTempFile("pic", fileExtension, picturesDir.getFile());
		try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {

			IOUtils.copy(in, out);
		}
		return new FileSystemResource(tempFile);
	}

	private boolean isImage(MultipartFile file) {
		return file.getContentType().startsWith("image");
	}

	private static String getFileExtension(String name) {
		return name.substring(name.lastIndexOf("."));
	}
}
