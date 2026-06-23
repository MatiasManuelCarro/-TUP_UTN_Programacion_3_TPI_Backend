package com.tp.jpa;

import com.tp.jpa.model.dtos.DetalleTemporal;
import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Pedido;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.model.entities.Usuario;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;
import com.tp.jpa.util.DataLoader;
import com.tp.jpa.util.Validator;

import java.util.*;
import java.util.logging.Level;

import static com.tp.jpa.util.Reports.*;
import static com.tp.jpa.util.Input.*;
import static com.tp.jpa.util.Validator.validarNombreCategoria;
import static com.tp.jpa.util.Validator.validarNombreProducto;


public class Main {
    public static void main(String[] args) {


        //quita los logs de hibernate dejando solo los errores.
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        Scanner sc = new Scanner(System.in);

        CategoriaRepository categoriaRepo = new CategoriaRepository();
        ProductoRepository productoRepo = new ProductoRepository();
        UsuarioRepository usuarioRepo = new UsuarioRepository();
        PedidoRepository pedidoRepo = new PedidoRepository();


        //Clase solo para pruebas - carga categorias y productos si la base se encuentra vacia.
        DataLoader.load(categoriaRepo, productoRepo);

        int opcion;

        do {
            System.out.println("\n");
            System.out.println("===== MENÚ PRINCIPAL =====\n..........................");
            System.out.println("1. Gestión de Categorías");
            System.out.println("2. Gestión de Productos");
            System.out.println("3. Gestión de Usuarios");
            System.out.println("4. Gestión de Pedidos");
            System.out.println("5. Reportes");
            System.out.println("0. Salir");
            System.out.println("..........................\n");

            opcion = intSeguro(sc, "Ingrese un numero: ");

            switch (opcion) {

                case 1 -> menuCategorias(sc, categoriaRepo);

                case 2 -> menuProductos(sc, productoRepo, categoriaRepo);

                case 3 -> menuUsuarios(sc, usuarioRepo);

                case 4 -> menuPedidos(sc, pedidoRepo, usuarioRepo, productoRepo);

                case 5 -> menuReportes(sc, productoRepo, categoriaRepo, usuarioRepo, pedidoRepo);

                case 0 -> System.out.println("Saliendo del sistema...");

                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);

        sc.close();
    }


    private static void menuCategorias(Scanner sc, CategoriaRepository categoriaRepo) {

        int opcion;

        do {
            System.out.println("\n");
            System.out.println("\n===== MENÚ DE CATEGORÍAS =====");
            System.out.println("1. Alta de categoría");
            System.out.println("2. Baja lógica de categoría");
            System.out.println("3. Modificación de categoría");
            System.out.println("4. Listado de categorías activas");
            System.out.println("0. Volver al Menu Principal");
            System.out.println("..........................\n");

            opcion = intSeguro(sc, "Ingrese un numero: ");


            switch (opcion) {

                case 1 -> { //Alta de categoría
                    System.out.println("\n--- ALTA DE CATEGORÍA ---");
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();

                    if (nombre.isBlank()) {
                        System.out.println("No se puede crear una categoria sin nombre. Operación cancelada.");
                        break;
                    }

                    if (validarNombreCategoria(nombre, categoriaRepo, sc)) {
                        break; // si devuelve true no continua
                    }

                    System.out.print("Descripción: ");
                    String descripcion = sc.nextLine();

                    Categoria nueva = Categoria.builder()
                            .nombre(nombre)
                            .descripcion(descripcion)
                            .build();

                    nueva = categoriaRepo.guardar(nueva);

                    System.out.println("Categoría creada con ID: " + nueva.getId());
                }

                case 2 -> { //Baja logica de Categoria
                    System.out.println("\n--- BAJA LÓGICA DE CATEGORÍA ---");


                    if (!mostrarCategoriasActivas(categoriaRepo)) {
                        System.out.println("Operación cancelada.");
                        break;
                    }

                    System.out.print("Elija una categoria ");

                    Long id = LongSeguro(sc, "Seleccione ID:");


                    Optional<Categoria> optionalCat = categoriaRepo.buscarPorId(id);

                    if (optionalCat.isEmpty() || optionalCat.get().isEliminado()) {
                        System.out.println("No existe una categoría activa con ese ID.");
                    } else {
                        Categoria cat = optionalCat.get();
                        boolean eliminado = categoriaRepo.eliminarLogico(id);

                        if (eliminado) {
                            System.out.println("Categoría dada de baja: " + cat.getNombre());
                        } else {
                            System.out.println("No se pudo eliminar la categoría.");
                        }
                    }
                }

                case 3 -> { //modificar categoria
                    System.out.println("\n--- MODIFICACIÓN DE CATEGORÍA ---");

                    if (!mostrarCategoriasActivas(categoriaRepo)) {
                        System.out.println("No hay categorías activas. Operación cancelada.");
                        break;
                    }

                    //revisar

                    Long id = LongSeguro(sc, "Ingrese ID para modificar la categoria: ");

                    Optional<Categoria> optionalCat = categoriaRepo.buscarPorId(id);

                    if (optionalCat.isEmpty() || optionalCat.get().isEliminado()) {
                        System.out.println("No existe una categoría activa con ese ID.");
                        break;
                    }

                    Categoria cat = optionalCat.get();

                    System.out.println("Valores actuales:");
                    System.out.println("Nombre: " + cat.getNombre());
                    System.out.println("Descripción: " + cat.getDescripcion());

                    System.out.print("Nuevo nombre (ENTER para mantener): ");
                    String nuevoNombre = sc.nextLine();

                    if (validarNombreCategoria(nuevoNombre, categoriaRepo, sc)) {
                        break; // salir del case sin pedir descripción
                    }

                    if (!nuevoNombre.isBlank()) cat.setNombre(nuevoNombre);

                    System.out.print("Nueva descripción (ENTER para mantener): ");
                    String nuevaDesc = sc.nextLine();
                    if (!nuevaDesc.isBlank()) cat.setDescripcion(nuevaDesc);

                    categoriaRepo.guardar(cat);

                    System.out.println("Categoría modificada correctamente.");
                }

                case 4 -> {
                    // Listar categorias
                    System.out.println("\n--- LISTADO DE CATEGORÍAS ACTIVAS ---");


                    if (!mostrarCategoriasActivas(categoriaRepo)) {
                        System.out.println("No hay categorías activas. Operación cancelada.");

                    }

                }


                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }


        } while (opcion != 0);

    }

    private static void menuProductos(Scanner sc, ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {

        int opcion;

        do {
            System.out.println("\n");
            System.out.println("\n===== MENÚ DE PRODUCTOS =====");
            System.out.println("1. Alta de producto");
            System.out.println("2. Baja lógica de producto");
            System.out.println("3. Modificación de producto");
            System.out.println("4. Listado de productos activos");
            System.out.println("0. Volver al Menu Principal");
            System.out.println("..........................\n");
            opcion = intSeguro(sc, "Ingrese un numero: ");

            switch (opcion) {

                case 1 -> { //Alta de producto
                    System.out.println("\n--- ALTA DE PRODUCTO ---");


                    if (!mostrarCategoriasActivas(categoriaRepo)) {
                        System.out.println("Operación cancelada.");
                        break;
                    }
                    //verificar long
                    Long idCat = LongSeguro(sc, "Seleccione ID de categoría: ");

                    Optional<Categoria> optionalCat = categoriaRepo.buscarPorId(idCat);


                    if (optionalCat.isEmpty() || optionalCat.get().isEliminado()) {
                        System.out.println("Categoría inválida.");
                        break;
                    }

                    Categoria categoria = optionalCat.get();

                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    if (nombre.isBlank()) {
                        System.out.println("El nombre no puede estar vacío.");
                        break;
                    }
                    if (validarNombreProducto(nombre, productoRepo, sc)) {
                        break; // salir del case sin pedir descripción
                    }

                    System.out.print("Descripción: ");
                    String descripcion = sc.nextLine();

                    System.out.print("Precio: ");
                    double precio = DoubleSeguro(sc, "Ingrese el precio: ");

                    if (precio <= 0) {
                        System.out.println("El precio debe ser mayor a 0.");
                        break;
                    }

                    System.out.print("Stock: ");
                    int stock = intSeguro(sc, "Ingrese el stock: ");

                    if (stock < 0) {
                        System.out.println("El stock no puede ser negativo.");
                        break;
                    }

                    System.out.println("imagen (URL) Opcional");
                    String imagen = sc.nextLine();
                    if (imagen.isBlank()) {
                        imagen = "";
                    }

                    System.out.print("¿Disponible? (S/N): ");
                    String disponibleInput = sc.nextLine().trim().toUpperCase();

                    boolean disponible;
                    if (disponibleInput.equals("S")) {
                        disponible = true;
                    } else if (disponibleInput.equals("N")) {
                        disponible = false;
                    } else {
                        System.out.println("Entrada inválida. Se asumirá NO disponible.");
                        disponible = false;
                    }

                    Producto nuevo = Producto.builder()
                            .nombre(nombre)
                            .descripcion(descripcion)
                            .precio(precio)
                            .stock(stock)
                            .disponible(true)
                            .imagen(imagen)
                            .categoria(categoria)
                            .disponible(disponible)
                            .build();

                    nuevo = productoRepo.guardar(nuevo);

                    System.out.println("Producto creado con ID: " + nuevo.getId());
                    System.out.println("Categoría asignada: " + categoria.getNombre());
                }

                case 2 -> { //Baja lógica de producto
                    System.out.println("\n--- BAJA LÓGICA DE PRODUCTO ---");


                    if (!mostrarProductosActivos(productoRepo)) {
                        System.out.println("Operación cancelada.");
                        break;
                    }

                    Long id = LongSeguro(sc, "Seleccione ID de producto: ");

                    Optional<Producto> optionalProd = productoRepo.buscarPorId(id);

                    if (optionalProd.isEmpty() || optionalProd.get().isEliminado()) {
                        System.out.println("No existe un producto activo con ese ID.");
                        break;
                    }

                    Producto prod = optionalProd.get();

                    boolean eliminado = productoRepo.eliminarLogico(id);

                    if (eliminado) {
                        System.out.println("Producto dado de baja: " + prod.getNombre());
                    } else {
                        System.out.println("No se pudo eliminar el producto.");
                    }
                }

                case 3 -> { //Modificación de producto
                    System.out.println("\n--- MODIFICACIÓN DE PRODUCTO ---");


                    if (!mostrarProductosActivos(productoRepo)) {
                        System.out.println("Operación cancelada.");
                        break;
                    }

                    Long id = LongSeguro(sc, "Seleccione ID de producto: ");

                    Optional<Producto> optionalProd = productoRepo.buscarPorId(id);

                    if (optionalProd.isEmpty() || optionalProd.get().isEliminado()) {
                        System.out.println("No existe un producto activo con ese ID.");
                        break;
                    }

                    Producto prod = optionalProd.get();

                    System.out.println("Valores actuales:");
                    System.out.println("Nombre: " + prod.getNombre());
                    System.out.println("Precio: " + prod.getPrecio());
                    System.out.println("Descripción: " + prod.getDescripcion());
                    System.out.println("Stock: " + prod.getStock());

                    System.out.print("Nuevo nombre (ENTER para mantener): ");
                    String nuevoNombre = sc.nextLine();
                    if (!nuevoNombre.isBlank()) {
                        if (validarNombreProducto(nuevoNombre, productoRepo, sc)) {
                            break;
                        }
                        prod.setNombre(nuevoNombre);
                    }

                    System.out.print("Nuevo precio (ENTER para mantener): ");
                    String nuevoPrecio = sc.nextLine();

                    if (!nuevoPrecio.isBlank()) { //si presiona ENTER -> mantiene el valor
                        double precio = DoubleSeguro(sc, "Ingrese el nuevo precio: ");

                        if (precio <= 0) {
                            System.out.println("El precio debe ser mayor a 0.");
                            break;
                        }

                        prod.setPrecio(precio);
                    }


                    System.out.print("Nueva descripción (ENTER para mantener): ");
                    String nuevaDescripcion = sc.nextLine();
                    if (!nuevaDescripcion.isBlank()) prod.setDescripcion(nuevaDescripcion);

                    System.out.print("Nuevo stock (ENTER para mantener): ");
                    String nuevoStock = sc.nextLine();

                    if (!nuevoStock.isBlank()) { // si presiona ENTER -> mantiene el valor actual
                        int stock = intSeguro(sc, "Ingrese el nuevo stock: ");

                        if (stock < 0) {
                            System.out.println("El stock no puede ser negativo.");
                            break;
                        }

                        prod.setStock(stock);
                    }


                    productoRepo.guardar(prod);

                    System.out.println("Producto modificado correctamente.");
                }

                case 4 -> { //Listado de productos activos
                    System.out.println("\n--- LISTADO DE PRODUCTOS ACTIVOS ---");

                    mostrarProductosActivos(productoRepo);

                }


                // Volver
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private static void menuUsuarios(Scanner sc, UsuarioRepository usuarioRepo) {
        int opcion;
        do {
            System.out.println("\n===== MENÚ DE USUARIOS =====");
            System.out.println("1. Alta de usuario");
            System.out.println("2. Modificación de usuario");
            System.out.println("3. Baja lógica de usuario");
            System.out.println("4. Listado de usuarios activos");
            System.out.println("5. Buscar usuario por mail");
            System.out.println("0. Volver al menú principal");

            opcion = intSeguro(sc, "Ingrese un número: ");

            switch (opcion) {
                case 1 -> { // Alta de usuario
                    System.out.println("\n--- ALTA DE USUARIO ---");
                    System.out.println("\nIngrese los datos del usuario:");
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    if (nombre.isBlank()) {
                        System.out.println("El nombre no puede estar vacío.");
                        break;
                    }

                    System.out.print("Apellido: ");
                    String apellido = sc.nextLine();
                    if (apellido.isBlank()) {
                        System.out.println("El apellido no puede estar vacío.");
                        break;
                    }

                    System.out.print("Mail: ");
                    String mail = sc.nextLine();
                    // valida formato
                    if (!Validator.validarFormatoMail(mail)) {
                        System.out.println("Formato de mail inválido.");
                        break;
                    }

                    //valida si esta disponible
                    if (!Validator.validarMailDisponible(mail, usuarioRepo, sc)) {
                        break;
                    }

                    System.out.print("Celular (opcional): ");
                    String celular = sc.nextLine();

                    if (!Validator.validarCelular(celular)) {
                        System.out.println("El celular solo puede tener números y opcionalmente el símbolo +.");
                        break;
                    }

                    System.out.print("Ingrese Contraseña: ");
                    String contrasenia = sc.nextLine();
                    if (contrasenia.isBlank()) {
                        System.out.println("La contraseña no puede estar vacía.");
                        break;
                    }

                    System.out.println("Rol (1 = ADMIN, 2 = USUARIO): ");
                    int rolOpcion = intSeguro(sc, "Seleccione rol: ");
                    Rol rol = (rolOpcion == 1) ? Rol.ADMIN : Rol.USUARIO;

                    Usuario nuevo = Usuario.builder()
                            .nombre(nombre)
                            .apellido(apellido)
                            .mail(mail)
                            .celular(celular)
                            .contrasenia(contrasenia)
                            .rol(rol)
                            .build();

                    nuevo = usuarioRepo.guardar(nuevo);
                    System.out.println("Usuario creado con ID: " + nuevo.getId());
                }

                case 2 -> { // Modificación
                    System.out.println("\n--- MODIFICACIÓN DE USUARIO ---");
                    if (!mostrarUsuariosActivos(usuarioRepo)) {
                        System.out.println("No hay usuarios activos.");
                        break;
                    }


                    Long id = LongSeguro(sc, "Seleccione ID de usuario: ");
                    var optUser = usuarioRepo.buscarPorId(id);
                    if (optUser.isEmpty() || optUser.get().isEliminado()) {
                        System.out.println("Usuario inválido.");
                        break;
                    }
                    Usuario user = optUser.get();

                    // Variables temporales (input del usuario)
                    String nuevoNombre = user.getNombre();
                    String nuevoApellido = user.getApellido();
                    String nuevoMail = user.getMail();
                    String nuevoCelular = user.getCelular();
                    String nuevaPass = user.getContrasenia();

                    System.out.println("Nombre actual: " + user.getNombre());
                    System.out.print("Nuevo nombre (ENTER para mantener): ");
                    String inputNombre = sc.nextLine();
                    if (!inputNombre.isBlank()) nuevoNombre = inputNombre;

                    System.out.println("Apellido actual: " + user.getApellido());
                    System.out.print("Nuevo apellido (ENTER para mantener): ");
                    String inputApellido = sc.nextLine();
                    if (!inputApellido.isBlank()) nuevoApellido = inputApellido;

                    System.out.println("Mail actual: " + user.getMail());
                    System.out.print("Nuevo mail (ENTER para mantener): ");
                    String inputMail = sc.nextLine();

                    if (!inputMail.isBlank()) {
                        // Validar formato de mail
                        if (!Validator.validarFormatoMail(inputMail)) {
                            System.out.println("Formato de mail inválido.");
                            break;
                        }

                        // valida disponibilidad
                        if (!Validator.validarMailDisponible(inputMail, usuarioRepo, sc)) {
                            break;
                        }
                        nuevoMail = inputMail;
                    }
                    // Verifica que si el mail existe sea del mismo usuario
                    var existente = usuarioRepo.buscarPorMail(inputMail);
                    if (existente.isPresent() && !existente.get().getId().equals(user.getId())) {
                        System.out.println("Ya existe otro usuario con ese mail.");
                        break;
                    }

                    System.out.println("Celular actual: " + user.getCelular());
                    System.out.print("Nuevo celular (ENTER para mantener): ");
                    String inputCelular = sc.nextLine();

                    if (!inputCelular.isBlank()) {
                        if (!Validator.validarCelular(inputCelular)) {
                            System.out.println("El celular debe contener solo números.");
                            break;
                        }
                        nuevoCelular = inputCelular;
                    }

                    System.out.print("Nueva contraseña (ENTER para mantener): ");
                    String inputPass = sc.nextLine();
                    if (!inputPass.isBlank()) nuevaPass = inputPass;


                    //Si se completa correctamente los datos se setean los cambios
                    //si falla algo, no se realiza ningun cambio
                    user.setNombre(nuevoNombre);
                    user.setApellido(nuevoApellido);
                    user.setMail(nuevoMail);
                    user.setCelular(nuevoCelular);
                    user.setContrasenia(nuevaPass);

                    usuarioRepo.guardar(user);
                    System.out.println("Usuario modificado correctamente.");
                }

                case 3 -> { // Baja lógica
                    System.out.println("\n--- BAJA LÓGICA DE USUARIO ---");
                    if (!mostrarUsuariosActivos(usuarioRepo)) {
                        System.out.println("No hay usuarios activos.");
                        break;
                    }
                    Long id = LongSeguro(sc, "Seleccione ID de usuario: ");
                    var optUser = usuarioRepo.buscarPorId(id);
                    if (optUser.isEmpty() || optUser.get().isEliminado()) {
                        System.out.println("Usuario inválido.");
                        break;
                    }
                    Usuario user = optUser.get();
                    if (usuarioRepo.eliminarLogico(id)) {
                        System.out.println("Usuario dado de baja: " + user.getNombre() + " " + user.getApellido());
                    } else {
                        System.out.println("No se pudo eliminar el usuario.");
                    }
                }

                case 4 -> { // Listado
                    System.out.println("\n--- LISTADO DE USUARIOS ACTIVOS ---");
                    mostrarUsuariosActivos(usuarioRepo);
                }

                case 5 -> { // Buscar por mail
                    System.out.print("Ingrese mail: ");
                    String mail = sc.nextLine();
                    var optUser = usuarioRepo.buscarPorMail(mail);
                    if (optUser.isPresent()) {
                        Usuario u = optUser.get();
                        System.out.println("Usuario encontrado:");
                        System.out.println("ID: " + u.getId());
                        System.out.println("Nombre: " + u.getNombre() + " " + u.getApellido());
                        System.out.println("Mail: " + u.getMail());
                        System.out.println("Celular: " + u.getCelular());
                        System.out.println("Rol: " + u.getRol());
                    } else {
                        System.out.println("No existe usuario activo con ese mail.");
                    }
                }

                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void menuPedidos(Scanner sc, PedidoRepository pedidoRepo, UsuarioRepository usuarioRepo, ProductoRepository productoRepo) {
        int opcion;
        do {
            System.out.println("\n\n===== MENÚ DE PEDIDOS =====");
            System.out.println("1. Alta de pedido");
            System.out.println("2. Cambiar estado");
            System.out.println("3. Baja lógica");
            System.out.println("4. Listado");
            System.out.println("5. Pedidos por usuario");
            System.out.println("6. Pedidos por estado");
            System.out.println("0. Volver");

            opcion = intSeguro(sc, "Seleccione una opción: ");

            switch (opcion) {
                case 1 -> { //alta de pedido
                    // Selección de usuario
                    mostrarUsuariosActivos(usuarioRepo);
                    Long idUsuario = LongSeguro(sc, "Seleccione ID de usuario: ");
                    Optional<Usuario> usuarioOpt = usuarioRepo.buscarPorId(idUsuario);
                    if (usuarioOpt.isEmpty()) {
                        System.out.println("Usuario no encontrado.");
                        continue;
                    }

                    // Forma de pago
                    System.out.println("Formas de pago: 1.TARJETA  2.TRANSFERENCIA  3.EFECTIVO");
                    int opcionPago = intSeguro(sc, "Seleccione forma de pago: ");
                    FormaPago formaPago;
                    switch (opcionPago) {
                        case 1 -> formaPago = FormaPago.TARJETA;
                        case 2 -> formaPago = FormaPago.TRANSFERENCIA;
                        case 3 -> formaPago = FormaPago.EFECTIVO;
                        default -> {
                            System.out.println("Opción inválida.");
                            continue;
                        }
                    }

                    // Productos
                    List<DetalleTemporal> detallesTemp = new ArrayList<>();
                    boolean agregarMas = true;
                    while (agregarMas) {
                        if (mostrarProductosActivos(productoRepo)) {
                            Long idProd = LongSeguro(sc, "ID producto: ");
                            Optional<Producto> prodOpt = productoRepo.buscarPorId(idProd);

                            if (prodOpt.isEmpty() || !prodOpt.get().isDisponible()) {
                                System.out.println("Producto inválido o no disponible.");
                                continue;
                            }

                            int cantidad = intSeguro(sc, "Cantidad: ");
                            if (cantidad <= 0) {
                                System.out.println("La cantidad debe ser mayor a 0.");
                                continue;
                            }

                            //cantidad mayor al stock disponible
                            if (cantidad > prodOpt.get().getStock()) {
                                System.out.println("Stock insuficiente. Disponible: " + prodOpt.get().getStock());
                                continue;
                            }

                            //se guarda de manera temporal los detalles para usarlos
                            detallesTemp.add(new DetalleTemporal(idProd, cantidad));

                            boolean opcionValida = false; //continua hasta que el usuario ingresa 1 o 2
                            while (!opcionValida) {
                                System.out.println("¿Agregar otro producto? (1.Sí / 2.No)");
                                int opcionAgregar = intSeguro(sc, "Seleccione: ");
                                switch (opcionAgregar) {
                                    case 1 -> {
                                        agregarMas = true;
                                        opcionValida = true;
                                    }
                                    case 2 -> {
                                        agregarMas = false;
                                        opcionValida = true;
                                    }
                                    default -> System.out.println("Opción inválida. Ingrese 1 o 2.");
                                }
                            }

                            if (detallesTemp.isEmpty()) {
                                System.out.println("El pedido debe tener al menos un producto.");
                                continue;
                            }

                            // Llamada al repositorio
                            try {
                                Pedido pedido = pedidoRepo.altaPedido(usuarioOpt.get(), formaPago, detallesTemp);
                                System.out.println("Pedido creado con ID: " + pedido.getId() + " | Total: $" + pedido.getTotal());
                            } catch (Exception e) {
                                System.out.println("Error al crear pedido. Se hizo rollback.");
                            }
                        } else {
                            System.out.println("No hay productos activos para seleccionar.");
                            continue;
                        }
                    }
                }
                case 2 -> {// cambiar estado de pedido
                    // Selección de pedido
                    mostrarPedidosActivos(pedidoRepo);
                    Long idPedido = LongSeguro(sc, "Seleccione ID de pedido: ");
                    Optional<Pedido> pedidoOpt = pedidoRepo.buscarPorId(idPedido);

                    if (pedidoOpt.isEmpty() || pedidoOpt.get().isEliminado()) {
                        System.out.println("Pedido no encontrado o dado de baja.");
                        continue;
                    }

                    Pedido pedido = pedidoOpt.get();
                    System.out.println("Estado actual: " + pedido.getEstado());

                    // Opciones de estado
                    System.out.println("Cambiar estado actual:");
                    System.out.println("1. PENDIENTE");
                    System.out.println("2. CONFIRMADO");
                    System.out.println("3. TERMINADO");
                    System.out.println("4. CANCELADO");

                    int opcionEstado = intSeguro(sc, "Seleccione nuevo estado: ");
                    Estado nuevoEstado;
                    switch (opcionEstado) {
                        case 1 -> nuevoEstado = Estado.PENDIENTE;
                        case 2 -> nuevoEstado = Estado.CONFIRMADO;
                        case 3 -> nuevoEstado = Estado.TERMINADO;
                        case 4 -> nuevoEstado = Estado.CANCELADO;
                        default -> {
                            System.out.println("Opción inválida.");
                            continue;
                        }
                    }

                    // Actualizar y guardar
                    pedido.setEstado(nuevoEstado);
                    pedidoRepo.guardar(pedido);

                    System.out.println("Pedido ID " + pedido.getId() +
                            " actualizado a estado: " + pedido.getEstado());

                }

                case 3 -> { //baja logica
                    // Mostrar pedidos activos
                    if (!mostrarPedidosActivos(pedidoRepo)) {
                        System.out.println("No hay pedidos activos para dar de baja.");
                        continue;
                    }

                    Long idPedido = LongSeguro(sc, "Seleccione ID de pedido: ");
                    Optional<Pedido> pedidoOpt = pedidoRepo.buscarPorId(idPedido);

                    // Validar existencia y que no esté eliminado
                    if (pedidoOpt.isEmpty() || pedidoOpt.get().isEliminado()) {
                        System.out.println("Pedido no encontrado o ya estaba dado de baja.");
                        continue;
                    }

                    Pedido pedido = pedidoOpt.get();

                    // Ejecutar baja lógica
                    boolean eliminado = pedidoRepo.eliminarLogico(idPedido);

                    if (!eliminado) {
                        System.out.println("Error al dar de baja el pedido.");
                        continue;
                    }

                    // Confirmar con datos guardados antes de la baja
                    System.out.println("Pedido ID " + pedido.getId() +
                            " dado de baja correctamente. Total: $" + pedido.getTotal());

                }
                case 4 -> { //mostrar pedidos
                    if (!mostrarPedidosActivos(pedidoRepo)) {
                        System.out.println("No se encontraron pedidos activos.");
                        continue;
                    }
                }
                case 5 -> {// pedidos por usuario

                    if (!mostrarUsuariosActivos(usuarioRepo)) {
                        continue;
                    }
                    Long idUsuario = LongSeguro(sc, "Seleccione ID de usuario: ");

                    if (!mostrarPedidosPorUsuario(pedidoRepo, idUsuario)) {
                        continue;
                    }
                }
                case 6 -> { // pedidos por estado
                    System.out.println("Seleccione estado de pedido:");
                    System.out.println("1. PENDIENTE");
                    System.out.println("2. CONFIRMADO");
                    System.out.println("3. TERMINADO");
                    System.out.println("4. CANCELADO");

                    int opcionEstado = intSeguro(sc, "Seleccione opción: ");
                    Estado estadoBuscado;

                    switch (opcionEstado) {
                        case 1 -> estadoBuscado = Estado.PENDIENTE;
                        case 2 -> estadoBuscado = Estado.CONFIRMADO;
                        case 3 -> estadoBuscado = Estado.TERMINADO;
                        case 4 -> estadoBuscado = Estado.CANCELADO;
                        default -> {
                            System.out.println("Opción inválida.");
                            continue;
                        }

                    }

                    if (!mostrarPedidosPorEstado(pedidoRepo, estadoBuscado)) {

                        continue;
                    }
                }
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);

    }

    private static void menuReportes(Scanner sc, ProductoRepository productoRepo, CategoriaRepository categoriaRepo, UsuarioRepository usuarioRepo,
                                     PedidoRepository pedidoRepo) {
        int opcionReporte;
        do {
            System.out.println("\n\n===== REPORTES =====");
            System.out.println("1. Listar Productos por categoría");
            System.out.println("2. Pedidos por usuario");
            System.out.println("3. Pedidos por estado");
            System.out.println("4. Total facturado");
            System.out.println("0. Volver al menú principal");

            opcionReporte = intSeguro(sc, "Ingrese un número: ");

            switch (opcionReporte) {
                case 1 -> {
                    if (!mostrarCategoriasActivas(categoriaRepo)) {
                        System.out.println("Operación cancelada.");
                        break;
                    }
                    Long idCategoria = LongSeguro(sc, "\nSeleccione ID de categoría: ");
                    listarProductoPorCategoriaHelper(productoRepo, idCategoria);
                }

                case 2 -> { //pedidos por usuario
                    if (mostrarUsuariosActivos(usuarioRepo)) {
                        Long idUsuario = LongSeguro(sc, "Seleccione ID de usuario: ");
                        if (mostrarPedidosPorUsuario(pedidoRepo, idUsuario)) {
                            System.out.println("Fin del listado.");
                        }
                    } else {
                        System.out.println("No hay usuarios activos para seleccionar.");
                    }
                }

                case 3 -> { //pedidos por estado
                    System.out.println("Seleccione estado de pedido:");
                    System.out.println("1. PENDIENTE");
                    System.out.println("2. CONFIRMADO");
                    System.out.println("3. TERMINADO");
                    System.out.println("4. CANCELADO");

                    int opcionEstado = intSeguro(sc, "Seleccione opción: ");
                    Estado estadoBuscado = null;

                    switch (opcionEstado) {
                        case 1 -> estadoBuscado = Estado.PENDIENTE;
                        case 2 -> estadoBuscado = Estado.CONFIRMADO;
                        case 3 -> estadoBuscado = Estado.TERMINADO;
                        case 4 -> estadoBuscado = Estado.CANCELADO;
                        default -> {
                            System.out.println("Opción inválida.");
                            continue;
                        }
                    }

                    if (mostrarPedidosPorEstado(pedidoRepo, estadoBuscado)) {
                        System.out.println("Fin del listado.");
                    }
                }

                case 4 -> {
                    mostrarTotalFacturado(pedidoRepo);
                }

                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcionReporte != 0);
    }
}





