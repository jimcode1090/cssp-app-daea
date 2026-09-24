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

Se completan a medida que se van fusionando los Pull Requests de cada ticket (ver `guia-ramas-github` para el detalle rama por rama).

## Estructura de paquetes

- `pe.edu.sismos.model` — entidades.
- `pe.edu.sismos.repository` — acceso a los datos en memoria.
- `pe.edu.sismos.listener` — inicialización de la aplicación.
- `pe.edu.sismos.servlet` — controladores.
