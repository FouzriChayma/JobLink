import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { JobListComponent } from './components/job-list/job-list.component';
import { JobSeekerDashboardComponent } from './components/job-seeker-dashboard/job-seeker-dashboard.component';
import { EmployerDashboardComponent } from './components/employer-dashboard/employer-dashboard.component';
import { ProfileComponent } from './components/profile/profile.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'jobs', component: JobListComponent },
  { path: 'job-seeker/dashboard', component: JobSeekerDashboardComponent },
  { path: 'employer/dashboard', component: EmployerDashboardComponent },
  { path: 'profile', component: ProfileComponent },
  { path: '**', redirectTo: '' }
];

