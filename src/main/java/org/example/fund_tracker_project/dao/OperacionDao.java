package org.example.fund_tracker_project.dao;

import org.example.fund_tracker_project.database.DBConnection;
import org.example.fund_tracker_project.database.SchemDB;
import org.example.fund_tracker_project.model.EstadoOperacion;
import org.example.fund_tracker_project.model.Operacion;
import org.example.fund_tracker_project.model.TipoOperacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperacionDao {

    private Connection connection;

    public OperacionDao() {
        this.connection = DBConnection.getConnection();
    }

    public void insertarOperacion(Operacion operacion) throws SQLException {
        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                SchemDB.TAB_OPERACION,
                SchemDB.COL_OPERACION_ID_USUARIO,
                SchemDB.COL_OPERACION_ID_ACTIVO,
                SchemDB.COL_OPERACION_TIPO,
                SchemDB.COL_OPERACION_FECHA,
                SchemDB.COL_OPERACION_PARTICIPACIONES,
                SchemDB.COL_OPERACION_PRECIO_UNITARIO,
                SchemDB.COL_OPERACION_CANTIDAD,
                SchemDB.COL_OPERACION_RENTABILIDAD,
                SchemDB.COL_OPERACION_ESTADO

        );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, operacion.getIdUsuario());
            ps.setLong(2, operacion.getIdActivo());
            ps.setString(3, operacion.getTipoOperacion().name());
            ps.setDate(4, Date.valueOf(operacion.getFecha()));
            ps.setDouble(5, operacion.getParticipaciones());
            ps.setDouble(6, operacion.getPrecioUnitario());
            ps.setDouble(7, operacion.getImporte());
            ps.setDouble(8, operacion.getRentabilidad());
            ps.setString(9, operacion.getEstadoOperacion().name());

            ps.executeUpdate();
        }
    }

    public void cancelarOperacion(Operacion operacion) throws SQLException {
        String query = String.format(
                "UPDATE %s SET %s = ? WHERE %s = ? ",
                SchemDB.TAB_OPERACION,
                SchemDB.COL_OPERACION_ESTADO,
                SchemDB.COL_OPERACION_ID
        );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, EstadoOperacion.CANCELADA.name());
            ps.setLong(2, operacion.getIdOperacion());

            ps.executeUpdate();
        }
    }

    public void modificarOperacion(Operacion operacion) throws SQLException {// no modifica estado ni usuario ni id operacion
        String query = String.format(
                "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
                SchemDB.TAB_OPERACION,
                SchemDB.COL_OPERACION_TIPO,
                SchemDB.COL_OPERACION_FECHA,
                SchemDB.COL_OPERACION_PARTICIPACIONES,
                SchemDB.COL_OPERACION_PRECIO_UNITARIO,
                SchemDB.COL_OPERACION_CANTIDAD,
                SchemDB.COL_OPERACION_RENTABILIDAD,
                SchemDB.COL_OPERACION_ID
        );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, operacion.getTipoOperacion().name());
            ps.setDate(2, Date.valueOf(operacion.getFecha()));
            ps.setDouble(3, operacion.getParticipaciones());
            ps.setDouble(4, operacion.getPrecioUnitario());
            ps.setDouble(5, operacion.getImporte());
            ps.setDouble(6, operacion.getRentabilidad());
            ps.setLong(7, operacion.getIdOperacion());
            ps.executeUpdate();
        }
    }

    public double obtenerParticipacionesEjecutadas(long idUsuario, long idActivo) throws SQLException {
        String query = String.format(
                "SELECT COALESCE(SUM(CASE WHEN %s = 'VENTA' THEN -%s ELSE %s END), 0) AS total " +
                        "FROM %s WHERE %s = ? AND %s = ? AND %s = 'EJECUTADA'", //coalesce por si el resultado es null
                SchemDB.COL_OPERACION_TIPO,
                SchemDB.COL_OPERACION_PARTICIPACIONES,
                SchemDB.COL_OPERACION_PARTICIPACIONES,
                SchemDB.TAB_OPERACION,
                SchemDB.COL_OPERACION_ID_USUARIO,
                SchemDB.COL_OPERACION_ID_ACTIVO,
                SchemDB.COL_OPERACION_ESTADO
        );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, idUsuario);
            ps.setLong(2, idActivo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0;
    }

    //obtener historico de operaciones con id_operacion asignado
    public List<Operacion> obtenerHistoricoUsuario(long idUsuario) throws SQLException {
        List<Operacion> operaciones = new ArrayList<>();

        String query = String.format(
                "SELECT * FROM %s WHERE %s = ? ORDER BY %s DESC",
                SchemDB.TAB_OPERACION,
                SchemDB.COL_OPERACION_ID_USUARIO,
                SchemDB.COL_OPERACION_FECHA
        );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Operacion operacion = new Operacion();
                    operacion.setIdOperacion(rs.getLong(SchemDB.COL_OPERACION_ID));
                    operacion.setTipoOperacion(
                            TipoOperacion.valueOf(rs.getString(SchemDB.COL_OPERACION_TIPO)));
                    operacion.setImporte(rs.getDouble(SchemDB.COL_OPERACION_CANTIDAD));
                    operacion.setRentabilidad(rs.getDouble(SchemDB.COL_OPERACION_RENTABILIDAD));
                    operacion.setFecha(rs.getDate(SchemDB.COL_OPERACION_FECHA).toLocalDate());
                    operacion.setParticipaciones(rs.getDouble(SchemDB.COL_OPERACION_PARTICIPACIONES));
                    operacion.setPrecioUnitario(rs.getDouble(SchemDB.COL_OPERACION_PRECIO_UNITARIO));
                    operacion.setIdUsuario(rs.getLong(SchemDB.COL_OPERACION_ID_USUARIO));
                    operacion.setIdActivo(rs.getLong(SchemDB.COL_OPERACION_ID_ACTIVO));
                    operacion.setEstadoOperacion(EstadoOperacion.valueOf(rs.getString(SchemDB.COL_OPERACION_ESTADO)));
                    operaciones.add(operacion);
                }
            }
        }
        return operaciones;
    }
}