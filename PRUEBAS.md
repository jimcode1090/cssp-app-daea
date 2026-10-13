# Pruebas integradas: 18 historias de la segunda entrega

Aquí está el recorrido de los criterios de aceptación de las 18 historias, probado sobre la aplicación desplegada en GlassFish 7.1.1 con MySQL (13 de octubre de 2026, con todo lo de las ramas ya fusionado a `main`).

Cómo se probó: se recorrió cada pantalla haciendo lo mismo que haría una persona (llenar formularios, pulsar botones, abrir enlaces) y, cuando el criterio habla de datos guardados, se confirmó además en la base de datos. Se crearon datos propios de prueba y se borraron al terminar, así que no quedó ningún resto.

## Sismos

| Historia | Criterio | Resultado |
|---|---|---|
| US-11 | CA1: título "Listado de sismos", botón "Nuevo sismo" y tabla con código, fecha y hora, magnitud, profundidad, departamento y estado | Cumple |
| US-11 | CA2: cada fila tiene los botones Ver, Editar y Eliminar | Cumple |
| US-11 | CA3: sin sismos, la tabla se reemplaza por "No hay sismos registrados" y el botón "Nuevo sismo" sigue visible | Cumple |
| US-11 | CA4: un sismo eliminado no aparece en el listado | Cumple |
| US-01 | CA1: el formulario tiene código, fecha y hora, magnitud, profundidad, latitud, longitud, ubicación, estación, referencia y estado | Cumple |
| US-01 | CA2: al guardar, el sismo aparece en el listado y se muestra su detalle con "El sismo SIS950 fue registrado correctamente" | Cumple |
| US-01 | CA3: un dato fuera de las reglas muestra su mensaje y no guarda (magnitud 0, código repetido, fecha futura) | Cumple |
| US-12 | CA1: el detalle muestra código, fecha y hora, magnitud, profundidad, latitud, longitud, departamento, provincia, distrito, estación y estado | Cumple |
| US-12 | CA2: botones Editar, Eliminar y Volver | Cumple |
| US-12 | CA3: "Volver" lleva al listado sin tocar ningún dato | Cumple |
| US-21 | CA1: el formulario carga los valores actuales y el código aparece bloqueado | Cumple |
| US-21 | CA2: al guardar, el valor nuevo se ve en la base de datos, en el detalle y en el listado | Cumple |
| US-21 | CA3: un campo vacío o una magnitud igual a cero muestra el mensaje, y los cambios no se guardan | Cumple |
| US-22 | CA1: la confirmación muestra el código, la magnitud y "Esta acción no se puede deshacer" | Cumple |
| US-22 | CA2: botones Confirmar y Cancelar | Cumple |
| US-22 | CA3: al confirmar, el sismo sale del listado y se muestra "El sismo SIS950 fue eliminado correctamente". En MySQL la fila sigue ahí, solo cambia a inactiva | Cumple |
| US-22 | CA4: "Cancelar" vuelve al detalle y el sismo sigue activo | Cumple |

Un sismo eliminado da 404 si se intenta abrir su detalle.

## Ubicación y estación dentro del sismo

| Historia | Criterio | Resultado |
|---|---|---|
| US-28 | CA1: al elegir un departamento el sistema entrega solo sus provincias, y al elegir una provincia, solo sus distritos | Cumple (falta verlo en el navegador, ver al final) |
| US-28 | CA2: ubicación incompleta, o un distrito que no es de la provincia elegida, muestra "Seleccione una ubicación válida" y no guarda | Cumple |
| US-28 | CA3: las listas solo incluyen elementos activos | Cumple |
| US-17 | CA1: la lista "Estación" muestra solo las estaciones activas | Cumple |
| US-17 | CA2: el detalle del sismo muestra el código y el nombre de la estación | Cumple |
| US-17 | CA3: asociar una estación inactiva muestra "La estación seleccionada está inactiva" | Cumple |

## Estaciones

| Historia | Criterio | Resultado |
|---|---|---|
| US-03 | CA1: el formulario tiene código, nombre, departamento, provincia, latitud, longitud, fecha de instalación y estado | Cumple |
| US-03 | CA1: al elegir un departamento, la lista de provincias muestra solo las de ese departamento; una provincia que no es de ese departamento, o dejarlo sin elegir, muestra "Seleccione una ubicación válida" | Cumple (falta verlo en el navegador, ver al final) |
| US-03 | CA2: al guardar con datos válidos, la estación aparece en el listado | Cumple |
| US-03 | CA3: código repetido muestra "Ya existe una estación con el código EST950" | Cumple |
| US-03 | CA4: la lista de provincias solo muestra las activas | Cumple |
| US-03 | Reglas: latitud fuera de rango y campo vacío muestran sus mensajes | Cumple |
| US-18 | CA1: la tabla muestra código, nombre, departamento, fecha de instalación y estado | Cumple |
| US-18 | CA2: "Ver" muestra los datos de la estación y la lista de sus sismos | Cumple |
| US-29 | CA1: el formulario carga los datos actuales con el código bloqueado | Cumple |
| US-29 | CA2: al guardar, el listado muestra los datos nuevos; con un campo vacío muestra "Todos los campos son obligatorios." y no guarda | Cumple |
| US-04 | CA1: "Desactivar" deja la estación en "Inactiva" y el botón pasa a "Activar" | Cumple |
| US-04 | CA2: una estación inactiva no aparece en la lista de estaciones al registrar un sismo | Cumple |

## Ubicaciones

| Historia | Criterio | Resultado |
|---|---|---|
| US-19 | CA1: al agregar un departamento, aparece en la lista | Cumple |
| US-19 | CA2: nombre repetido muestra "El departamento ya existe" | Cumple |
| US-19 | CA3: desactivar un departamento sin provincias lo deja inactivo, el botón pasa a "Activar" y deja de aparecer en las listas de selección | Cumple |
| US-19 | CA4: un departamento con provincias activas no se desactiva y muestra "El departamento tiene provincias asociadas" | Cumple |
| US-19 | CA5: "Activar" lo devuelve a activo y vuelve a aparecer en las listas | Cumple |
| US-08 | CA1: el formulario muestra el nombre actual | Cumple |
| US-08 | CA2: el nombre nuevo aparece en la lista y sus provincias siguen asociadas | Cumple |
| US-08 | CA3 y CA4: nombre de otro departamento muestra "El departamento ya existe"; vacío muestra "Todos los campos son obligatorios." | Cumple |
| US-26 | CA1: al agregar una provincia se elige su departamento y aparece asociada a él | Cumple |
| US-26 | CA2: el mismo nombre en el mismo departamento muestra "La provincia ya existe en este departamento"; en otro departamento sí se permite | Cumple |
| US-09 | CA1: el formulario muestra el nombre y el departamento actuales | Cumple |
| US-09 | CA2: los cambios aparecen en la lista y sus distritos siguen asociados | Cumple |
| US-09 | CA3 y CA4: nombre repetido en el departamento y campo vacío muestran sus mensajes | Cumple |
| US-49 | CA1: desactivar una provincia sin distritos ni estaciones la deja inactiva y sale de las listas | Cumple |
| US-49 | CA2: con distritos activos muestra "La provincia tiene distritos asociados" | Cumple |
| US-49 | CA3: con estaciones activas muestra "La provincia tiene estaciones asociadas" | Cumple |
| US-49 | CA4: "Activar" la devuelve a activa | Cumple |
| US-27 | CA1: al agregar un distrito se elige su provincia y aparece asociado a ella | Cumple |
| US-27 | Regla: el mismo nombre en la misma provincia muestra "El distrito ya existe en esta provincia" | Cumple |
| US-27 | CA2: desactivar un distrito sin sismos lo deja inactivo | Cumple |
| US-27 | CA3: un distrito con sismos no se desactiva y muestra "El distrito está asociado a sismos registrados" | Cumple |
| US-27 | CA4: "Activar" lo devuelve a activo | Cumple |
| US-10 | CA1 a CA4: igual que provincias, con el nombre y la provincia del distrito | Cumple |

## Lo que falta revisar a mano

- **Las listas que se llenan solas (US-28 y US-03).** Al elegir un departamento, la lista de provincias debería llenarse sola con las de ese departamento, y en el sismo lo mismo con los distritos al elegir una provincia. Eso hay que mirarlo en el navegador: entra a "Nuevo sismo" o a "Nueva estación" y elige un departamento. Lo que sí está comprobado es que el sistema entrega las provincias y distritos correctos, y que al editar un sismo las tres listas ya aparecen con su ubicación.
- **La página de error ("Algo salió mal").** Solo aparece cuando falla la base de datos, y no se provocó un fallo para verla.
- **Cómo se ven las pantallas.** Se revisó el contenido de cada página y la alineación de los formularios con capturas, pero no hay una prueba que diga si algo "se ve bien".
