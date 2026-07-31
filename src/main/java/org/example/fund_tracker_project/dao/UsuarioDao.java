package org.example.fund_tracker_project.dao;

import org.example.fund_tracker_project.database.DBConnection;
import org.example.fund_tracker_project.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.example.fund_tracker_project.database.SchemDB.*;


public class UsuarioDao {

    private Connection connection;

    public UsuarioDao(){
        connection=DBConnection.getConnection();
    }

    public int darDeAlta(Usuario usuario){
        String query= String.format(
                "INSERT INTO %s (%s, %s,%s,%s,%s) VALUES (?,?,?,?,?) ",
                TAB_USUARIOS,
                COL_USUARIO_NOMBRE,
                COL_USUARIO_APELLIDOS,
                COL_USUARIO_DNI,
                COL_USUARIO_CONTRASENHA,
                COL_USUARIO_EMAIL
        );

        try (PreparedStatement preparedStatement=connection.prepareStatement(query)){

            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getApellidos());
            preparedStatement.setString(3, usuario.getDni());
            preparedStatement.setString(4, usuario.getContrasenha());
            preparedStatement.setString(5, usuario.getEmail());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
    }

    public int modificarDatos(Usuario usuario){
            String query=String.format(
                    "UPDATE %s SET %s= ?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
                    TAB_USUARIOS,
                    COL_USUARIO_NOMBRE,
                    COL_USUARIO_APELLIDOS,
                    COL_USUARIO_DNI,
                    COL_USUARIO_CONTRASENHA,
                    COL_USUARIO_EMAIL,
                    COL_USUARIO_DNI
            );

            try (PreparedStatement preparedStatement=connection.prepareStatement(query)){

                preparedStatement.setString(1,usuario.getNombre());
                preparedStatement.setString(2,usuario.getApellidos());
                preparedStatement.setString(3,usuario.getDni());
                preparedStatement.setString(4,usuario.getContrasenha());
                preparedStatement.setString(5,usuario.getEmail());
                preparedStatement.setString(6, usuario.getDni());

                return preparedStatement.executeUpdate();

            } catch (SQLException e) {
                return -1;
            }
    }

    public int darDeBaja(Usuario usuario) {
        if (usuario != null) {
            String query = String.format(
                    "DELETE FROM %s WHERE %s=?",
                    TAB_USUARIOS,
                    COL_USUARIO_DNI
            );

            //uso try with resources para no tener que cerrar resultset y preparedStatement
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1, usuario.getDni());
                return preparedStatement.executeUpdate();

            } catch (SQLException e) {
                return -1;
            }
        }
        return -1;
    }

    public Usuario buscarPorDni(String dniIntroducido) throws SQLException {
        String query=String.format("SELECT * FROM %s WHERE %s= ?",
                TAB_USUARIOS,
                COL_USUARIO_DNI
        );

        try (PreparedStatement preparedStatement=connection.prepareStatement(query)){

            preparedStatement.setString(1, dniIntroducido);
            try(ResultSet resultSet= preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(resultSet.getLong(COL_USUARIO_ID));
                    usuario.setNombre(resultSet.getString(COL_USUARIO_NOMBRE));
                    usuario.setApellidos(resultSet.getString(COL_USUARIO_APELLIDOS));
                    usuario.setDni(resultSet.getString(COL_USUARIO_DNI));
                    usuario.setContrasenha(resultSet.getString(COL_USUARIO_CONTRASENHA));
                    usuario.setEmail(resultSet.getString(COL_USUARIO_EMAIL));

                    return usuario;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error de conexion con la base de datos",e);
        }
        return null;
    }

    public Usuario buscarPorEmail(String emailIntroducido) throws SQLException {
        String query=String.format("SELECT * FROM %s WHERE %s= ?",
                TAB_USUARIOS,
                COL_USUARIO_EMAIL
        );

        try (PreparedStatement preparedStatement=connection.prepareStatement(query)){

            preparedStatement.setString(1, emailIntroducido);
            try(ResultSet resultSet= preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(resultSet.getLong(COL_USUARIO_ID));
                    usuario.setNombre(resultSet.getString(COL_USUARIO_NOMBRE));
                    usuario.setApellidos(resultSet.getString(COL_USUARIO_APELLIDOS));
                    usuario.setDni(resultSet.getString(COL_USUARIO_DNI));
                    usuario.setContrasenha(resultSet.getString(COL_USUARIO_CONTRASENHA));
                    usuario.setEmail(resultSet.getString(COL_USUARIO_EMAIL));

                    return usuario;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error de conexion con la base de datos",e);
        }
        return null;
    }

    public ArrayList<Usuario> listarTodos() throws SQLException {
        ArrayList <Usuario> listaUsuarios= new ArrayList<>();
        String query=String.format("SELECT * FROM %s ",
                TAB_USUARIOS
        );
        try (PreparedStatement preparedStatement= connection.prepareStatement(query)){
            try (ResultSet resultSet= preparedStatement.executeQuery()){

            while (resultSet.next()){
                Usuario usuario=new Usuario();
                usuario.setIdUsuario(resultSet.getLong(COL_USUARIO_ID));
                usuario.setNombre(resultSet.getString(COL_USUARIO_NOMBRE));
                usuario.setApellidos(resultSet.getString(COL_USUARIO_APELLIDOS));
                usuario.setDni(resultSet.getString(COL_USUARIO_DNI));
                usuario.setContrasenha(resultSet.getString(COL_USUARIO_CONTRASENHA));
                usuario.setEmail(resultSet.getString(COL_USUARIO_EMAIL));

                listaUsuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new SQLException("Error de conexion con la base de datos",e);
        }
        }
    return listaUsuarios;
    }

    public Usuario hacerLogin(String email, String contrasenha) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE %s= ? AND %s= ?",
                TAB_USUARIOS,
                COL_USUARIO_EMAIL,
                COL_USUARIO_CONTRASENHA);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, contrasenha);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setNombre(resultSet.getString(COL_USUARIO_NOMBRE));
                    usuario.setDni(resultSet.getString(COL_USUARIO_DNI));
                    usuario.setApellidos(resultSet.getString(COL_USUARIO_APELLIDOS));
                    usuario.setEmail(resultSet.getString(COL_USUARIO_EMAIL));
                    usuario.setContrasenha(resultSet.getString(COL_USUARIO_CONTRASENHA));
                    usuario.setIdUsuario(resultSet.getLong(COL_USUARIO_ID));

                    return usuario;
                }
            } catch (SQLException e) {
                throw new SQLException("Error de conexion a la base de datos", e);
            }
            return null;
        }
    }
}
