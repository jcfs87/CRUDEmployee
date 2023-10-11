package com.company.dao;

import com.company.jdbc.utilities.ConnectionDB;
import com.company.model.Employee;

import java.sql.*;
import java.util.ArrayList;

public class DAOEmployee {
    Connection con = null;

    public DAOEmployee() {
        try {
            con = ConnectionDB.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<Employee> getALLEmployees() throws SQLException {
        try (Statement stmt = con.createStatement()) {
            ArrayList<Employee> emps = new ArrayList<>();
            String query = "SELECT * FROM employee";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int empID = rs.getInt("id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                Date birthDate = rs.getDate("birthdate");
                float salary = rs.getFloat("salary");
                emps.add(new Employee(empID, firstName, lastName, birthDate, salary));
            }
            return emps;
        }
    }

    public void deleteEmployee(int identificador) throws Exception {
        try (PreparedStatement stmt = con.prepareStatement("DELETE FROM employee where id = ?")) {
            stmt.setInt(1, identificador);
            stmt.execute();
        } catch (SQLException e) {
            throw new Exception("Failed to delete a employee record");
        }
    }

    public void addEmployee(Employee nouEmployee) {
        String queryInsert = "INSERT INTO employee(firstname,lastname,birthdate,salary) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(queryInsert)) {
            stmt.setString(1, nouEmployee.getFirstName());
            stmt.setString(2, nouEmployee.getLastName());
            stmt.setDate(3, new java.sql.Date(nouEmployee.getBirthDate().getTime()));
            stmt.setDouble(4, nouEmployee.getSalary());stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Employee getEmployee(int identificador) throws Exception {
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM employee where id = ?")) {
            stmt.setInt(1, identificador);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int empID = rs.getInt("id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                Date birthDate = rs.getDate("birthdate");
                float salary = rs.getFloat("salary");
                return new Employee(empID, firstName, lastName, birthDate, salary);
            }
            return null;
        } catch (SQLException e) {
            throw new Exception("Failed to retrive a employee record");
        }
    }

    public void updateEmployee(Employee employeeActualitzar, int employeeid) {
        String queryInsert = "UPDATE employee SET firstname = ?, lastname= ?, salary = ? WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(queryInsert)) {
            stmt.setString(1, employeeActualitzar.getFirstName());
            stmt.setString(2, employeeActualitzar.getLastName());
            stmt.setDouble(3, employeeActualitzar.getSalary());
            stmt.setInt(4, employeeid);
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

   public Employee goToFirst() throws Exception {
        String querySelect = "SELECT * from employee order by id LIMIT 1;";
        try (PreparedStatement stmt= con.prepareStatement(querySelect)) {
            ResultSet rs = stmt.executeQuery(querySelect);
            if (rs.next()) {
                int empID = rs.getInt("id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                Date birthDate = rs.getDate("birthdate");
                float salary = rs.getFloat("salary");
               return new Employee(empID,firstName,lastName,birthDate,salary);

            }
            return null;
        } catch (SQLException e ){
         throw new Exception("Failed");
        }

    }



}
