import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8765/api';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) {}

  // Job Posting Service
  getJobs(): Observable<any[]> {
    return this.http.get<any[]>(`${API_URL}/jobs`);
  }

  getJobById(id: number): Observable<any> {
    return this.http.get<any>(`${API_URL}/jobs/${id}`);
  }

  searchJobs(params: any): Observable<any[]> {
    return this.http.get<any[]>(`${API_URL}/jobs/search`, { params });
  }

  createJob(job: any): Observable<any> {
    return this.http.post<any>(`${API_URL}/jobs`, job, { headers: this.headers });
  }

  // Profile Service
  getProfile(userId: string): Observable<any> {
    return this.http.get<any>(`${API_URL}/profiles/user/${userId}`);
  }

  createOrUpdateProfile(profile: any): Observable<any> {
    return this.http.post<any>(`${API_URL}/profiles`, profile, { headers: this.headers });
  }

  addSkill(profileId: number, skill: any): Observable<any> {
    return this.http.post<any>(`${API_URL}/profiles/${profileId}/skills`, skill, { headers: this.headers });
  }

  getAllSkills(): Observable<any[]> {
    return this.http.get<any[]>(`${API_URL}/profiles/skills`);
  }

  getSkillSuggestions(jobSeekerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${API_URL}/profiles/${jobSeekerId}/suggestions`);
  }

  // Matching Service
  generateRecommendations(jobSeekerId: number): Observable<any[]> {
    return this.http.post<any[]>(`${API_URL}/matching/generate/${jobSeekerId}`, {});
  }

  getRecommendations(jobSeekerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${API_URL}/matching/${jobSeekerId}`);
  }

  getUnviewedRecommendations(jobSeekerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${API_URL}/matching/${jobSeekerId}/unviewed`);
  }

  markRecommendationAsViewed(recommendationId: number): Observable<void> {
    return this.http.put<void>(`${API_URL}/matching/${recommendationId}/viewed`, {});
  }

  // Application Service
  createApplication(application: any): Observable<any> {
    return this.http.post<any>(`${API_URL}/applications`, application, { headers: this.headers });
  }

  getApplicationsByJobSeeker(jobSeekerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${API_URL}/applications/job-seeker/${jobSeekerId}`);
  }

  getApplicationsByJob(jobId: number): Observable<any[]> {
    return this.http.get<any[]>(`${API_URL}/applications/job/${jobId}`);
  }

  updateApplicationStatus(id: number, status: string): Observable<any> {
    return this.http.put<any>(`${API_URL}/applications/${id}/status?status=${status}`, {});
  }

  scheduleInterview(interview: any): Observable<any> {
    return this.http.post<any>(`${API_URL}/applications/interviews`, interview, { headers: this.headers });
  }
}

