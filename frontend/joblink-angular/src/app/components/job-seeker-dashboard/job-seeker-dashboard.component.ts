import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { ChartConfiguration, ChartOptions } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';

@Component({
  selector: 'app-job-seeker-dashboard',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  template: `
    <div class="container">
      <h2 class="mb-4">Job Seeker Dashboard</h2>
      
      <div class="row mb-4">
        <div class="col-md-4">
          <div class="card text-center">
            <div class="card-body">
              <h5 class="card-title">Total Applications</h5>
              <h3>{{ applications.length }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card text-center">
            <div class="card-body">
              <h5 class="card-title">Job Recommendations</h5>
              <h3>{{ recommendations.length }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card text-center">
            <div class="card-body">
              <h5 class="card-title">Skill Suggestions</h5>
              <h3>{{ skillSuggestions.length }}</h3>
            </div>
          </div>
        </div>
      </div>

      <div class="row mb-4">
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">Application Status</div>
            <div class="card-body">
              <canvas baseChart [data]="applicationStatusData" [type]="'doughnut'" [options]="chartOptions"></canvas>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">Job Recommendations</div>
            <div class="card-body">
              <div *ngFor="let rec of recommendations" class="mb-3 p-3 border rounded">
                <h6>{{ rec.job?.title }}</h6>
                <p class="mb-1"><strong>Match Score:</strong> {{ rec.matchScore }}%</p>
                <p class="mb-1">{{ rec.recommendationReason }}</p>
                <button class="btn btn-sm btn-primary" (click)="viewJob(rec.jobId)">View Job</button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">Skill Development Suggestions</div>
            <div class="card-body">
              <div *ngFor="let suggestion of skillSuggestions" class="mb-3 p-3 border rounded">
                <h6>{{ suggestion.skillName }}</h6>
                <p>{{ suggestion.reason }}</p>
                <p *ngIf="suggestion.suggestedCourse"><strong>Course:</strong> {{ suggestion.suggestedCourse }}</p>
                <p *ngIf="suggestion.suggestedCertification"><strong>Certification:</strong> {{ suggestion.suggestedCertification }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class JobSeekerDashboardComponent implements OnInit {
  applications: any[] = [];
  recommendations: any[] = [];
  skillSuggestions: any[] = [];
  applicationStatusData: ChartConfiguration<'doughnut'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      backgroundColor: ['#667eea', '#764ba2', '#f093fb', '#4facfe']
    }]
  };
  chartOptions: ChartOptions<'doughnut'> = {
    responsive: true,
    maintainAspectRatio: false
  };

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    // Mock job seeker ID - in real app, get from auth service
    const jobSeekerId = 1;
    this.loadDashboardData(jobSeekerId);
  }

  loadDashboardData(jobSeekerId: number) {
    // Load applications
    this.apiService.getApplicationsByJobSeeker(jobSeekerId).subscribe({
      next: (data) => {
        this.applications = data;
        this.updateApplicationStatusChart();
      }
    });

    // Load recommendations
    this.apiService.getRecommendations(jobSeekerId).subscribe({
      next: (data) => {
        this.recommendations = data;
      }
    });

    // Load skill suggestions
    this.apiService.getSkillSuggestions(jobSeekerId).subscribe({
      next: (data) => {
        this.skillSuggestions = data;
      }
    });
  }

  updateApplicationStatusChart() {
    const statusCounts: { [key: string]: number } = {};
    this.applications.forEach(app => {
      statusCounts[app.status] = (statusCounts[app.status] || 0) + 1;
    });

    this.applicationStatusData.labels = Object.keys(statusCounts);
    this.applicationStatusData.datasets[0].data = Object.values(statusCounts);
  }

  viewJob(jobId: number) {
    // Navigate to job details
    console.log('View job:', jobId);
  }
}

