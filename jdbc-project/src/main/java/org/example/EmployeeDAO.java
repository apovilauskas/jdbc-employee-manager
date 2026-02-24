package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public void createEmployeeTable() {
        String query = "CREATE TABLE IF NOT EXISTS employees (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(150) UNIQUE NOT NULL, " +
                "salary NUMERIC(10,2) NOT NULL, " +
                "department_id INTEGER NOT NULL, " +
                "FOREIGN KEY (department_id) REFERENCES departments(id));";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Table 'employees' is ready.");
        } catch (SQLException e) {
            System.err.println("CREATE ERROR: " + e.getMessage());
        }
    }

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

    public void insertEmployee(Employee employee) {
        String query = "INSERT INTO employees (name, email, salary, department_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setDouble(3, employee.getSalary());
            pstmt.setInt(4, employee.getDepartmentId());

            pstmt.executeUpdate();
            System.out.println("Employee inserted: " + employee.getName());
        } catch (SQLException e) {
            System.err.println("INSERT ERROR: " + e.getMessage());
        }
    }

    public void deleteEmployee(Employee employee) {
        String query = "DELETE FROM employees WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, employee.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee deleted");
            } else {
                System.out.println("No employee found with id: " + employee.getId());
            }
        } catch (SQLException e) {
            System.err.println("DELETE ERROR: " + e.getMessage());
        }
    }

    public void updateEmployee(Employee employee) {
        String query = "UPDATE employees SET name = ?, email = ?, salary = ?, department_id = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setDouble(3, employee.getSalary());
            pstmt.setInt(4, employee.getDepartmentId());
            pstmt.setInt(5, employee.getId());

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected >0){
                System.out.println("Employee with ID " + employee.getId() + " updated.");
            }else{
                System.out.println("Id not found");
            }

        } catch (SQLException e) {
            if(e.getSQLState().startsWith("23")){
                System.out.println("Department not found");
            }else{
                System.err.println("UPDATE ERROR: " + e.getMessage());
            }

        }
    }

    public Employee findById(Employee employee) {
        String query = "SELECT * FROM employees WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, employee.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("id"));
                    emp.setName(rs.getString("name"));
                    emp.setEmail(rs.getString("email"));
                    emp.setSalary(rs.getDouble("salary"));
                    emp.setDepartmentId(rs.getInt("department_id"));
                    return emp;
                }
            }
        } catch (SQLException e) {
            System.err.println("FIND ERROR: " + e.getMessage());
        }
        return null;
    }

    public Employee findByEmail(Employee employee) {
        String query = "SELECT * FROM employees WHERE email = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, employee.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("id"));
                    emp.setName(rs.getString("name"));
                    emp.setEmail(rs.getString("email"));
                    emp.setSalary(rs.getDouble("salary"));
                    emp.setDepartmentId(rs.getInt("department_id"));
                    return emp;
                }
            }
        } catch (SQLException e) {
            System.err.println("FIND ERROR: " + e.getMessage());
        }
        return null;
    }

    public void transferAndPromote(int empId, int newDeptId, double newSalary) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            String sql1 = "UPDATE employees SET department_id = ?  WHERE id = ?";
             PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, newDeptId);
            ps1.setInt(2, empId);
            int affectedRows = ps1.executeUpdate();
            if(affectedRows ==0 ){
                throw new SQLException("Employee ID " + empId + " not found.");
            }

            String sql2 = "UPDATE employees SET salary = ? WHERE id = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setDouble(1, newSalary);
            ps2.setInt(2, empId);
            ps2.executeUpdate();

            conn.commit();
            System.out.println("Transaction Successful!");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Error! Transaction rolled back: "+e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }


}