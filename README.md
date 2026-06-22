# TPI Programacion II - Food Store

Sistema de gestion de pedidos de comida desarrollado como Trabajo Practico Integrador de la materia **Programacion II** de la Tecnicatura Universitaria en Programacion.

## Descripcion

Food Store es una aplicacion de consola que permite gestionar el catalogo de productos de un negocio de comidas. El sistema implementa operaciones CRUD completas para categorias, productos, usuarios y pedidos, aplicando Programacion Orientada a Objetos, manejo de excepciones propias y almacenamiento en memoria mediante colecciones.

## Caracteristicas principales

### Epica 1 - Gestion de Categorias
- Listar categorias (solo no eliminadas, con id, nombre y descripcion)
- Crear categorias con validacion de nombre unico
- Editar categorias (nombre y/o descripcion)
- Eliminacion logica (soft delete) con impedimento si tiene productos activos

### Epica 2 - Gestion de Productos
- Listar productos (filtro opcional por categoria, muestra id, nombre, precio, stock, categoria)
- Crear productos con validaciones (precio >= 0, stock >= 0, nombre no vacio, categoria existente)
- Editar productos (todos los campos, mantiene los anteriores si se dejan vacios)
- Eliminacion logica preservando integridad de pedidos historicos

### Epica 3 - Gestion de Usuarios
- Listar usuarios (solo no eliminados, con id, nombre, apellido, mail, rol)
- Crear usuarios con validacion de mail unico y formato basico
- Editar usuarios (todos los campos, validacion de mail unico al modificar)
- Eliminacion logica (impide usar el usuario en nuevos pedidos, pero mantiene historial)

### Epica 4 - Gestion de Pedidos y Detalles
- Listar pedidos (filtro opcional por usuario, muestra id, usuario, estado, forma de pago, total y fecha)
- Crear pedidos con detalles multiples (atomicidad: si falla un detalle, no se crea el pedido)
- Ver detalles de un pedido especifico
- Actualizar estado y forma de pago con reglas de transicion de estados
- Eliminacion logica en cascada (pedido y sus detalles)

## Tecnologias utilizadas

- **Java 21**
- **Programacion Orientada a Objetos** (herencia, encapsulamiento, polimorfismo, interfaces)
- **Colecciones** (`ArrayList`, `Stream` para filtrados)
- **Programacion funcional y lambdas** (expresiones lambda en filtros y validaciones)
- **Excepciones personalizadas**
- **Validaciones centralizadas** (clase `Utilitario` con metodos reutilizables)
- **Arquitectura en capas** (entidades, servicios, UI, utilidades)

## Estructura del proyecto
```text
src/integrado/prog2/
├── Main.java
├── entities/
│   ├── Base.java
│   ├── Categoria.java
│   ├── Producto.java
│   ├── Usuario.java
│   ├── Pedido.java
│   └── DetallePedido.java
├── enums/
│   ├── Rol.java
│   ├── Estado.java
│   └── FormaPago.java
├── exceptions/
│   ├── TextoInvalidoException.java
│   ├── NumeroNegativoException.java
│   ├── NumeroNoPositivoException.java
│   ├── ValidacionException.java
│   ├── EntidadNoEncontradaException.java
│   └── MailDuplicadoException.java
├── interfaces/
│   ├── Calculable.java
│   └── ValidableMail.java
├── services/
│   ├── Crud_Generico/
│   │   ├── CrudService.java
│   │   └── CrudGenerico.java
│   ├── ServicioCategoria.java
│   ├── ServicioProducto.java
│   ├── ServicioUsuario.java
│   └── ServicioPedido.java
├── ui/
│   ├── Menu.java
│   ├── MenuCategoria.java
│   ├── MenuProducto.java
│   ├── MenuUsuario.java
│   └── MenuPedido.java
└── utils/
    ├── Utilitario.java
    └── DataSeed.java
```

## Arquitectura en capas
El proyecto separa responsabilidades en las siguientes capas:
- **UI**: interaccion por consola (menus y entrada/salida).
- **Services**: logica de negocio, operaciones CRUD y validaciones complejas.
- **Entities**: modelo de dominio con herencia, relaciones y validaciones basicas.
- **Interfaces**: contratos para comportamientos comunes.
- **Exceptions**: excepciones propias para validacion, unicidad y entidades no encontradas.
- **Utils**: metodos estaticos reutilizables (validaciones de texto, numeros, mail) y carga de datos de prueba.

## Requisitos
- Java 21 o superior
- Maven 3.9+

## Como ejecutar - Con Maven (recomendado)
1. Clonar el repositorio:
```bash
git clone https://github.com/SantinoNicolasBoscatto/Food-Store-TPI-Programacion-II.git
```

2. Ingresar al directorio del proyecto:
```bash
cd TPI-Food\ Store/
```

3. Compilar desde la raiz del proyecto:
```bash
mvn clean compile
```

4. Ejecutar la aplicacion:
```bash
java -cp target/classes integrado.prog2.Main
```


El sistema se inicia con datos de prueba precargados (DataSeed) para facilitar la verificacion de todas las funcionalidades.

## Pruebas rapidas con DataSeed
Al ejecutar el programa, se cargan automaticamente datos de prueba. Para verificar las funcionalidades principales:

### Categorias
Ir a 1. Categorias > 
1. Listar => debe mostrar Bebidas, Pizzas, Postres.
2. Crear => probar nombre duplicado (ej: Bebidas) => debe mostrar error.
3. Editar => modificar el nombre de una categoria existente.
4. Eliminar => intentar borrar Bebidas (tiene productos) => debe mostrar error.

### Productos
Ir a 2. Productos > 
1. Listar => elegir filtro S, categoria 1 => solo productos de Bebidas.
2. Crear => ingresar precio -500 => debe mostrar error de valor negativo.
3. Editar => cambiar el precio de un producto y verificar la actualizacion.
4. Eliminar => eliminar un producto sin pedidos y verificar que desaparece del listado.

### Usuarios
Ir a 3. Usuarios > 
1. Listar => aparecen tres usuarios cargados.
2. Crear => ingresar un mail ya existente => debe mostrar error de duplicado.
3. Editar => cambiar el rol de un usuario.
4. Eliminar => eliminar un usuario y luego intentar usarlo en un nuevo pedido => debe mostrar error.

### Pedidos
Ir a 4. Pedidos > 
1. Listar => filtro por usuario N => muestra todos los pedidos.
2. Crear => ingresar un usuario valido (ej: 13), fecha (ej: 15/06/2026), estado, forma de pago y al menos un producto con su cantidad.
3. Editar => intentar pasar un pedido TERMINADO a PENDIENTE => debe mostrar error de transicion.
4. Eliminar => eliminar un pedido y confirmar con S => ya no aparece en el listado.
5. Ver detalles => elegir un ID de pedido valido => muestra sus items y subtotales.

## Reglas de negocio implementadas
- Soft delete: todas las eliminaciones son logicas (eliminado = true)

- Mail unico: validacion al crear y editar usuarios

- Nombre de categoria unico: validacion al crear y editar

- Precio y stock no negativos: validacion en creacion y edicion de productos

- Pedido sin usuario: no permitido

- Cantidad de detalle > 0: validacion en creacion de detalles

- Transiciones de estado: PENDIENTE -> CONFIRMADO/CANCELADO, CONFIRMADO -> TERMINADO/CANCELADO, TERMINADO y CANCELADO son estados finales

- Atomicidad en pedidos: si algun detalle falla, el pedido completo no se crea y no quedan datos inconsistentes

- Categoria con productos activos: no se puede eliminar

## Video
https://youtu.be/smmQAfSCGOQ

## Documentacion
El PDF se encuentra junto a este readme en GitHub