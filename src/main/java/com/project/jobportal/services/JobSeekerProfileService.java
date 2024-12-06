package com.project.jobportal.services;

import com.project.jobportal.entity.JobSeekerProfile;
import com.project.jobportal.repository.JobSeekerProfileRepository;
import com.project.jobportal.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {
    private JobSeekerProfileRepository profileRepo;

    public JobSeekerProfileService(JobSeekerProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }

    public Optional<JobSeekerProfile> getOne(int userId) {
        return profileRepo.findById(userId);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
        return profileRepo.save(jobSeekerProfile);
    }
}
