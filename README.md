# Sistema de Gestión de Sismos (CSSP)

Proyecto del curso Desarrollo de Aplicaciones Empresariales Avanzado. Es un CRUD de la entidad Sismo, siguiendo el mismo patrón que vimos en clase con `erp-importadora`: Servlets + JSP con MVC, sin base de datos (todo vive en memoria mientras corre la app).

## Requisitos

- JDK 17+
- Maven 3.9+
- Apache Tomcat 11

## Cómo correrlo

```bash
mvn clean package
```

Copiar `target/sismos-peru.war` a la carpeta `webapps` de Tomcat y entrar a:

```
http://localhost:8080/sismos-peru/sismos
```

## Rutas

| Método | Ruta | Qué hace |
|---|---|---|
| GET | `/sismos` | Lista los sismos registrados |
| GET | `/sismos/nuevo` | Formulario de registro |
| POST | `/sismos/nuevo` | Valida y registra un sismo |
| GET | `/sismos/detalle?codigo=SIS001` | Detalle de un sismo |
| GET | `/sismos/editar?codigo=SIS001` | Formulario con los datos actuales |
| POST | `/sismos/editar` | Valida y actualiza un sismo |
| GET | `/sismos/eliminar?codigo=SIS001` | Confirmación antes de eliminar |
| POST | `/sismos/eliminar` | Elimina el sismo |

## Cómo está armado

Navegador → Servlet → request attributes → JSP → HTML. Nada más.

- `Sismo` es un record de Java con los datos.
- `SismoRepository` guarda todo en memoria con un `CopyOnWriteArrayList`.
- `AplicacionListener` arma el repositorio al arrancar la app y carga 5 sismos de ejemplo para no empezar con la lista vacía.
- Los Servlets reciben la petición, validan y le pasan los atributos a la vista.
- Las JSP están en `WEB-INF/views`, así que no se puede entrar a ellas directo por URL, siempre pasan por un Servlet primero. Usan EL y JSTL para pintar el HTML.

## Algunas decisiones que tomé (siguiendo el proyecto de referencia)

- Nada de scriptlets `<% %>` en las JSP.
- Todo lo que se imprime en pantalla pasa por `<c:out>` para evitar problemas de escape.
- El Servlet valida de nuevo los datos del POST aunque el formulario ya los valide en el navegador — nunca hay que confiar solo en el cliente.
- Si hay un error de validación se hace `forward` para no perder lo que el usuario ya escribió; si el registro o la edición sale bien, se usa Post/Redirect/Get.
- No hay base de datos a propósito, la idea de esta entrega era enfocarse en Servlets, JSP y el patrón MVC, no en persistencia.

## Qué entra en esta entrega

Se cubrieron 8 historias: US-11, US-01, US-02, US-31, US-41, US-12, US-21 y US-22 (el detalle está en `03_Product_Backlog_y_Plan_de_Desarrollo.md`, sección 3.3).

Las validaciones de coordenadas (US-03) y de fecha no futura (US-04) quedaron para la Fase 2 del backlog. Están marcadas como `TODO` en `SismoNuevoServlet` y `SismoEditarServlet` en lugar de implementarlas a medias, porque `05_Requisitos_Tecnicos_del_CRUD.md` no las pedía para esta fase.

El diseño de las vistas sale de los prototipos HTML en `04_Prototipos_de_Interfaces.md` (carpeta `prototipos-html`), pasados a JSP con EL y JSTL.

## Sobre las ramas

Todo el trabajo salió en 13 ramas, una por ticket, cada una fusionada a `main` cuando su PR se aprobaba. Si quieres ver el detalle rama por rama (comandos, archivos tocados, texto del PR), está en `guia-ramas-github/`.
