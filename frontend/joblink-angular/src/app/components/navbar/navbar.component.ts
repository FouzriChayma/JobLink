import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, CommonModule],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
      <div class="container-fluid">
        <a class="navbar-brand" routerLink="/">
          <strong>JobLink</strong>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <a class="nav-link" routerLink="/">Home</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/jobs">Jobs</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/job-seeker/dashboard">Job Seeker Dashboard</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/employer/dashboard">Employer Dashboard</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/profile">Profile</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  `
})
export class NavbarComponent {}

