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
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "%s = ?, %s = ?, %s = ?, %s = ?, %s = ?",
                SchemDB.TAB_ACTIVO,
                SchemDB.COL_ACTIVO_NOMBRE,
                SchemDB.COL_ACTIVO_TIPO,
                SchemDB.COL_ACTIVO_GESTORA,
                SchemDB.COL_ACTIVO_TICKER,
                SchemDB.COL_ACTIVO_EXCHANGE,
                SchemDB.COL_ACTIVO_MIC_CODE,
                SchemDB.COL_ACTIVO_TICKER_YAHOO,
                SchemDB.COL_ACTIVO_NOMBRE,
                SchemDB.COL_ACTIVO_GESTORA,
                SchemDB.COL_ACTIVO_MIC_CODE,
                SchemDB.COL_ACTIVO_TICKER_YAHOO,
                SchemDB.COL_ACTIVO_EXCHANGE
                );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, activo.getNombre());
            ps.setString(2, activo.getTipoActivo().name());
            ps.setString(3, activo.getGestora());
            ps.setString(4, activo.getTicker());
            ps.setString(5, activo.getExchange());
            ps.setString(6, activo.getMicCode());
            ps.setString(7, activo.getTickerYahoo());

            ps.setString(8, activo.getNombre());
            ps.setString(9, activo.getGestora());
            ps.setString(10, activo.getMicCode());
            ps.setString(11, activo.getTickerYahoo());
            ps.setString(12, activo.getExchange());


            ps.executeUpdate();
        }
    }

    public List<Activo> obtenerTodos() throws SQLException {
        String query = String.format(
                "SELECT * FROM %s ORDER BY %s",
                SchemDB.TAB_ACTIVO,
                SchemDB.COL_ACTIVO_TICKER
        );

        List<Activo> lista = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Activo activo = new Activo();
                activo.setIdActivo(rs.getLong(SchemDB.COL_ACTIVO_ID));
                activo.setNombre(rs.getString(SchemDB.COL_ACTIVO_NOMBRE));
                activo.setTipoActivo(TipoActivo.valueOf(rs.getString(SchemDB.COL_ACTIVO_TIPO))
                );
                activo.setGestora(rs.getString(SchemDB.COL_ACTIVO_GESTORA));
                activo.setTicker(rs.getString(SchemDB.COL_ACTIVO_TICKER));
                activo.setExchange(rs.getString(SchemDB.COL_ACTIVO_EXCHANGE));
                activo.setMicCode(rs.getString(SchemDB.COL_ACTIVO_MIC_CODE));
                activo.setTickerYahoo(rs.getString(SchemDB.COL_ACTIVO_TICKER_YAHOO)
                );
                lista.add(activo);
            }
        }

        return lista;
    }
}
