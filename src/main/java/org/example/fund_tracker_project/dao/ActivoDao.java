package org.example.fund_tracker_project.dao;

import org.example.fund_tracker_project.database.DBConnection;
import org.example.fund_tracker_project.database.SchemDB;
import org.example.fund_tracker_project.model.Activo;
import org.example.fund_tracker_project.model.TipoActivo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivoDao {

    private Connection connection;

    public ActivoDao() {
        this.connection = DBConnection.getConnection();
    }

    public void actualizarPrecio() {

    }

    public int comprobarActivo(Activo activo) throws SQLException {
        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?",
                SchemDB.TAB_ACTIVO,
                SchemDB.COL_ACTIVO_TICKER
        );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, activo.getTicker());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);  // primera columna: COUNT(*)
                }
            }
        }

        return 0;
    }

    public void insertarActivo(Activo activo) throws SQLException {
        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE %s = ?, %s = ?, %s = ?",
                SchemDB.TAB_ACTIVO,
                SchemDB.COL_ACTIVO_NOMBRE,
                SchemDB.COL_ACTIVO_TIPO,
                SchemDB.COL_ACTIVO_GESTORA,
                SchemDB.COL_ACTIVO_ISIN,
                SchemDB.COL_ACTIVO_TICKER,
                SchemDB.COL_ACTIVO_NOMBRE,
                SchemDB.COL_ACTIVO_GESTORA,
                SchemDB.COL_ACTIVO_TICKER
        );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, activo.getNombre());
            ps.setString(2, activo.getTipoActivo().name());
            ps.setString(3, activo.getGestora());
            ps.setString(4, activo.getIsin());
            ps.setString(5, activo.getTicker());
            ps.setString(6, activo.getNombre());
            ps.setString(7, activo.getGestora());
            ps.setString(8, activo.getTicker());

            ps.executeUpdate();
        }
    }

    public List<Activo> obtenerTodos() throws SQLException {
        String query = String.format("SELECT * FROM %s ORDER BY %s",
                SchemDB.TAB_ACTIVO, SchemDB.COL_ACTIVO_TICKER);
        List<Activo> lista = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Activo activo = new Activo();
                activo.setNombre(rs.getString("nombre"));
                activo.setTipoActivo(TipoActivo.valueOf(rs.getString("tipo_activo")));
                activo.setGestora(rs.getString("gestora"));
                activo.setIsin(rs.getString("isin"));
                activo.setTicker(rs.getString("ticker"));
                activo.setIdActivo(rs.getLong("id_activo"));
                lista.add(activo);
            }
        }

        return lista;
    }
}
