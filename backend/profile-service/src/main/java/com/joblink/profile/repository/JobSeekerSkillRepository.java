package com.joblink.profile.repository;

import com.joblink.profile.model.JobSeekerSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSkillRepository extends JpaRepository<JobSeekerSkill, Long> {
    List<JobSeekerSkill> findByProfileId(Long profileId);
    List<JobSeekerSkill> findBySkillId(Long skillId);
}

