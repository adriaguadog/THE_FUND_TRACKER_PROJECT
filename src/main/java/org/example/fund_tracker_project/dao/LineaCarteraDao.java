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
import java.util.ArrayList;
import java.util.List;

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

    public void actualizarLineaCartera(Operacion operacion) throws SQLException {
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
        String query = String.format("UPDATE %s SET %s = %s - ?, %s = %s - ? WHERE %s = ? AND %s = ?",
                SchemDB.TAB_LINEA_CARTERA,
                SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES,
                SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES,
                SchemDB.COL_LINEA_CARTERA_IMPORTE,
                SchemDB.COL_LINEA_CARTERA_IMPORTE,
                SchemDB.COL_LINEA_CARTERA_ID_USUARIO,
                SchemDB.COL_LINEA_CARTERA_ID_ACTIVO
        );
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setDouble(1, linea.getParticipaciones());
        ps.setDouble(2, linea.getImporte());
        ps.setLong(3, linea.getIdUsuario());
        ps.setLong(4, linea.getIdActivo());
        ps.executeUpdate();
    }

    public List<LineaCartera> obtenerLineasUsuario(Usuario usuario) throws SQLException {
        //LISTA PARA ALMACENAR LAS LINEAS
        List<LineaCartera> lineas = new ArrayList<>();

        //select id-activo, nombre,participaciones, importe from lineaCartera join activo on idusuario=usuario.getid and participaciones>0
        String query = String.format("SELECT a.%s, a.%s, lc.%s, lc.%s FROM %s lc JOIN %s a ON a.%s = lc.%s WHERE lc.%s = ? AND lc.%s > 0",
                SchemDB.COL_ACTIVO_ID,                   // a.id_activo
                SchemDB.COL_ACTIVO_NOMBRE,               // a.nombre
                SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES, // lc.participaciones
                SchemDB.COL_LINEA_CARTERA_IMPORTE,       // lc.importe
                SchemDB.TAB_LINEA_CARTERA,               // FROM linea_cartera lc
                SchemDB.TAB_ACTIVO,                      // JOIN activo a
                SchemDB.COL_ACTIVO_ID,                   // ON a.id_activo
                SchemDB.COL_LINEA_CARTERA_ID_ACTIVO,     // = lc.id_activo
                SchemDB.COL_LINEA_CARTERA_ID_USUARIO,    // WHERE lc.id_usuario = ?
                SchemDB.COL_LINEA_CARTERA_PARTICIPACIONES // AND lc.participaciones > 0
        );

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, usuario.getIdUsuario());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LineaCartera linea = new LineaCartera();
                    linea.setIdUsuario(usuario.getIdUsuario());
                    linea.setIdActivo(rs.getLong("id_activo"));
                    linea.setNombreActivo(rs.getString("nombre"));
                    linea.setParticipaciones(rs.getDouble("participaciones"));
                    linea.setImporte(rs.getDouble("importe"));
                    lineas.add(linea);
                }
            }
        }
        return lineas;
    }
}
