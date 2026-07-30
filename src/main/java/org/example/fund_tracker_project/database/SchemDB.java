package org.example.fund_tracker_project.database;

public interface SchemDB {

        // TABLAS
        public static final String TAB_ACTIVO = "activo";
        public static final String TAB_USUARIOS = "usuarios";
        public static final String TAB_LINEA_CARTERA = "linea_cartera";
        public static final String TAB_OPERACION = "operacion";

        // ACTIVO
        public static final String COL_ACTIVO_ID = "id_activo";
        public static final String COL_ACTIVO_NOMBRE = "nombre";
        public static final String COL_ACTIVO_GESTORA = "gestora";
        public static final String COL_ACTIVO_ISIN = "isin";
        public static final String COL_ACTIVO_TIPO = "tipo_activo";

        // USUARIOS
        public static final String COL_USUARIO_ID = "id_usuario";
        public static final String COL_USUARIO_NOMBRE = "nombre";
        public static final String COL_USUARIO_APELLIDOS = "apellidos";
        public static final String COL_USUARIO_DNI = "dni";
        public static final String COL_USUARIO_EMAIL = "email";
        public static final String COL_USUARIO_CONTRASENHA = "contrasenha";

        // LINEA_CARTERA
        public static final String COL_LINEA_CARTERA_ID_USUARIO = "id_usuario";
        public static final String COL_LINEA_CARTERA_ID_ACTIVO = "id_activo";
        public static final String COL_LINEA_CARTERA_PARTICIPACIONES = "participaciones";
        public static final String COL_LINEA_CARTERA_IMPORTE = "importe";

        // OPERACION
        public static final String COL_OPERACION_ID = "id_operacion";
        public static final String COL_OPERACION_ID_USUARIO = "id_usuario";
        public static final String COL_OPERACION_ID_ACTIVO = "id_activo";
        public static final String COL_OPERACION_TIPO = "tipo_operacion";
        public static final String COL_OPERACION_FECHA = "fecha";
        public static final String COL_OPERACION_PARTICIPACIONES = "participaciones";
        public static final String COL_OPERACION_PRECIO_UNITARIO = "precio_unitario";
        public static final String COL_OPERACION_CANTIDAD = "cantidad";
        public static final String COL_OPERACION_RENTABILIDAD = "rentabilidad";
    }

