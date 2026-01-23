package ETI.sgc.dao;

import ETI.sgc.model.Usuario;
import ETI.sgc.model.UsuarioDatos;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class UsuarioDAO {

    private final Jdbi jdbi;

    public UsuarioDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    // 🔍 Buscar por username
    public Usuario buscarPorUsername(String username) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                    SELECT id, username, password_hash, rol, activo, usuario_datos_id
                    FROM usuarios
                    WHERE username = :username
                """)
                        .bind("username", username)
                        .map((rs, ctx) -> {
                            Usuario u = new Usuario();
                            u.id = rs.getLong("id");
                            u.username = rs.getString("username");
                            u.password_hash = rs.getString("password_hash");
                            u.rol = rs.getString("rol");
                            u.activo = rs.getBoolean("activo");
                            u.usuario_datos_id = rs.getLong("usuario_datos_id");
                            return u;
                        })
                        .findOne()
                        .orElse(null)
        );
    }

    // 🧑‍💼 Crear usuario COMPLETO (según tu tabla real)
    public void crearUsuarioCompleto(
            String username,
            String passwordHash,
            String rol,
            String cedula,
            String nombres,
            String apellidos,
            String email,
            String telefono,
            String direccion
    ) {

        jdbi.useTransaction(handle -> {

            // 1️⃣ Insertar usuarios_datos
            Long datosId = handle.createUpdate("""
                INSERT INTO usuarios_datos
                (cedula, nombres, apellidos, email, telefono, direccion, created_at)
                VALUES
                (:cedula, :nombres, :apellidos, :email, :telefono, :direccion, NOW())
            """)
                    .bind("cedula", cedula)
                    .bind("nombres", nombres)
                    .bind("apellidos", apellidos)
                    .bind("email", email)
                    .bind("telefono", telefono)
                    .bind("direccion", direccion)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Long.class)
                    .one();

            // 2️⃣ Insertar usuario
            handle.createUpdate("""
                INSERT INTO usuarios
                (username, password_hash, rol, activo, usuario_datos_id)
                VALUES
                (:username, :password_hash, :rol, TRUE, :datosId)
            """)
                    .bind("username", username)
                    .bind("password_hash", passwordHash)
                    .bind("rol", rol)
                    .bind("datosId", datosId)
                    .execute();
        });
    }

    // 🔁 Activar / desactivar usuario
    public void cambiarEstado(Long usuarioId, boolean activo) {
        jdbi.useHandle(handle ->
                handle.createUpdate("""
                    UPDATE usuarios
                    SET activo = :activo
                    WHERE id = :id
                """)
                        .bind("activo", activo)
                        .bind("id", usuarioId)
                        .execute()
        );
    }
    public void resetPassword(Long usuarioId, String passwordHash) {
        jdbi.useHandle(handle ->
                handle.createUpdate("""
                    UPDATE usuarios
                    SET password_hash = :hash
                    WHERE id = :id
                """)
                        .bind("hash", passwordHash)
                        .bind("id", usuarioId)
                        .execute()
        );
    }

    public List<Usuario> listarUsuarios() {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT 
                    u.id u_id,
                    u.username u_username,
                    u.rol u_rol,
                    u.activo u_activo,
                    d.id d_id,
                    d.cedula d_cedula,
                    d.nombres d_nombres,
                    d.apellidos d_apellidos,
                    d.email d_email,
                    d.telefono d_telefono,
                    d.direccion d_direccion
                FROM usuarios u
                JOIN usuarios_datos d ON d.id = u.usuario_datos_id
                ORDER BY u.id DESC
            """)
                        .map((rs, ctx) -> {
                            Usuario u = new Usuario();
                            u.id = rs.getLong("u_id");
                            u.username = rs.getString("u_username");
                            u.rol = rs.getString("u_rol");
                            u.activo = rs.getBoolean("u_activo");

                            UsuarioDatos d = new UsuarioDatos();
                            d.id = rs.getLong("d_id");
                            d.cedula = rs.getString("d_cedula");
                            d.nombres = rs.getString("d_nombres");
                            d.apellidos = rs.getString("d_apellidos");
                            d.email = rs.getString("d_email");
                            d.telefono = rs.getString("d_telefono");
                            d.direccion = rs.getString("d_direccion");

                            u.datos = d;
                            return u;
                        })
                        .list()
        );
    }

}
