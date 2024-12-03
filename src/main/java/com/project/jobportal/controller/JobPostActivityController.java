package com.project.jobportal.controller;

import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.entity.RecruiterJobsDto;
import com.project.jobportal.entity.RecruiterProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.services.JobPostActivityService;
import com.project.jobportal.services.UsersService;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@Controller
public class JobPostActivityController {
    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;

    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
    }

    @GetMapping("/dashboard/")
    public String searchJobs(Model model) {
        Object currentUserProfile = usersService.getCurrentUserprofile();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            model.addAttribute("username", currentUserName);
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
            List<RecruiterJobsDto> recruiterJobs =
                    jobPostActivityService.getRecruiterJobs(((RecruiterProfile) currentUserProfile).getUserAccountId());
            model.addAttribute("jobPost", recruiterJobs);
        }
        model.addAttribute("user", currentUserProfile);

        return "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model) {
        model.addAttribute("user", usersService.getCurrentUserprofile());
        model.addAttribute("jobPostActivity", new JobPostActivity());
        return "add-jobs";
    }

    @PostMapping("/dashboard/addNew")
    public String addNewJob(JobPostActivity jobPostActivity, Model model) {
        Users user = usersService.getCurrentUser();
        if (user != null) {
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPostActivity", jobPostActivity);
        jobPostActivityService.addNew(jobPostActivity);
        return "redirect:/dashboard/";
    }

    @PostMapping("/dashboard/edit/{id}")
    public String editJob(@PathVariable("id") int id, Model model) {
        JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
        model.addAttribute("jobPostActivity", jobPostActivity);
        model.addAttribute("user", usersService.getCurrentUserprofile());
        return "add-jobs";

    }
}
