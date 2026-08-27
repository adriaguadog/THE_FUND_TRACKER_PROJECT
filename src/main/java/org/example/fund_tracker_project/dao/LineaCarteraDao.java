package org.example.fund_tracker_project.dao;

import org.example.fund_tracker_project.database.DBConnection;
import org.example.fund_tracker_project.database.SchemDB;
import org.example.fund_tracker_project.model.LineaCartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

}
