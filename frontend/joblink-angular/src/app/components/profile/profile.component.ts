import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <div class="row">
        <div class="col-md-8 offset-md-2">
          <div class="card">
            <div class="card-header">Profile Management</div>
            <div class="card-body">
              <form (ngSubmit)="saveProfile()">
                <div class="mb-3">
                  <label class="form-label">First Name</label>
                  <input type="text" class="form-control" [(ngModel)]="profile.firstName" name="firstName">
                </div>
                <div class="mb-3">
                  <label class="form-label">Last Name</label>
                  <input type="text" class="form-control" [(ngModel)]="profile.lastName" name="lastName">
                </div>
                <div class="mb-3">
                  <label class="form-label">Phone</label>
                  <input type="text" class="form-control" [(ngModel)]="profile.phone" name="phone">
                </div>
                <div class="mb-3">
                  <label class="form-label">Location</label>
                  <input type="text" class="form-control" [(ngModel)]="profile.location" name="location">
                </div>
                <div class="mb-3">
                  <label class="form-label">Bio</label>
                  <textarea class="form-control" rows="4" [(ngModel)]="profile.bio" name="bio"></textarea>
                </div>
                <div class="mb-3">
                  <label class="form-label">CV URL</label>
                  <input type="text" class="form-control" [(ngModel)]="profile.cvUrl" name="cvUrl">
                </div>
                <button type="submit" class="btn btn-primary">Save Profile</button>
              </form>
            </div>
          </div>

          <div class="card mt-4">
            <div class="card-header">Skills</div>
            <div class="card-body">
              <div class="mb-3">
                <select class="form-select" [(ngModel)]="selectedSkillId">
                  <option [value]="null">Select a skill</option>
                  <option *ngFor="let skill of availableSkills" [value]="skill.id">{{ skill.name }}</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label">Proficiency Level</label>
                <select class="form-select" [(ngModel)]="newSkill.proficiencyLevel">
                  <option value="BEGINNER">Beginner</option>
                  <option value="INTERMEDIATE">Intermediate</option>
                  <option value="ADVANCED">Advanced</option>
                  <option value="EXPERT">Expert</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label">Years of Experience</label>
                <input type="number" class="form-control" [(ngModel)]="newSkill.yearsOfExperience">
              </div>
              <button class="btn btn-primary" (click)="addSkill()">Add Skill</button>

              <div class="mt-4">
                <h6>Your Skills</h6>
                <div *ngFor="let skill of profile.skills" class="mb-2 p-2 border rounded">
                  <strong>{{ skill.skillName }}</strong> - {{ skill.proficiencyLevel }} 
                  ({{ skill.yearsOfExperience }} years)
                  <button class="btn btn-sm btn-danger float-end" (click)="removeSkill(skill.id)">Remove</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class ProfileComponent implements OnInit {
  profile: any = {
    userId: 'user1', // Mock user ID
    firstName: '',
    lastName: '',
    phone: '',
    location: '',
    bio: '',
    cvUrl: '',
    skills: []
  };
  availableSkills: any[] = [];
  selectedSkillId: number | null = null;
  newSkill = {
    skillId: 0,
    proficiencyLevel: 'BEGINNER',
    yearsOfExperience: 0
  };

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loadProfile();
    this.loadSkills();
  }

  loadProfile() {
    this.apiService.getProfile(this.profile.userId).subscribe({
      next: (data) => {
        this.profile = data;
      },
      error: (error) => {
        console.error('Error loading profile:', error);
      }
    });
  }

  loadSkills() {
    this.apiService.getAllSkills().subscribe({
      next: (data) => {
        this.availableSkills = data;
      }
    });
  }

  saveProfile() {
    this.apiService.createOrUpdateProfile(this.profile).subscribe({
      next: (data) => {
        this.profile = data;
        alert('Profile saved successfully!');
      },
      error: (error) => {
        console.error('Error saving profile:', error);
        alert('Error saving profile');
      }
    });
  }

  addSkill() {
    if (!this.selectedSkillId) {
      alert('Please select a skill');
      return;
    }

    this.newSkill.skillId = this.selectedSkillId;
    this.apiService.addSkill(this.profile.id, this.newSkill).subscribe({
      next: (data) => {
        this.profile.skills.push(data);
        this.selectedSkillId = null;
        this.newSkill = {
          skillId: 0,
          proficiencyLevel: 'BEGINNER',
          yearsOfExperience: 0
        };
      },
      error: (error) => {
        console.error('Error adding skill:', error);
      }
    });
  }

  removeSkill(skillId: number) {
    // TODO: Implement remove skill API call
    this.profile.skills = this.profile.skills.filter((s: any) => s.id !== skillId);
  }
}

