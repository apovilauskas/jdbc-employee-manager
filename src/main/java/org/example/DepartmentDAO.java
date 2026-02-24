package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public void createDepartmentTable(){
        String query = "Create table if not exists departments (" +
                "id serial primary key, " +
                "name varchar(100) not null, " +
                "location varchar(100) not null);";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement statement = conn.createStatement()){
            statement.executeUpdate(query);
            System.out.println("Table 'departments' is ready.");
        }catch(SQLException e){
            System.err.println("CREATE ERROR: "+ e.getMessage());
        }
    }

    public void insertDepartment(Department department){
        String query = "Insert into departments(name, location) values(?,?)";
        try(Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1, department.getName());
            preparedStatement.setString(2, department.getLocation());
            preparedStatement.executeUpdate();
            System.out.println("Department inserted: "+department.getName());
        }catch(SQLException e){
            System.err.println("INSERT ERROR: "+ e.getMessage());
        }
    }

    public List<Department> getAllDepartments(){
        List<Department> departments = new ArrayList<>();
        String query = "select * from departments";

        try(Connection conn = DatabaseManager.getInstance().getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query))
        {
            while(rs.next()){
                Department dep = new Department();
                dep.setId(rs.getInt("id"));
                dep.setName(rs.getString("name"));
                dep.setLocation(rs.getString("location"));
                departments.add(dep);
            }
        }catch(Exception e){
            System.err.println("READ ERROR: "+e.getMessage());
        }
        return departments;
    }

    public void deleteDepartment(Department department){
        String query = "Delete from departments where id = ?";

        try(Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)){

            preparedStatement.setInt(1, department.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Department deleted");
            }else{
                System.out.println("No department found with id: "+department.getId());
            }
        }catch(Exception e){
            System.err.println("DELETE ERROR: "+e.getMessage());
        }
    }

    public void updateDepartment(Department department){
        String query = "update departments set name = ?, location = ? where id = ?";

        try(Connection conn = DatabaseManager.getInstance().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query)){

            preparedStatement.setString(1, department.getName());
            preparedStatement.setString(2, department.getLocation());
            preparedStatement.setInt(3, department.getId());

            int rowsAffected =preparedStatement.executeUpdate();
             preparedStatement.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Department with id "+ department.getId() + " updated.");
            }else{
                System.out.println("No department found with id: "+department.getId());
            }

        }catch(SQLException e){
            System.err.println("UPDATE ERROR: "+e.getMessage());
        }
    }

    public Department viewDepartmentById(Department department){
        String query = "select * from departments where id = ?";

        try(Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)){

            preparedStatement.setInt(1, department.getId());
            try(ResultSet rs = preparedStatement.executeQuery()){
                if(rs.next()){
                    Department dep = new Department();
                    dep.setId(rs.getInt("id"));
                    dep.setName(rs.getString("name"));
                    dep.setLocation(rs.getString("location"));
                    return dep;
                }
            }
        } catch(SQLException e){
            System.err.println("FIND ERROR: " + e.getMessage());
        }
        return null;
    }

}
