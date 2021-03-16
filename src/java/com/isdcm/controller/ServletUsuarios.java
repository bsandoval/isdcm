/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isdcm.controller;

import com.isdcm.dao.UsuarioDAO;
import com.isdcm.dto.UsuarioDTO;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author fiblabs
 */
@WebServlet(urlPatterns = "/")
public class ServletUsuarios extends HttpServlet {
    private final UsuarioDAO usuarioDAO;

    public ServletUsuarios() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Entra a doGet : ");
        HttpSession session = request.getSession(false);
        if (session != null) { 
            RequestDispatcher dispatcher = request.getRequestDispatcher("video.jsp");
            dispatcher.forward(request, response);
        }else{
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        System.out.println("Entra a doPost con action: "+action);
        try {
            switch (action) {
            case "/login":
                    login(request, response);
                    break;
            case "/logout":
                    logout(request, response);
                    break;
            case "/signup":
                    signUp(request, response);
                    break;
            case "/user":
                    createUser(request, response);
                    break;
            default:
                    redirectToLogin(request, response);
                    break;
            }
        } catch (SQLException ex) {
                throw new ServletException(ex);
        }
    }
    
    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response)
                    throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
        dispatcher.forward(request, response);
    }
    
    private void login(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException, ServletException{
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            UsuarioDTO user = usuarioDAO.checkLogin(username, password);
            String destPage = "login.jsp";
             
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                destPage = "video.jsp";
            } else {
                request.setAttribute("errorMsg", "Invalid email/password");
            }
             
            RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
            dispatcher.forward(request, response);
             
        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void logout(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException, ServletException{
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("user");
             
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    private void signUp(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException, ServletException{
        RequestDispatcher dispatcher = request.getRequestDispatcher("signup.jsp");
        dispatcher.forward(request, response);
    }
    
    private void createUser(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException, ServletException{
        try {
            String destPage = "signup.jsp";
            
            String password = request.getParameter("password");
            String passwordConfirmation = request.getParameter("passwordConfirmation");
            if(!checkPasswordConfirmation(password, passwordConfirmation)){
                request.setAttribute("errorMsg", "Contraseñas no coinciden");
            }
            // Se comprueba si existe el usuario en la base de datos
            else if(usuarioDAO.checkUserExists(request.getParameter("username"))){
                request.setAttribute("errorMsg", "El usuario ya existe");
            }
            else{
                String nombre = request.getParameter("nombre");
                String apellido = request.getParameter("apellido");
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                
                UsuarioDTO usuario = new UsuarioDTO(nombre, apellido, email, username, password);
                boolean inserted = usuarioDAO.insertUsuario(usuario);
            
                if (inserted) {
                    destPage = "login.jsp";
                    request.setAttribute("successMsg", "Usuario registrado correctamente! Ahora puede conectarse"); //register success messeage
                } else {
                    request.setAttribute("errorMsg", "Ocurrió un error al registrar usuario");
                }
            }
             
            RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
            dispatcher.forward(request, response);
             
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    private boolean checkPasswordConfirmation(String pass, String passConfirm){
        return pass.equals(passConfirm);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
