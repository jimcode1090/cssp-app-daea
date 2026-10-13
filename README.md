# Sistema de Gestión de Sismos (CSSP)

Proyecto del curso Desarrollo de Aplicaciones Empresariales Avanzado. En la primera entrega era un CRUD de sismos con los datos en memoria. En esta segunda entrega pasó a guardar todo en una base de datos MySQL usando JPA, y creció con dos módulos nuevos: **Estaciones** de monitoreo y **Ubicaciones** (departamentos, provincias y distritos).

Sigue el mismo patrón que vimos en clase con `erp-importadora`: Servlets + JSP con MVC, y repositorios con `EntityManager`.

## Qué necesitas

- JDK 17
- Maven 3.9 o superior
- GlassFish 7.1 (con él probamos todo; trae EclipseLink como proveedor de JPA)
- MySQL 8

## Cómo ponerlo a andar

**1. Crear la base de datos.** En la carpeta `database/` están los dos scripts. Hay que ejecutarlos en este orden: primero `01_schema.sql` y después `02_datos_iniciales.sql`. Lo más cómodo es abrir cada archivo en MySQL Workbench y ejecutarlo. Si prefieres la consola, desde la carpeta del proyecto:

```bash
mysql -u root -p < database/01_schema.sql
mysql -u root -p < database/02_datos_iniciales.sql
```

Ese comando funciona en la terminal de Mac o Linux y en el símbolo del sistema (`cmd`) de Windows. En PowerShell el signo `<` no sirve; ahí usa Workbench.

El primero crea la base `bd_cssp_sismos` con sus tablas, y el segundo la llena con unos datos de ejemplo (5 departamentos, 7 distritos, 4 estaciones y 5 sismos). Ojo: el segundo script usa ids fijos, así que solo se puede cargar sobre una base vacía. Si necesitas repetirlo, borra la base y empieza otra vez desde el primero.

**2. Poner tus datos de conexión.** Abre `src/main/resources/db.properties` y cambia el usuario y la contraseña por los de tu MySQL. La URL ya apunta a `localhost:3306/bd_cssp_sismos`.

**3. Compilar.** Con Maven (`mvn clean package`) o, en NetBeans, con "Clean and Build". Genera `target/sismos-peru.war`.

**4. Desplegar.** En NetBeans, con GlassFish configurado, basta con "Run". Si prefieres la consola, con GlassFish encendido:

```bash
asadmin deploy target/sismos-peru.war
```

(en Windows el comando es `asadmin.bat`). También puedes subir el `.war` desde la consola de administración, en `http://localhost:4848`. Después entra a:

```
http://localhost:8080/sismos-peru/sismos
```

**Si algo falla al desplegar.** Si la aplicación abre pero, al guardar, sale el error `No suitable driver`, el servidor no está encontrando el driver de MySQL. Un `.war` es un archivo zip: ábrelo con cualquier descompresor (`target/sismos-peru.war`), busca `WEB-INF/lib/mysql-connector-j-9.6.0.jar` y copia ese archivo a la carpeta `glassfish/domains/domain1/lib` de GlassFish. Después reinicia GlassFish. A nosotros nos pasó una vez y con eso se arregló.

## Rutas

Las pantallas se mueven con el menú de arriba (Sismos, Estaciones y Ubicaciones), pero estas son todas las rutas.

**Sismos**: se identifican por su código.

| Método | Ruta | Qué hace |
|---|---|---|
| GET | `/sismos` | Lista los sismos activos |
| GET / POST | `/sismos/nuevo` | Formulario de registro / valida y guarda |
| GET | `/sismos/detalle?codigo=SIS001` | Detalle de un sismo |
| GET / POST | `/sismos/editar` | Formulario con los datos actuales / valida y actualiza |
| GET / POST | `/sismos/eliminar` | Pantalla de confirmación / da de baja el sismo |

**Estaciones**: también por código.

| Método | Ruta | Qué hace |
|---|---|---|
| GET | `/estaciones` | Lista las estaciones |
| GET / POST | `/estaciones/nuevo` | Formulario de registro / valida y guarda |
| GET | `/estaciones/detalle?codigo=EST001` | Datos de la estación y sus sismos |
| GET / POST | `/estaciones/editar` | Edición (el código no se puede cambiar) |
| POST | `/estaciones/estado` | Activa o desactiva la estación |

**Ubicaciones**: no tienen código, así que van por `id`.

| Método | Ruta | Qué hace |
|---|---|---|
| GET | `/ubicaciones/departamentos` | Lista y formulario para agregar |
| POST | `/ubicaciones/departamentos/nuevo` | Agrega un departamento |
| GET / POST | `/ubicaciones/departamentos/editar?id=1` | Edita el nombre |
| POST | `/ubicaciones/departamentos/estado` | Activa o desactiva |

Provincias (`/ubicaciones/provincias`) y distritos (`/ubicaciones/distritos`) tienen las mismas rutas. Además hay dos rutas que no muestran una página: entregan las provincias de un departamento (`/ubicaciones/provincias/lista?departamentoId=`) y los distritos de una provincia (`/ubicaciones/distritos/lista?provinciaId=`). Las usa el formulario de sismo para llenar sus listas encadenadas.

## Cómo está armado

Navegador → Servlet → Repositorio → MySQL, y de vuelta: el Servlet deja los datos como atributos de la petición y una JSP los pinta.

- `model`: las entidades JPA (`Departamento`, `Provincia`, `Distrito`, `Estacion` y `Sismo`).
- `repository`: un repositorio por entidad. Cada método abre su `EntityManager`, lo cierra al terminar, y las escrituras van dentro de una transacción con `rollback` si algo falla. Las consultas son JPQL y usan `JOIN FETCH` donde hace falta traer relaciones.
- `servlet`: reciben la petición, validan y preparan lo que ve la JSP.
- `config` y `listener`: `JpaUtil` crea la fábrica de `EntityManager` a partir de `db.properties`, y `JpaContextListener` la cierra cuando se retira la aplicación.
- Las JSP están en `WEB-INF/views`, así que no se entra a ellas directo por URL: siempre pasan por un Servlet. Usan EL y JSTL.

## La base de datos

Cinco tablas, con estas relaciones:

```
departamento 1 ── N provincia 1 ── N distrito 1 ── N sismo
                                  provincia 1 ── N estacion 1 ── N sismo
```

Todas las restricciones están en el script (`UNIQUE` en códigos y nombres, `CHECK` en estados y rangos, claves foráneas), y `persistence.xml` tiene `schema-generation = none`, así que JPA nunca crea ni modifica tablas: manda el script. Los nombres de `@Table` y `@Column` son los mismos que los del SQL.

## Algunas decisiones que tomamos

- **No se borra nada de verdad.** Departamentos, provincias, distritos y estaciones tienen un `estado` (activo o inactivo), y los sismos tienen `estado_registro`. "Eliminar" un sismo solo lo marca como inactivo: desaparece de las pantallas pero la fila sigue en la base de datos. Por eso el código de un sismo eliminado no se puede reutilizar.
- **Los estados se guardan como códigos** sin tildes (`REGISTRADO`, `EN_EVALUACION`, `ACTIVO`...) y las pantallas muestran el texto normal ("En evaluación").
- **Una ubicación o estación inactiva no se ofrece** en las listas de selección, y no se puede desactivar una ubicación que todavía tiene elementos activos debajo (por ejemplo, un distrito con sismos).
- **Las listas encadenadas** del formulario de sismo (departamento, provincia y distrito) son lo único que usa JavaScript: un pequeño script pide esas dos rutas y rellena la lista siguiente al elegir en la anterior. Todo lo demás es Servlet y JSP.
- Nada de scriptlets `<% %>` en las JSP, y todo lo que se imprime pasa por `<c:out>`.
- El servidor vuelve a validar todo lo que llega por POST, aunque el navegador ya lo haya validado.
- Si un formulario tiene errores, se vuelve a mostrar con lo que el usuario ya había escrito. Si todo sale bien, se redirige (Post/Redirect/Get) para que recargar la página no repita la operación.

## Qué entra en esta entrega

Son 18 historias:

| Módulo | Historias |
|---|---|
| Sismos | US-11 listado, US-01 registrar, US-12 detalle, US-21 editar, US-22 eliminar |
| Estaciones | US-03 registrar, US-18 listado y detalle, US-29 editar, US-04 activar o desactivar, US-17 asociar una estación al sismo |
| Ubicaciones | US-19 departamentos, US-26 provincias, US-27 distritos, US-08, US-09 y US-10 editarlos, US-49 desactivar provincias, US-28 elegir una ubicación válida en el sismo |

Las validaciones (campos obligatorios, código repetido, magnitud, profundidad, coordenadas, fecha no futura...) ya no son historias aparte sino reglas de negocio que aplican a las historias de arriba.

Una diferencia con lo que pedía la historia US-03: el formulario de estación tiene un solo selector "Provincia (Departamento)" en vez de dos listas separadas.

## Pruebas

En `PRUEBAS.md` está el recorrido de todos los criterios de aceptación con su resultado.

## Sobre las ramas

El trabajo se hizo en ramas por bloque (la base de datos, la base de JPA, cada módulo, el menú, los mensajes...), y cada una se fusionó a `main` con su pull request.
