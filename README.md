# Trabajo Practico Integrador - Food Store – Backend JPA (Consola)

## Alumno: Matias Manuel Carro

## 🎥 Video de presentación

https://youtu.be/0o0vLaEKT-o

#### Aplicación de consola desarrollada para la materia **Programación III** de la **Tecnicatura Universitaria en Programación – UTN**.

# 📌 Introducción

Food Store es una aplicación de consola desarrollada para la materia **Programación III**, cuyo objetivo es implementar un sistema completo de gestión utilizando **Java Persistence API (JPA)**, repositorios genéricos, consultas JPQL, validaciones y operaciones CRUD con baja lógica.

El sistema permite administrar:

- Categorías  
- Productos  
- Usuarios  
- Pedidos  
- Reportes  

Todo persistido mediante **Hibernate + JPA**, con una arquitectura modular, escalable y orientada a buenas prácticas.

---

---

# 🎯 Objetivos del proyecto

- Implementar entidades JPA con relaciones correctas.  
- Aplicar repositorios genéricos con CRUD completo.  
- Utilizar JPQL para consultas personalizadas.  
- Implementar baja lógica y reactivación de registros.  
- Desarrollar un menú de consola funcional y validado.  
- Gestionar pedidos con stock, totales y estados.  
- Aplicar buenas prácticas de arquitectura y modularidad.

---

# 🗄️ Modelo de Datos

El sistema implementa las siguientes entidades:

- **Categoria**
- **Producto**
- **Usuario**
- **Pedido**
- **DetallePedido**

### Relaciones principales:

- Producto → Categoría (**ManyToOne**)  
- Usuario → Pedido (**OneToMany**)  
- Pedido → DetallePedido (**OneToMany**)  
- DetallePedido → Producto (**ManyToOne**)  

Todas las entidades incluyen:

- ID autogenerado  
- Campo `eliminado` para baja lógica  
- Builder pattern (Lombok)  
- Validaciones en el menú  

---

# ⚙️ Funcionalidades Principales

## 📦 Repositorio Genérico (BaseRepository)

El repositorio base implementa las operaciones CRUD comunes para todas las entidades:

- Guardar / actualizar (`guardar()`)
- Buscar por ID (`buscarPorId()`)
- Listar activos (`listarActivos()` JPQL con `eliminado = false`)
- Listar inactivos (`listarInactivos()` JPQL con `eliminado = true`)
- Baja lógica (`eliminarLogico()` Donde se declara `eliminado = true`) 
- Alta lógica (reactivar registros `eliminado = false`)
- Manejo de transacciones y cierre de `EntityManager`
- Uso de `Optional<T>`

- `guardar()`  
- `buscarPorId()`  
- `listarActivos()`  
- `listarInactivos()`  
- `eliminarLogico()`  
- `AltaLogica()`  
- Manejo de transacciones  

Los repositorios específicos extienden esta clase y agregan consultas JPQL personalizadas.

---

## 🛒 Gestión de Productos

### ✔ Alta
- Lista categorías activas  
- Solicita nombre, descripción, precio y stock  
- Valida precio > 0 y stock ≥ 0  
- Valida duplicados y permite reactivar productos inactivos  

### ✔ Baja lógica
- Marca el producto como eliminado  

### ✔ Modificación
- Muestra valores actuales  
- Permite mantener campos con ENTER  
- Valida precio y stock  

### ✔ Listado
- Muestra todos los productos activos  
- Incluye categoría asociada  

---

# 👤 Gestión de Usuarios

### ✔ Alta
- Validación de mail (formato + duplicados)  
- Reactivación de usuarios inactivos  
- Rol configurable (ADMIN / USUARIO)  

### ✔ Modificación
- Validación de mail  
- Validación de celular  

### ✔ Baja lógica
- `eliminado = true`  

### ✔ Búsqueda por mail
- Implementada con JPQL  

---

# 📦 Gestión de Pedidos

### ✔ Alta de pedido
- Selección de usuario  
- Selección de forma de pago  
- Agregado de productos con validación de stock  
- Cálculo automático del total  

### ✔ Cambio de estado
Estados disponibles:
- PENDIENTE  
- PREPARACION  
- ENVIADO  
- TERMINADO  

### ✔ Baja lógica
- `eliminado = true`  

### ✔ Listados
- Pedidos activos  
- Pedidos por usuario  
- Pedidos por estado  

---

# 🔎 Reportes (JPQL)

Incluye:

- Productos por categoría  
- Pedidos por estado  
- Pedidos por usuario  
- Total facturado  

Ejemplo de consultas JPQL utilizadas:

- `buscarPorUsuario()` 
```java
String jpql = "SELECT p FROM Pedido p WHERE p.usuario.id = :uid AND p.eliminado = false";
```

- `buscarPorEstado()`

```java
 String jpql = "SELECT p FROM Pedido p WHERE p.estado = :estado AND p.eliminado = false";
```

- `buscarPorCategoria()`
```java
String jpql = "SELECT p FROM Producto p " +
                    "WHERE p.categoria.id = :categoriaId " +
                    "AND p.eliminado = false";
```

- `buscarPorNombre()`
```java
String jpql = "SELECT p FROM Producto p " +
                    "WHERE LOWER(p.nombre) = LOWER(:nombre) " +
                    "AND p.eliminado = false";
```

 

## 🛠️ Tecnologías utilizadas

- Java 21
- JPA / Hibernate  
- Jakarta Persistence  
- Gradle  
- Lombok  
- H2 

---

## 📁 Estructura del proyecto

```
src/main/java/com/tp/jpa/
│
├── model/
│   ├── entities/        # Entidades JPA (Categoria, Producto, Base, etc.)
│   └── enums/           # Enums del TP base
│
├── repository/
│   ├── BaseRepository.java
│   ├── CategoriaRepository.java
│   ├── ProductoRepository.java
│   ├── UsuarioRepository.java
│   └── PedidoRepository.java
│
├── util/
│   ├── JPAUtil.java
│   ├── Input.java         # Utilidad para los inputs de usuario
│   ├── Validator.java     # Utilidad para validar los ingresos de usuario
│   ├── Reports.java       # Utilidad para imprimir en pantalla los informes
│   └── DataLoader.java    # Carga datos en el primer inicio - uso educativo y de muestra
│
└── Main.java            # Menú principal y submenús ABM y reportes
```


## 🚀 Instalación y ejecución

### 1. Clonar o descomprimir el proyecto

abrir la consola en la carpeta raiz del proyecto

### 2. Compilar el proyecto

```
./gradlew build
```

### 3. Configurar UTF‑8 en Windows 🛠️ (antes de ejecutar el proyecto)

Para que la aplicación muestre correctamente los acentos y caracteres especiales en la consola de Windows, es necesario configurar la terminal en **UTF‑8**.  
De lo contrario, pueden aparecer símbolos incorrectos como:

```
MEN├Ü PRINCIPAL
Gesti├│n de Categor├¡as
```

##### 3.1 - Paso 1 — Cambiar la página de códigos a UTF‑8

Ejecutar en PowerShell:

```
chcp 65001
```

##### 3.2 - Paso 2 — Configurar la consola para usar UTF‑8 real

En PowerShell, ejecutar:

```
[Console]::OutputEncoding = [Text.Encoding]::UTF8
```

### 3. Ejecutar la aplicación

```
./gradlew run
```

El sistema abrirá el menú principal en consola.

---

## ▶️ Uso del sistema

El menú principal permite acceder a:

1. Gestión de categorías  
2. Gestión de productos  
3. Gestion de Usuarios
4. Gestion de Pedidos
5. Reportes  
0. Salir  

Cada submenú guía al usuario paso a paso con validaciones y mensajes claros, mostrando siempre mensajes de error cuando el ID no existe o el registro está dado de baja, y confirmaciones cuando las operaciones se realizan correctamente.

---

## 🎥 Video de presentación



### Proyecto realizado por:
**Matías Manuel Carro**  
Tecnicatura Universitaria en Programación – UTN