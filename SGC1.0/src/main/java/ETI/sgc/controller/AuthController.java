package ETI.sgc.controller;

import ETI.sgc.dao.UsuarioDAO;
import ETI.sgc.dto.LoginRequest;
import ETI.sgc.model.Usuario;
import ETI.sgc.security.JwtUtil;
import io.javalin.Javalin;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;

public class AuthController {

    private final UsuarioDAO usuarioDao;

    public AuthController(UsuarioDAO usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public void routes(Javalin app) {

        app.post("/login", ctx -> {

            LoginRequest req = ctx.bodyAsClass(LoginRequest.class);

            Usuario user = usuarioDao.buscarPorUsername(req.username);

            if (user == null || !BCrypt.checkpw(req.password, user.password_hash)) {
                ctx.status(401).result("Credenciales inválidas");
                return;
            }

            String token = JwtUtil.generar(user);

            ctx.json(Map.of(
                    "token", token,
                    "rol", user.rol,
                    "username", user.username
            ));
        });
        app.post("api2/login", ctx -> {

            LoginRequest req = ctx.bodyAsClass(LoginRequest.class);

            Usuario user = usuarioDao.buscarPorUsername(req.username);

            if (user == null || !BCrypt.checkpw(req.password, user.password_hash)) {
                ctx.status(401).result("Credenciales inválidas");
                return;
            }

            String token = JwtUtil.generar(user);

            ctx.json(Map.of(
                    "token", token,
                    "rol", user.rol,
                    "username", user.username
            ));
        });
    }
}
