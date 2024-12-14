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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {
    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;

    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
    }

    @GetMapping("/dashboard/")
    public String searchJobs(Model model,
                             @RequestParam(value = "job", required = false) String job
            , @RequestParam(value = "location", required = false) String location
            , @RequestParam(value = "partTime", required = false) String partTime
            , @RequestParam(value = "fullTime", required = false) String fullTime
            , @RequestParam(value = "freelancer", required = false) String freelancer
            , @RequestParam(value = "remoteOnly", required = false) String remoteOnly
            , @RequestParam(value = "officeOnly", required = false) String officeOnly
            , @RequestParam(value = "partialRemote", required = false) String partialRemote
            , @RequestParam(value = "today", required = false) boolean today
            , @RequestParam(value = "days7", required = false) boolean days7
            , @RequestParam(value = "days30", required = false) boolean days30) {

        model.addAttribute("partTime", Objects.equals(partTime,"Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime,"Full-Time"));
        model.addAttribute("freelancer", Objects.equals(freelancer,"Freelancer"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly,"Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly,"Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote,"Partial-Remote"));

        model.addAttribute("today",today);
        model.addAttribute("days7",days7);
        model.addAttribute("days30",days30);

        model.addAttribute("job",job);
        model.addAttribute("location",location);

        LocalDate searchDate=null;
        List<JobPostActivity> jobPost=null;

        boolean dateSearchFlag=true;
        boolean remote=true;
        boolean type=true;

        if (days30){
            searchDate=LocalDate.now().minusDays(30);
        }else if (days7){
            searchDate=LocalDate.now().minusDays(7);
        }else if (today){
            searchDate=LocalDate.now();
        }else dateSearchFlag=false;

        if (partTime==null && fullTime==null && freelancer==null){
            partTime="Part-Time";
            fullTime="Full-Time";
            freelancer="Freelancer";
            remote=false;
        }
        if (remoteOnly==null && officeOnly==null && partialRemote==null && today){
            remoteOnly="Remote-Only";
            officeOnly="Office-Only";
            partialRemote="Partial-Remote";
            today=false;
        }



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
