# Pruebas integradas — 8 historias del CRUD de Sismo

Verificación de los criterios de aceptación de las 8 historias de esta entrega, ejecutada sobre `cssp-app-dev` con las 13 ramas ya fusionadas a `main`.

| Historia | Criterio | Resultado |
|---|---|---|
| US-11 | CA1: título, botón "Nuevo sismo" y tabla con las 6 columnas | Cumple |
| US-11 | CA2: botones Ver, Editar y Eliminar por fila | Cumple |
| US-11 | CA3: mensaje "No hay sismos registrados" si la tabla está vacía | Cumple |
| US-01 | CA1: formulario con los 9 campos al pulsar "Nuevo sismo" | Cumple |
| US-01 | CA2: el sismo aparece en el listado tras guardar | Cumple |
| US-02 | CA1: mensaje "Todos los campos son obligatorios." si hay campos vacíos | Cumple |
| US-02 | CA2: el sismo no aparece en el listado mientras falten campos | Cumple |
| US-31 | CA1: mensaje de código duplicado al repetir un código existente | Cumple |
| US-31 | CA2: un código nuevo se acepta y se guarda | Cumple |
| US-41 | CA1: magnitud ≤ 0 rechazada | Cumple |
| US-41 | CA2: profundidad negativa rechazada | Cumple |
| US-41 | CA3: estado fuera de la lista permitida rechazado | Cumple |
| US-12 | CA1: detalle muestra todos los datos del sismo | Cumple |
| US-12 | CA2: botones Editar, Eliminar y Volver | Cumple |
| US-12 | CA3: "Volver" regresa al listado sin modificar datos | Cumple |
| US-21 | CA1: formulario de edición con datos actuales y código de solo lectura | Cumple |
| US-21 | CA2: el listado y el detalle muestran el valor actualizado | Cumple |
| US-21 | CA3: error de validación al editar no guarda los cambios | Cumple |
| US-22 | CA1: confirmación con código, fecha, magnitud y advertencia | Cumple |
| US-22 | CA2: botones Confirmar y Cancelar | Cumple |
| US-22 | CA3: al confirmar, el sismo se elimina y se muestra el mensaje | Cumple |
| US-22 | CA4: al cancelar, el sismo sigue apareciendo | Cumple |

Fuera de alcance de esta entrega (Fase 2 del backlog, comentarios `TODO` en el código): validación de coordenadas geográficas (US-03) y de fecha no futura (US-04).
