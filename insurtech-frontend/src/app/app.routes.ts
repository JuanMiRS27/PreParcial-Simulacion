import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ClaimListComponent } from './pages/claim-list/claim-list.component';
import { ClaimFormComponent } from './pages/claim-form/claim-form.component';
import { ClaimDetailComponent } from './pages/claim-detail/claim-detail.component';
import { EvaluationResultComponent } from './pages/evaluation-result/evaluation-result.component';
import { AdminDashboardComponent } from './pages/admin/admin-dashboard.component';
import { AdminOverviewComponent } from './pages/admin/admin-overview.component';
import { AdminClaimsComponent } from './pages/admin/admin-claims.component';
import { AdminParametersComponent } from './pages/admin/admin-parameters.component';
import { AdminUsersComponent } from './pages/admin/admin-users.component';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard],
    children: [
      { path: 'claims', component: ClaimListComponent },
      { path: 'claims/new', component: ClaimFormComponent },
      { path: 'claims/:id', component: ClaimDetailComponent },
      { path: 'evaluations/:claimId', component: EvaluationResultComponent },
      { path: '', pathMatch: 'full', redirectTo: 'claims' }
    ]
  },
  {
    path: 'admin',
    component: AdminDashboardComponent,
    canActivate: [authGuard, adminGuard],
    children: [
      { path: 'overview', component: AdminOverviewComponent },
      { path: 'claims', component: AdminClaimsComponent },
      { path: 'parameters', component: AdminParametersComponent },
      { path: 'users', component: AdminUsersComponent },
      { path: '', pathMatch: 'full', redirectTo: 'overview' }
    ]
  },
  { path: '', pathMatch: 'full', redirectTo: 'login' }
];
