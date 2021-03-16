/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isdcm.dao;

import com.isdcm.dto.UsuarioDTO;
import com.isdcm.jdbc.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author fiblabs
 */
public class UsuarioDAO {
    
    public UsuarioDTO checkLogin(String username, String password) throws SQLException,
            ClassNotFoundException {
        
        DataSource ds = DataSource.getInstance();
        String sql = "SELECT * FROM MYDB.USUARIO WHERE USERNAME = ? AND PASSWORD = ?";
        PreparedStatement statement = ds.getConnection().prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);
 
        ResultSet result = statement.executeQuery();
        UsuarioDTO user = null;
 
        if (result.next()) {
            user = new UsuarioDTO();
            user.setNombre(result.getString("nombre"));
            user.setApellido(result.getString("apellido"));
            user.setEmail(result.getString("email"));
            user.setUsername(result.getString("username"));
        }
 
        ds.getConnection().close();
        return user;
    }
    
    public boolean checkUserExists(String username){
        try{
            DataSource ds = DataSource.getInstance();
            String sql = "SELECT COUNT(1) FROM MYDB.USUARIO WHERE USERNAME = ?";
            PreparedStatement statement = ds.getConnection().prepareStatement(sql);
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) return true;

            ds.getConnection().close();
        }
        catch(SQLException e){ System.out.println(e);}
        return false;
    }
    
    public boolean insertUsuario(UsuarioDTO usuario) throws SQLException{
        DataSource ds = DataSource.getInstance();
        String insertNewUserSQL = "INSERT INTO MYDB.USUARIO (NOMBRE, APELLIDO, EMAIL, USERNAME, PASSWORD) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = ds.getConnection().prepareStatement(insertNewUserSQL);

        pstmt.setString(1, usuario.getNombre());
        pstmt.setString(2, usuario.getApellido());
        pstmt.setString(3, usuario.getEmail());
        pstmt.setString(4, usuario.getUsername());
        //a nice feature would be to encrypt user's password
        pstmt.setString(5, usuario.getPassword());
        boolean rowInserted = pstmt.executeUpdate() > 0;
        ds.getConnection().close();
        return rowInserted;
    }
}
