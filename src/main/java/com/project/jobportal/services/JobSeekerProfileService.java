package com.project.jobportal.services;

import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.entity.Users;
import com.project.jobportal.repository.JobSeekerProfileRepository;
import com.project.jobportal.repository.UsersRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {
    private JobSeekerProfileRepository profileRepo;
    private UsersRepository usersRepo;

    public JobSeekerProfileService(JobSeekerProfileRepository profileRepo, UsersRepository usersRepo) {
        this.profileRepo = profileRepo;
        this.usersRepo = usersRepo;
    }

    public Optional<JobSeekerProfile> getOne(int userId) {
        return profileRepo.findById(userId);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
        return profileRepo.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepo.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<JobSeekerProfile> jobSeeker = getOne(users.getUserId());
            return jobSeeker.orElse(null);
        }else return null;
    }
}
