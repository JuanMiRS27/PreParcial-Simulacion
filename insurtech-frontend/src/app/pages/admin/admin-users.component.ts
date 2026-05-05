import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../core/services/admin.service';
import { AdminUser } from '../../models/admin.model';

@Component({
  selector: 'app-admin-users',
  standalone: true,
  imports: [FormsModule],
  template: `<section class="panel">
    <h3 class="panel-title">Usuarios</h3>
    <div class="table-wrap">
      <table>
        <thead><tr><th>ID</th><th>Nombre</th><th>Cedula</th><th>Correo</th><th>Rol</th><th>Acciones</th></tr></thead>
        <tbody>
          @for (u of users; track u.id) {
            <tr>
              <td>{{ u.id }}</td>
              <td>{{ u.name }}</td>
              <td>{{ u.cedula || '-' }}</td>
              <td>{{ u.email }}</td>
              <td>
                <select [ngModel]="u.role" (ngModelChange)="changeRole(u, $event)">
                  <option value="USER">USER</option>
                  <option value="ADMIN">ADMIN</option>
                </select>
              </td>
              <td>
                <button class="btn btn-danger" (click)="remove(u)" [disabled]="u.email === currentEmail">Eliminar</button>
              </td>
            </tr>
          }
        </tbody>
      </table>
    </div>
  </section>`
})
export class AdminUsersComponent implements OnInit {
  users: AdminUser[] = [];
  currentEmail = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.currentEmail = localStorage.getItem('email') ?? '';
    this.load();
  }

  load(): void {
    this.adminService.getUsers().subscribe((users) => this.users = users);
  }

  changeRole(user: AdminUser, role: string): void {
    if (role !== 'USER' && role !== 'ADMIN') return;
    this.adminService.updateUserRole(user.id, role).subscribe(() => this.load());
  }

  remove(user: AdminUser): void {
    if (!confirm(`Eliminar usuario ${user.email}?`)) return;
    this.adminService.deleteUser(user.id).subscribe(() => this.load());
  }
}
