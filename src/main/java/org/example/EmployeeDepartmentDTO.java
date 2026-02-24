package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDepartmentDTO {
    private int id;
    private String name;
    private String email;
    private double salary;
    private String departmentName;
    private String location;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public double getSalary() { return salary; }
        public void setSalary(double salary) { this.salary = salary; }

        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location;}

    public List<EmployeeDepartmentDTO> getAllEmployeesWithDeptInfo() {
        List<EmployeeDepartmentDTO> list = new ArrayList<>();
        String query = "SELECT e.id, e.name, e.email, e.salary, d.name AS dept_name, d.location " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.id";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                EmployeeDepartmentDTO dto = new EmployeeDepartmentDTO();
                dto.setId(rs.getInt("id"));
                dto.setName(rs.getString("name"));
                dto.setEmail(rs.getString("email"));
                dto.setSalary(rs.getDouble("salary"));

                dto.setDepartmentName(rs.getString("dept_name"));
                dto.setLocation(rs.getString("location"));

                list.add(dto);
            }
        } catch (SQLException e) {
            System.err.println("JOIN ERROR: " + e.getMessage());
        }
        return list;
    }

}
