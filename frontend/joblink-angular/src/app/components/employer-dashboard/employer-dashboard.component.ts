import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { ChartConfiguration, ChartOptions } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';

@Component({
  selector: 'app-employer-dashboard',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  template: `
    <div class="container">
      <h2 class="mb-4">Employer Dashboard</h2>
      
      <div class="row mb-4">
        <div class="col-md-3">
          <div class="card text-center">
            <div class="card-body">
              <h5 class="card-title">Total Jobs</h5>
              <h3>{{ jobs.length }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card text-center">
            <div class="card-body">
              <h5 class="card-title">Total Applications</h5>
              <h3>{{ totalApplications }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card text-center">
            <div class="card-body">
              <h5 class="card-title">Pending Reviews</h5>
              <h3>{{ pendingApplications }}</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card text-center">
            <div class="card-body">
              <h5 class="card-title">Scheduled Interviews</h5>
              <h3>{{ scheduledInterviews }}</h3>
            </div>
          </div>
        </div>
      </div>

      <div class="row mb-4">
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">Applications by Status</div>
            <div class="card-body">
              <canvas baseChart [data]="applicationStatusData" [type]="'bar'" [options]="chartOptions"></canvas>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">Applications by Job</div>
            <div class="card-body">
              <canvas baseChart [data]="applicationsByJobData" [type]="'pie'" [options]="chartOptions"></canvas>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">Recent Applications</div>
            <div class="card-body">
              <table class="table">
                <thead>
                  <tr>
                    <th>Job Title</th>
                    <th>Applicant</th>
                    <th>Status</th>
                    <th>Applied Date</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  <tr *ngFor="let app of recentApplications">
                    <td>{{ getJobTitle(app.jobId) }}</td>
                    <td>Applicant #{{ app.jobSeekerId }}</td>
                    <td><span class="badge" [ngClass]="getStatusClass(app.status)">{{ app.status }}</span></td>
                    <td>{{ app.appliedDate | date:'short' }}</td>
                    <td>
                      <button class="btn btn-sm btn-primary" (click)="viewApplication(app)">View</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class EmployerDashboardComponent implements OnInit {
  jobs: any[] = [];
  allApplications: any[] = [];
  recentApplications: any[] = [];
  totalApplications = 0;
  pendingApplications = 0;
  scheduledInterviews = 0;

  applicationStatusData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      label: 'Applications',
      backgroundColor: '#667eea'
    }]
  };

  applicationsByJobData: ChartConfiguration<'pie'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      backgroundColor: ['#667eea', '#764ba2', '#f093fb', '#4facfe']
    }]
  };

  chartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false
  };

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    // Load jobs (mock employer ID)
    const employerId = 1;
    this.apiService.getJobs().subscribe({
      next: (data) => {
        this.jobs = data.filter(job => job.employerId === employerId);
        this.loadApplications();
      }
    });
  }

  loadApplications() {
    const applicationPromises = this.jobs.map(job =>
      this.apiService.getApplicationsByJob(job.id).toPromise()
    );

    Promise.all(applicationPromises).then(results => {
      this.allApplications = results.flat();
      this.totalApplications = this.allApplications.length;
      this.pendingApplications = this.allApplications.filter(a => a.status === 'PENDING').length;
      this.scheduledInterviews = this.allApplications
        .flatMap(a => a.interviews || [])
        .filter(i => i.status === 'SCHEDULED').length;
      
      this.recentApplications = this.allApplications.slice(0, 10);
      this.updateCharts();
    });
  }

  updateCharts() {
    // Application status chart
    const statusCounts: { [key: string]: number } = {};
    this.allApplications.forEach(app => {
      statusCounts[app.status] = (statusCounts[app.status] || 0) + 1;
    });

    this.applicationStatusData.labels = Object.keys(statusCounts);
    this.applicationStatusData.datasets[0].data = Object.values(statusCounts);

    // Applications by job chart
    const jobCounts: { [key: string]: number } = {};
    this.allApplications.forEach(app => {
      const jobTitle = this.getJobTitle(app.jobId);
      jobCounts[jobTitle] = (jobCounts[jobTitle] || 0) + 1;
    });

    this.applicationsByJobData.labels = Object.keys(jobCounts);
    this.applicationsByJobData.datasets[0].data = Object.values(jobCounts);
  }

  getJobTitle(jobId: number): string {
    const job = this.jobs.find(j => j.id === jobId);
    return job ? job.title : 'Unknown';
  }

  getStatusClass(status: string): string {
    const classes: { [key: string]: string } = {
      'PENDING': 'bg-warning',
      'ACCEPTED': 'bg-success',
      'REJECTED': 'bg-danger',
      'INTERVIEW': 'bg-info'
    };
    return classes[status] || 'bg-secondary';
  }

  viewApplication(application: any) {
    // TODO: Implement view application details
    console.log('View application:', application);
  }
}

