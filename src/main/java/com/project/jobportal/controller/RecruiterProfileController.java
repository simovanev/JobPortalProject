package com.project.jobportal.controller;

import ch.qos.logback.core.util.StringUtil;
import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.RecruiterProfileRepository;
import com.project.jobportal.repository.UsersRepository;
import com.project.jobportal.services.RecruiterProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {
    private UsersRepository usersRepository;
    private RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(UsersRepository usersRepository, RecruiterProfileService recruiterProfileService) {
        this.usersRepository = usersRepository;
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping("/")
    public String recruiterProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String currentName = authentication.getName();
            Users currentUser = usersRepository.findByEmail(currentName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getById(currentUser.getUserId());
            if (recruiterProfile.isPresent()) {
                model.addAttribute("profile", recruiterProfile.get());
            }
        }
        return "recruiter_profile";
    }
    @GetMapping("/recruiter-profile/addNew")
    public String addNew(RecruiterProfile recruiterProfile, @RequestParam("image")MultipartFile multipartFile, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String currentName = authentication.getName();
            Users user= usersRepository.findByEmail(currentName).orElseThrow(()->new UsernameNotFoundException("User not found"));
            recruiterProfile.setUserId(user);
            recruiterProfile.setUserAccountId(user.getUserId());
        }
        model.addAttribute("profile", recruiterProfile);
        String fileName="";
        if (!multipartFile.getOriginalFilename().equals("")){
            fileName= StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(fileName);
        }
        RecruiterProfile saveUser=recruiterProfileService.addNew(recruiterProfile);

        String uploadDir="photos/recruiter/"+saveUser.getUserAccountId();

        return "dashboard";
    }
}
