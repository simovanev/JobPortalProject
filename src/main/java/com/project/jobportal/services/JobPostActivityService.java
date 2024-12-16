package com.project.jobportal.services;

import com.project.jobportal.entity.*;
import com.project.jobportal.repository.JobPostActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JobPostActivityService {
    private final JobPostActivityRepository postActivityRepository;

    public JobPostActivityService(JobPostActivityRepository postActivityRepository) {
        this.postActivityRepository = postActivityRepository;
    }
    public JobPostActivity addNew(JobPostActivity jobPostActivity){
        return postActivityRepository.save(jobPostActivity);
    }
    public List<RecruiterJobsDto> getRecruiterJobs(int recruiter){
        List<IRecruiterJob> recruiterJobsDtos = postActivityRepository.getRecruiterJobs(recruiter);
        List<RecruiterJobsDto> recruiterJobsDtoList = new ArrayList<>();
        for(IRecruiterJob recruiterJob : recruiterJobsDtos){
            JobLocation loc=
                    new JobLocation(recruiterJob.getLocationId(),recruiterJob.getCity()
                            ,recruiterJob.getCountry(),recruiterJob.getState());
            JobCompany com=new JobCompany(recruiterJob.getCompanyId(),recruiterJob.getName(),"");
            recruiterJobsDtoList
                    .add(new RecruiterJobsDto(recruiterJob.getTotalCandidates(),recruiterJob.getJob_post_id()
                            ,recruiterJob.getJob_title(),loc,com));
        }
        return recruiterJobsDtoList;
    }

    public JobPostActivity getOne(int id) {
      return   postActivityRepository.findById(id).orElseThrow(()->new RuntimeException("Job not found"));
    }


    public List<JobPostActivity> getAll() {
        return postActivityRepository.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote, LocalDate searchDate) {
    return Objects.isNull(searchDate)?postActivityRepository.searchWithoutDate(job,location,type,remote):
            postActivityRepository.search(job,location,type,remote,searchDate);
    }
}
