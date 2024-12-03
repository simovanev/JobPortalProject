package com.project.jobportal.services;

import com.project.jobportal.entity.JobPostActivity;
import com.project.jobportal.repository.JobPostActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class JobPostActivityService {
    private final JobPostActivityRepository postActivityRepository;

    public JobPostActivityService(JobPostActivityRepository postActivityRepository) {
        this.postActivityRepository = postActivityRepository;
    }
    public JobPostActivity addNew(JobPostActivity jobPostActivity){
        return postActivityRepository.save(jobPostActivity);
    }
}
