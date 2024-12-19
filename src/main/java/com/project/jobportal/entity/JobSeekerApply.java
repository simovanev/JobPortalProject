package com.project.jobportal.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"job", "userId"}))
public class JobSeekerApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_account_id")
    private JobSeekerProfile userId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job", referencedColumnName = "jobPostId")
    private JobPostActivity job;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date applyDate;
    private String coverLetter;

    public JobSeekerApply() {
    }

    public JobSeekerApply(Integer id, JobSeekerProfile userId, JobPostActivity job, Date applyDAte, String coverLetter) {
        this.id = id;
        this.userId = userId;
        this.job = job;
        this.applyDate = applyDAte;
        this.coverLetter = coverLetter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JobSeekerProfile getUserId() {
        return userId;
    }

    public void setUserId(JobSeekerProfile userId) {
        this.userId = userId;
    }

    public JobPostActivity getJob() {
        return job;
    }

    public void setJob(JobPostActivity job) {
        this.job = job;
    }

    public Date getApplyDAte() {
        return applyDate;
    }

    public void setApplyDAte(Date applyDAte) {
        this.applyDate = applyDAte;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    @Override
    public String toString() {
        return "JobSeekerApply{" +
                "id=" + id +
                ", userId=" + userId +
                ", job=" + job +
                ", applyDAte=" + applyDate +
                ", coverLetter='" + coverLetter + '\'' +
                '}';
    }
}
