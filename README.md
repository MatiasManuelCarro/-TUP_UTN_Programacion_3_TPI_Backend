# JPA – ABM de Categorías y Productos

## Java Persistence API (JPA) - Programación 3 - Segundo Parcial Parcial 

## Alumno: Matias Manuel Carro

## 🎥 Video de presentación

Link al video:  https://www.youtube.com/watch?v=7qWdzV3JqFE

#### Aplicación de consola desarrollada para la materia **Programación III** de la **Tecnicatura Universitaria en Programación – UTN**.

El objetivo del proyecto es implementar un sistema de gestión de **Categorías** y **Productos** utilizando **Java Persistence API (JPA)**, aplicando repositorios genéricos, operaciones CRUD, validaciones y una consulta JPQL 


Aplicación de consola desarrollada para la materia **Programación III** de la **Tecnicatura Universitaria en Programación – UTN**.

El objetivo del proyecto es implementar un sistema de gestión de **Categorías** y **Productos** utilizando **Java Persistence API (JPA)**, aplicando repositorios genéricos, operaciones CRUD, validaciones y una consulta JPQL personalizada.

---

## ⚙️ Funcionalidades Principales

### 📦 1. Repositorio Genérico (BaseRepository)

Implementa las operaciones CRUD comunes para todas las entidades:

- Guardar / actualizar (`merge`)
- Buscar por ID (`Optional<T>`)
- Listar activos (JPQL con `eliminado = false`)
- Listar inactivos
- Baja lógica (`eliminado = true`)
- Alta lógica (reactivar registros)
- Manejo de transacciones y cierre de `EntityManager`

Este repositorio es la base para `CategoriaRepository` y `ProductoRepository`.

---

### 🗂️ 2. Gestión de Categorías

Incluye un menú completo con:

#### ✔️ Alta
- Solicita nombre y descripción  
- Valida duplicados  
- Permite reactivar categorías inactivas  
- Muestra ID generado  

#### ✔️ Baja lógica
- Marca la categoría como eliminada  
- No se borra físicamente  

#### ✔️ Modificación
- Muestra valores actuales  
- Permite mantener campos con ENTER  
- Valida nombre duplicado  

#### ✔️ Listado
- Muestra todas las categorías activas  

---

### 🛒 3. Gestión de Productos

Incluye:

#### ✔️ Alta
- Lista categorías activas  
- Solicita nombre, descripción, precio y stock  
- Valida precio > 0 y stock ≥ 0  
- Valida duplicados y permite reactivar productos inactivos  

#### ✔️ Baja lógica
- Marca el producto como eliminado  

#### ✔️ Modificación
- Muestra valores actuales  
- Permite mantener campos con ENTER  
- Valida precio y stock  

#### ✔️ Listado
- Muestra todos los productos activos  
- Incluye categoría asociada  

---

### 🔎 4. Reporte JPQL – Productos por Categoría

El menú de reportes permite:

- Listar categorías activas  
- Seleccionar una categoría por ID  
- Mostrar productos activos de esa categoría  
- Informar explícitamente si no existen productos asociados  

Consulta implementada en `ProductoRepository`:

```java
String jpql = "SELECT p FROM Producto p " +
        "WHERE p.categoria.id = :categoriaId " +
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
│   └── ProductoRepository.java
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

- Gestión de categorías  
- Gestión de productos  
- Reportes  
- Salir  

Cada submenú guía al usuario paso a paso con validaciones y mensajes claros, mostrando siempre mensajes de error cuando el ID no existe o el registro está dado de baja, y confirmaciones cuando las operaciones se realizan correctamente.

---

## 🎥 Video de presentación

Link al video:  https://www.youtube.com/watch?v=7qWdzV3JqFE

### Proyecto realizado por Matias Carro