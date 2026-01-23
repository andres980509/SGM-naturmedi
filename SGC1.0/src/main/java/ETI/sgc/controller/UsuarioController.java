package ETI.sgc.controller;

import ETI.sgc.dao.UsuarioDAO;
import ETI.sgc.dto.CrearUsuarioRequest;
import ETI.sgc.model.Usuario;
import io.javalin.Javalin;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Map;

public class UsuarioController {

    private final UsuarioDAO usuarioDao;

    public UsuarioController(UsuarioDAO usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean rolValido(String rol) {
        return "ADMIN".equals(rol) || "VENDEDOR".equals(rol);
    }

    public void routes(Javalin app) {

        /* ===============================
           🧑‍💼 CREAR USUARIO (ADMIN)
        =============================== */
        app.post("/api/usuarios", ctx -> {

            String rolActual = ctx.attribute("rol");
            if (!"ADMIN".equals(rolActual)) {
                ctx.status(403).json("Solo ADMIN puede crear usuarios");
                return;
            }

            CrearUsuarioRequest req = ctx.bodyAsClass(CrearUsuarioRequest.class);

            if (
                    isBlank(req.username) ||
                            isBlank(req.password) ||
                            isBlank(req.rol) ||

                            isBlank(req.cedula) ||
                            isBlank(req.nombres) ||
                            isBlank(req.apellidos) ||
                            isBlank(req.email) ||
                            isBlank(req.telefono) ||
                            isBlank(req.direccion)
            ) {
                ctx.status(400).json("Todos los campos son obligatorios");
                return;
            }

            if (!rolValido(req.rol)) {
                ctx.status(400).json("Rol inválido");
                return;
            }

            Usuario existente = usuarioDao.buscarPorUsername(req.username);
            if (existente != null) {
                ctx.status(409).json("El usuario ya existe");
                return;
            }

            String hash = BCrypt.hashpw(req.password, BCrypt.gensalt(10));

            usuarioDao.crearUsuarioCompleto(
                    req.username,
                    hash,
                    req.rol,
                    req.cedula,
                    req.nombres,
                    req.apellidos,
                    req.email,
                    req.telefono,
                    req.direccion
            );

            ctx.status(201).json("Usuario creado correctamente");
        });

        /* ===============================
           📋 LISTAR USUARIOS (ADMIN)
        =============================== */
        app.get("/api/usuarios", ctx -> {

            String rolActual = ctx.attribute("rol");
            if (!"ADMIN".equals(rolActual)) {
                ctx.status(403).json("Acceso denegado");
                return;
            }

            List<Usuario> usuarios = usuarioDao.listarUsuarios();
            ctx.json(usuarios);
        });

        /* ===============================
           🔁 ACTIVAR / DESACTIVAR
        =============================== */
        app.patch("/api/usuarios/{id}/estado", ctx -> {

            String rolActual = ctx.attribute("rol");
            if (!"ADMIN".equals(rolActual)) {
                ctx.status(403).json("Acceso denegado");
                return;
            }

            Long id = Long.parseLong(ctx.pathParam("id"));
            Boolean activo = ctx.bodyAsClass(Map.class).get("activo") != null
                    ? Boolean.valueOf(ctx.bodyAsClass(Map.class).get("activo").toString())
                    : null;

            if (activo == null) {
                ctx.status(400).json("Campo 'activo' requerido");
                return;
            }

            usuarioDao.cambiarEstado(id, activo);
            ctx.json("Estado actualizado");
        });

        /* ===============================
           🔑 RESET PASSWORD (ADMIN)
        =============================== */
        app.patch("/api/usuarios/{id}/reset-password", ctx -> {
            
            String rolActual = ctx.attribute("rol");
            if (!"ADMIN".equals(rolActual)) {
                ctx.status(403).json("Acceso denegado");
                return;
            }

            Long id = Long.parseLong(ctx.pathParam("id"));
            String nuevaPassword = ctx.bodyAsClass(Map.class).get("password") != null
                    ? ctx.bodyAsClass(Map.class).get("password").toString()
                    : null;

            if (isBlank(nuevaPassword)) {
                ctx.status(400).json("Contraseña requerida");
                return;
            }

            String hash = BCrypt.hashpw(nuevaPassword, BCrypt.gensalt(10));
            usuarioDao.resetPassword(id, hash);

            ctx.json("Contraseña restablecida");
        });
    }
}
