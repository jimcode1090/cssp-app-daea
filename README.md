# cssp-app-dev — Sistema de Gestión de Sismos (repositorio de práctica)

> **Repositorio de práctica.** Este proyecto es una simulación para aprender el flujo de ramas y Pull Requests del curso Desarrollo de Aplicaciones Empresariales Avanzado antes de aplicarlo en el repositorio real. No está vinculado al repositorio del curso ni a la entrega oficial.

CRUD de la entidad Sismo del Sistema de Seguimiento y Gestión de Sismos del CSSP. Construido con el mismo patrón trabajado en clase (proyecto `erp-importadora`): Servlets + JSP bajo MVC, sin base de datos.

## Requisitos

- JDK 17 o superior.
- Maven 3.9 o superior.
- Apache Tomcat 11.

## Ejecutar

```bash
mvn clean package
```

Copiar `target/sismos-peru.war` a `webapps` de Tomcat y abrir:

```text
http://localhost:8080/sismos-peru/sismos
```

## Rutas

| Método | Ruta | Responsabilidad |
|---|---|---|
| GET | `/sismos` | Listar los sismos registrados |
| GET | `/sismos/nuevo` | Mostrar el formulario de registro |
| POST | `/sismos/nuevo` | Validar y registrar un sismo |
| GET | `/sismos/detalle?codigo=SIS001` | Mostrar el detalle de un sismo |
| GET | `/sismos/editar?codigo=SIS001` | Mostrar el formulario con los datos actuales |
| POST | `/sismos/editar` | Validar y actualizar un sismo |
| GET | `/sismos/eliminar?codigo=SIS001` | Mostrar la confirmación de eliminación |
| POST | `/sismos/eliminar` | Eliminar el sismo |

## Flujo MVC

```text
Navegador -> Servlet controlador -> request attributes -> JSP vista -> HTML
```

- `Sismo` representa los datos (record de Java).
- `SismoRepository` mantiene los datos temporales en memoria (`CopyOnWriteArrayList`).
- `AplicacionListener` crea el repositorio compartido y carga 5 sismos de ejemplo al iniciar.
- Los Servlets reciben la petición, validan y preparan los atributos.
- Las JSP presentan el HTML con Expression Language y JSTL, y están en `WEB-INF/views`, por lo que solo se llega a ellas a través de un Servlet.

## Decisiones seguidas del proyecto de referencia

- No se usan scriptlets `<% ... %>`.
- Se usa `<c:out>` para escapar los datos que se muestran.
- El Servlet vuelve a validar los datos recibidos por POST, aunque el formulario HTML también los verifique.
- Un error de formulario se muestra con `forward` (conservando lo escrito); un registro o edición correcta usa Post/Redirect/Get.
- No hay base de datos: el repositorio en memoria permite concentrarse en Servlets, JSP y MVC.

## Alcance de esta entrega

Las 8 historias trabajadas son US-11, US-01, US-02, US-31, US-41, US-12, US-21 y US-22 (ver `03_Product_Backlog_y_Plan_de_Desarrollo.md`, sección 3.3). Las validaciones de coordenadas geográficas (US-03) y de fecha no futura (US-04) están planificadas para la Fase 2 del backlog y quedan como comentarios `TODO` en `SismoNuevoServlet` y `SismoEditarServlet`, para no generar una función no documentada en `05_Requisitos_Tecnicos_del_CRUD.md`.

Las vistas usan el diseño de los prototipos HTML de `04_Prototipos_de_Interfaces.md` (carpeta `prototipos-html`), adaptado a JSP con EL y JSTL.

## Cómo se construyó este repositorio de práctica

El código se desarrolló en 13 ramas, una por ticket, fusionadas en orden a `main` a medida que cada Pull Request se aprobaba. El detalle de cada rama (comandos, archivos y texto del PR) está en `guia-ramas-github/`.
