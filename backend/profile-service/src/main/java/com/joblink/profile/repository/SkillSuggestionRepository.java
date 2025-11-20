package com.joblink.profile.repository;

import com.joblink.profile.model.SkillSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillSuggestionRepository extends JpaRepository<SkillSuggestion, Long> {
    List<SkillSuggestion> findByJobSeekerId(Long jobSeekerId);
}

