import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, CommonModule],
  template: `
    <div class="container">
      <div class="row">
        <div class="col-12 text-center mb-5">
          <h1 class="display-4 mb-3">Welcome to JobLink</h1>
          <p class="lead">Smart Job & Skill Matching Platform</p>
        </div>
      </div>
      <div class="row">
        <div class="col-md-4 mb-4">
          <div class="card h-100">
            <div class="card-body text-center">
              <h5 class="card-title">For Job Seekers</h5>
              <p class="card-text">Find your dream job with personalized recommendations based on your skills.</p>
              <a routerLink="/job-seeker/dashboard" class="btn btn-primary">Get Started</a>
            </div>
          </div>
        </div>
        <div class="col-md-4 mb-4">
          <div class="card h-100">
            <div class="card-body text-center">
              <h5 class="card-title">For Employers</h5>
              <p class="card-text">Post jobs and find the perfect candidates with skill-based matching.</p>
              <a routerLink="/employer/dashboard" class="btn btn-primary">Post a Job</a>
            </div>
          </div>
        </div>
        <div class="col-md-4 mb-4">
          <div class="card h-100">
            <div class="card-body text-center">
              <h5 class="card-title">Browse Jobs</h5>
              <p class="card-text">Explore available job opportunities and filter by skills, location, and salary.</p>
              <a routerLink="/jobs" class="btn btn-primary">Browse</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class HomeComponent {}

