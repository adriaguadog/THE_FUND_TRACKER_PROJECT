package org.example.fund_tracker_project.dao;

import org.example.fund_tracker_project.database.DBConnection;
import org.example.fund_tracker_project.database.SchemDB;
import org.example.fund_tracker_project.model.Activo;
import org.example.fund_tracker_project.model.LineaCartera;
import org.example.fund_tracker_project.model.Operacion;
import org.example.fund_tracker_project.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LineaCarteraDao {

    private Connection connection;

    public LineaCarteraDao() {
        this.connection = DBConnection.getConnection();
    }

    public void insertarLinea(LineaCartera linea) throws SQLException {
        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE %s = %s + ?, %s = %s + ?", //para suma acumulativa
                SchemDB.TAB_LINEA_CARTERA,
                SchemDB.COL_LINEA_CARTERA_ID_USUARIO,
                SchemDB.COL_LINEA_CARTERA_ID_ACTIVO,
                SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES,
                SchemDB.COL_LINEA_CARTERA_IMPORTE,
                SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES, SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES,
                SchemDB.COL_LINEA_CARTERA_IMPORTE, SchemDB.COL_LINEA_CARTERA_IMPORTE
        );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, linea.getIdUsuario());
            ps.setLong(2, linea.getIdActivo());
            ps.setDouble(3, linea.getParticipaciones());
            ps.setDouble(4, linea.getImporte());
            ps.setDouble(5, linea.getParticipaciones());
            ps.setDouble(6, linea.getImporte());
            ps.executeUpdate();
        }
    }

    public void actualizarLineaCartera(Operacion operacion) throws SQLException{
        String query = String.format(
                "UPDATE %s SET %s = %s - ?, %s = %s - ? WHERE %s = ? AND %s = ?",
                SchemDB.TAB_LINEA_CARTERA,
                SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES, SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES,
                SchemDB.COL_LINEA_CARTERA_IMPORTE, SchemDB.COL_LINEA_CARTERA_IMPORTE,
                SchemDB.COL_LINEA_CARTERA_ID_USUARIO,
                SchemDB.COL_LINEA_CARTERA_ID_ACTIVO
        );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, operacion.getParticipaciones());
            ps.setDouble(2, operacion.getImporte());
            ps.setLong(3, operacion.getIdUsuario());
            ps.setLong(4, operacion.getIdActivo());
            ps.executeUpdate();
        }
        }

    public void restarLinea(LineaCartera linea) throws SQLException {
        String query=String. format("UPDATE %s SET %s = %s - ?, %s = %s - ? WHERE %s = ? AND %s = ?",
                SchemDB.TAB_LINEA_CARTERA,
                SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES,
                SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES,
                SchemDB.COL_LINEA_CARTERA_IMPORTE,
                SchemDB.COL_LINEA_CARTERA_IMPORTE,
                SchemDB.COL_LINEA_CARTERA_ID_USUARIO,
                SchemDB.COL_LINEA_CARTERA_ID_ACTIVO
                );
        PreparedStatement ps= connection.prepareStatement(query);
        ps.setDouble(1, linea.getParticipaciones());
        ps.setDouble(2, linea.getImporte());
        ps.setLong(3, linea.getIdUsuario());
        ps.setLong(4, linea.getIdActivo());
        ps.executeUpdate();
    }
    }
