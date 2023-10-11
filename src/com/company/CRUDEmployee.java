package com.company;

import com.company.dao.DAOEmployee;
import com.company.jdbc.utilities.ConnectionDB;
import com.company.model.Employee;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.sql.*;

public class CRUDEmployee {


    public static void main(String[] args) {
        boolean timeToQuit = false;
        try (Scanner in = new Scanner(System.in)) {
            do {
                try {
                    timeToQuit = executeMenu(in);
                } catch (Exception e) {
                    System.out.println("Error " + e.getClass().getName());
                    System.out.println("Message: " + e.getMessage());
                }
            } while (timeToQuit);
        } catch (Exception e) {
            System.out.println("Message: " + e.getMessage());
        }
    }

    public static boolean executeMenu(Scanner in) throws Exception {
        String action;
        int id;
        boolean resultat = true;
        System.out.println("\n\n[C]reate | [R]ead | [U]pdate | [D]elete | [L]ist | [Q]uit: ");
        action = in.nextLine();
        if ((action.length() == 0) || action.toUpperCase().charAt(0) == 'Q') {
            return true;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        DAOEmployee dao = new DAOEmployee();
        try (Statement stmt = ConnectionDB.getInstance().createStatement()){
            switch (action.toUpperCase().charAt(0)) {
                case 'C':
                    //addEmployee(dao,in);
                    irALprimerEmpleado(dao);
                    break;
                case 'R':
                    displayEmployeeRecord(dao);
                    break;
                case 'U':
                    updateEmployee(dao,in);
                    break;
                case 'D':
                    deleteEmployee(dao);
                    break;
                case 'L':
                    displayAllEmployees(dao);
                    break;
                case 'Q':
                    System.out.println("Hs escollit sortir");
                    resultat = false;
                    break;
            }
        }catch (MySQLIntegrityConstraintViolationException e){
            System.out.println("ERROR: Recorrd id already existing");
        } catch (SQLException e) {
            System.out.println(e.getClass().getName());
            e.printStackTrace();
        }
        return resultat;
    }

    private static void displayAllEmployees(DAOEmployee dao) throws SQLException {
        ArrayList<Employee> emps= dao.getALLEmployees();
        for(Employee emp : emps){
            System.out.println(emp);
        }
    }

    private static void deleteEmployee(DAOEmployee dao) throws SQLException {
        System.out.println("Escriu el id del empleat que vols borrar: ");
        int identificador = llegirEnter("Escriu el id del empleat que vols borrar: ",0,Integer.MAX_VALUE);
        try {
            dao.deleteEmployee(identificador);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void updateEmployee(DAOEmployee dao, Scanner in) throws Exception {
        int employeeid = llegirEnter("Posa el id del empleat que vols updatejar: ",0,Integer.MAX_VALUE);
        Employee employeeActualitzar = dao.getEmployee(employeeid);
        if(employeeActualitzar != null) {
            System.out.println(employeeActualitzar);
            int opcio = llegirEnter("Que vols canviar: el nom[1], el cognom[2] o el salari[3]", 1, 3);
            switch (opcio) {
                case 1:
                    System.out.println("Escriu el nou nom: ");
                    String nombre = in.nextLine();
                    employeeActualitzar.setFirstName(nombre);
                    dao.updateEmployee(employeeActualitzar,employeeid);
                    break;
                case 2:
                    System.out.println("Escriu el nou cognom: ");
                    String cognom = in.nextLine();
                    employeeActualitzar.setLastName(cognom);
                    dao.updateEmployee(employeeActualitzar,employeeid);
                    break;
                case 3:
                    int diners = llegirEnter("Escriu el nou salari: ", 0, Integer.MAX_VALUE);
                    employeeActualitzar.setSalary(diners);
                    dao.updateEmployee(employeeActualitzar,employeeid);
                    break;
                default:
                    System.out.println("La opció triada no existeix");
            }
            System.out.println("Actualitzat");
            System.out.println(employeeActualitzar);
        }else{
            System.out.println("No data found");
        }
    }

    private static void displayEmployeeRecord(DAOEmployee dao) throws Exception {
        int pk = llegirEnter("Escriu el id d'el empleat el qual vols que s'et mostri: ",0,Integer.MAX_VALUE);
        Employee employeeMostrar = dao.getEmployee(pk);
        if(employeeMostrar != null) {
            System.out.println(employeeMostrar);
        }
    }

    private static void irALprimerEmpleado(DAOEmployee dao) throws Exception {
      //  int pk= llegirEnter("Escriu el id d'el empleat el qual vols que s'et mostri: ",0,Integer.MAX_VALUE);
        Employee employee= dao.goToFirst();
        if(employee !=null){
            System.out.println(employee);
        }
    }

    private static void addEmployee(DAOEmployee dao,Scanner in) throws SQLException {
        System.out.println("Posa el firstname: ");
        String name = in.nextLine();
        System.out.println("Posa el lastname: ");
        String lastname = in.nextLine();
        int any = llegirEnter("Posa el any en el que va neixer: ",Calendar.getInstance().get(Calendar.YEAR)-100, Calendar.getInstance().get(Calendar.YEAR));
        int mes = llegirEnter("Posa el mes en el que va neixer: ",1,12);
        int dia = llegirEnter("Posa el dia en el que va neixer: ",1,31);
        int salari = llegirEnter("Posa el salari: ",0,100000);
        Calendar c = Calendar.getInstance();
        c.set(any,mes-1,dia,0,0);
        java.util.Date birthdate = c.getTime();
        Employee nouEmployee = new Employee(0,name,lastname,birthdate,salari);
        dao.addEmployee(nouEmployee);
    }

    /**
     * Aquest mètode serveix per llegir un enter de teclat amb control d'errors.
     * @param menuPrincipal: Serveix per passar-li el text a mostrar del menú.
     * @param min: Valor mínim acceptat
     * @param max: Valor màxim acceptat
     * @return : retorna un enter dins del domini de valors
     */
    public static int llegirEnter(String menuPrincipal, int min, int max) {
        Scanner llegir = new Scanner(System.in);
        int opcio = 0;
        boolean valorCorrecte = false;

        do {
            System.out.println(menuPrincipal);

            valorCorrecte = llegir.hasNextInt();

            if (!valorCorrecte){
                System.out.println("ERROR: No has introduït un enter");
                llegir.nextLine();
            }else{
                opcio = llegir.nextInt();
                llegir.nextLine();

                if (opcio < min || opcio > max){
                    System.out.println("ERROR: Opció no correcte");
                    valorCorrecte = false;
                }
            }

        }while(!valorCorrecte);

        // llegir.close(); // És necessari per alliberar memòria RAM.
        return opcio;
    }
}