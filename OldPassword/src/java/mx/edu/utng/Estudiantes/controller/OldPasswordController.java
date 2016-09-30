package mx.edu.utng.Estudiantes.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.utng.Estudiantes.dao.OldPasswordDAOImp;
import mx.edu.utng.Estudiantes.model.OldPassword;
import mx.edu.utng.Estudiantes.dao.OldPasswordDAO;

public class OldPasswordController extends HttpServlet {
    private static final String LISTA_OLD_PASSWORD = "/lista_oldPassword.jsp";
    private static final String AGREGAR_O_CAMBIAR = "/oldPassword.jsp";
    private OldPasswordDAO dao ;
    
    public OldPasswordController(){
        dao = new OldPasswordDAOImp();
    }

@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("borrar")){
            forward = LISTA_OLD_PASSWORD; //va a cambiar, ya uqe borro los estudiantes con id especifico
            int idP_uuid = Integer.parseInt(
            request.getParameter("idP_uuid"));
            dao.borrarOldPassword(idP_uuid);
            request.setAttribute("oldPasswords", dao.desplegarOldPasswords()); //Listar nuevamente sin el valor borrado
        }else if (action.equalsIgnoreCase("cambiar")){
            forward = AGREGAR_O_CAMBIAR;
             int idP_uuid = Integer.parseInt(
             request.getParameter("idP_uuid"));
             OldPassword oldPassword = dao.elegirOldPassword(idP_uuid);
             request.setAttribute("oldPassword", oldPassword);
        }else if (action.equalsIgnoreCase("agregar")){
            forward = AGREGAR_O_CAMBIAR;
        }else{
            forward = LISTA_OLD_PASSWORD;
            request.setAttribute("oldPasswords", dao.desplegarOldPasswords());
        }
        
        RequestDispatcher view = request.getRequestDispatcher(forward); //Se va adeterminada vista o a otra
        view.forward(request, response); 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OldPassword oldPassword = new OldPassword();
        
        oldPassword.setPassword(request.getParameter("password"));
        oldPassword.setChanged_timestamp(Integer.parseInt(request.getParameter("changed_timestamp")));
        String idP_uuid = request.getParameter("idP_uuid");
        
        if(idP_uuid==null || idP_uuid.isEmpty()){
                dao.agregarOldPassword(oldPassword);
            }else{
            oldPassword.setIdP_uuid(Integer.parseInt(idP_uuid));
            dao.cambiarOldPassword(oldPassword);
        }
        RequestDispatcher view = request.getRequestDispatcher(LISTA_OLD_PASSWORD);
        request.setAttribute("oldPasswords", dao.desplegarOldPasswords());
        view.forward(request, response);
    }
}