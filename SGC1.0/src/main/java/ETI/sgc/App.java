package ETI.sgc;

import ETI.sgc.config.Database;
import ETI.sgc.controller.AuthController;
import ETI.sgc.controller.UsuarioController;
import ETI.sgc.dao.UsuarioDAO;
import ETI.sgc.security.JwtUtil;
import io.javalin.Javalin;
import org.mindrot.jbcrypt.BCrypt;

public class App {

    public static void main(String[] args) {

        // 1️⃣ Inicializar BD
        Database.init();

        UsuarioDAO usuarioDao = new UsuarioDAO(Database.jdbi);

        // 2️⃣ CREAR ADMIN INICIAL SI NO EXISTE
        if (usuarioDao.buscarPorUsername("admin") == null) {

            String hash = BCrypt.hashpw("admin123", BCrypt.gensalt(10));

            usuarioDao.crearUsuarioCompleto(
                    "admin",
                    hash,
                    "ADMIN",
                    "0000000000",
                    "Administrador",
                    "Sistema",
                    "admin@sgc.local",
                    "0000000000",
                    "Sistema"
            );

            System.out.println("⚠️ ADMIN creado");
            System.out.println("   usuario: admin");
            System.out.println("   contraseña: admin123");
        }

        // 3️⃣ Arrancar Javalin
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(8080);

        // 4️⃣ Middleware JWT (ANTES DE LAS RUTAS)
        app.before("/api/*", ctx -> {

            // 🔓 Permitir login sin token
            if (ctx.path().equals("/api/login")) {
                return;
            }

            String auth = ctx.header("Authorization");

            if (auth == null || !auth.startsWith("Bearer ")) {
                ctx.status(401).json("Token requerido");
                return;
            }

            try {
                var claims = JwtUtil.validar(auth.replace("Bearer ", ""));
                ctx.attribute("rol", claims.get("rol"));
                ctx.attribute("username", claims.getSubject());
            } catch (Exception e) {
                ctx.status(401).json("Token inválido o expirado");
            }
        });

        // 5️⃣ Registrar controladores
        new AuthController(usuarioDao).routes(app);
        new UsuarioController(usuarioDao).routes(app);

        System.out.println("✅ API SGC corriendo en http://localhost:8080");
    }
}
