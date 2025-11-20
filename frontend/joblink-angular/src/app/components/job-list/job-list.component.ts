import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-job-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <div class="row mb-4">
        <div class="col-12">
          <h2>Job Listings</h2>
        </div>
      </div>
      <div class="row mb-4">
        <div class="col-md-3">
          <input type="text" class="form-control" placeholder="Location" [(ngModel)]="filters.location">
        </div>
        <div class="col-md-3">
          <select class="form-select" [(ngModel)]="filters.jobType">
            <option value="">All Types</option>
            <option value="FULL_TIME">Full Time</option>
            <option value="PART_TIME">Part Time</option>
            <option value="CONTRACT">Contract</option>
            <option value="REMOTE">Remote</option>
          </select>
        </div>
        <div class="col-md-3">
          <input type="number" class="form-control" placeholder="Min Salary" [(ngModel)]="filters.minSalary">
        </div>
        <div class="col-md-3">
          <button class="btn btn-primary w-100" (click)="searchJobs()">Search</button>
        </div>
      </div>
      <div class="row">
        <div class="col-12" *ngIf="loading">
          <div class="text-center">Loading...</div>
        </div>
        <div class="col-md-6 mb-4" *ngFor="let job of jobs">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">{{ job.title }}</h5>
              <p class="card-text">{{ job.description }}</p>
              <p><strong>Location:</strong> {{ job.location }}</p>
              <p><strong>Type:</strong> {{ job.jobType }}</p>
              <p *ngIf="job.salaryMin && job.salaryMax">
                <strong>Salary:</strong> ${{ job.salaryMin }} - ${{ job.salaryMax }}
              </p>
              <button class="btn btn-sm btn-primary" (click)="applyForJob(job.id)">Apply</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class JobListComponent implements OnInit {
  jobs: any[] = [];
  loading = false;
  filters = {
    location: '',
    jobType: '',
    minSalary: null as number | null,
    maxSalary: null as number | null
  };

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loadJobs();
  }

  loadJobs() {
    this.loading = true;
    this.apiService.getJobs().subscribe({
      next: (data) => {
        this.jobs = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading jobs:', error);
        this.loading = false;
      }
    });
  }

  searchJobs() {
    this.loading = true;
    const params: any = {};
    if (this.filters.location) params.location = this.filters.location;
    if (this.filters.jobType) params.jobType = this.filters.jobType;
    if (this.filters.minSalary) params.minSalary = this.filters.minSalary;

    this.apiService.searchJobs(params).subscribe({
      next: (data) => {
        this.jobs = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error searching jobs:', error);
        this.loading = false;
      }
    });
  }

  applyForJob(jobId: number) {
    // TODO: Implement application logic
    alert('Application feature coming soon!');
  }
}

