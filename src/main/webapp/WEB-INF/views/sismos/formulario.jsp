<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><c:out value="${modoEdicion ? 'Editar sismo' : 'Nuevo sismo'}"/> · Sistema de Gestión de Sismos</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="topbar"><div class="topbar-in"><a class="brand" href="${pageContext.request.contextPath}/sismos"><svg viewBox="0 0 32 32" aria-hidden="true"><rect width="32" height="32" rx="3" fill="#2C5F9E"/><polyline points="3,17 9,17 12,8 16,25 20,12 23,17 29,17" fill="none" stroke="#fff" stroke-width="2" stroke-linejoin="round" stroke-linecap="round"/></svg><span class="brand-name">CSSP</span><span class="brand-sub">Sistema de Gestión de Sismos</span></a><div class="userbox">Módulo Sismos · Primera entrega</div></div></header>
<main class="page narrow">
<div class="crumbs"><a href="${pageContext.request.contextPath}/sismos">Sismos</a> /
    <c:choose>
        <c:when test="${modoEdicion}">Editar <c:out value="${codigoIngresado}"/></c:when>
        <c:otherwise>Nuevo sismo</c:otherwise>
    </c:choose>
</div>
<div class="page-head">
    <div><h1><c:out value="${modoEdicion ? 'Editar sismo' : 'Nuevo sismo'}"/></h1>
    <p class="lead"><c:out value="${modoEdicion ? 'Modifique los datos que necesite corregir' : 'Complete los datos del reporte'}"/></p></div>
</div>

<div class="card">
<div class="card-h">Datos del sismo <small>(*) Campo obligatorio</small></div>
<div class="card-b">

<c:if test="${not empty error}">
    <div class="alert err"><b>Zona de mensajes de validación:</b> <c:out value="${error}"/></div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}${modoEdicion ? '/sismos/editar' : '/sismos/nuevo'}" class="form-grid">

    <c:choose>
        <c:when test="${modoEdicion}">
            <div class="field">
                <label>Código<span class="req">*</span></label>
                <input type="text" value="<c:out value='${codigoIngresado}'/>" readonly>
                <input type="hidden" name="codigo" value="<c:out value='${codigoIngresado}'/>">
                <span class="hint">El código no se puede modificar.</span>
            </div>
        </c:when>
        <c:otherwise>
            <div class="field">
                <label>Código<span class="req">*</span></label>
                <input type="text" name="codigo" value="<c:out value='${codigoIngresado}'/>" placeholder="Ej. SIS006">
                <span class="hint">Identificador único del sismo.</span>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="field"><label>Fecha y hora<span class="req">*</span></label>
        <input type="datetime-local" name="fechaHora" value="<c:out value='${fechaHoraIngresada}'/>"></div>

    <div class="field"><label>Magnitud<span class="req">*</span></label>
        <input type="number" step="0.1" name="magnitud" value="<c:out value='${magnitudIngresada}'/>"></div>

    <div class="field"><label>Profundidad (km)<span class="req">*</span></label>
        <input type="number" step="0.1" name="profundidad" value="<c:out value='${profundidadIngresada}'/>"></div>

    <div class="field"><label>Latitud<span class="req">*</span></label>
        <input type="text" name="latitud" value="<c:out value='${latitudIngresada}'/>"></div>

    <div class="field"><label>Longitud<span class="req">*</span></label>
        <input type="text" name="longitud" value="<c:out value='${longitudIngresada}'/>"></div>

    <div class="field">
        <label>Distrito<span class="req">*</span></label>
        <select name="distritoId">
            <option value="">Seleccione un distrito</option>
            <c:forEach items="${distritos}" var="distrito">
                <option value="${distrito.id}" ${distrito.id == distritoIdIngresado ? 'selected' : ''}>
                    <c:out value="${distrito.nombre}, ${distrito.provincia.nombre}, ${distrito.provincia.departamento.nombre}"/></option>
            </c:forEach>
        </select>
    </div>

    <div class="field">
        <label>Estación<span class="req">*</span></label>
        <select name="estacionId">
            <option value="">Seleccione una estación</option>
            <c:forEach items="${estaciones}" var="estacion">
                <option value="${estacion.id}" ${estacion.id == estacionIdIngresada ? 'selected' : ''}>
                    <c:out value="${estacion.codigo} · ${estacion.nombre}"/></option>
            </c:forEach>
        </select>
    </div>

    <div class="field">
        <label>Estado<span class="req">*</span></label>
        <select name="estado">
            <option value="REGISTRADO" ${empty estadoIngresado || estadoIngresado == 'REGISTRADO' ? 'selected' : ''}>Registrado</option>
            <option value="EN_EVALUACION" ${estadoIngresado == 'EN_EVALUACION' ? 'selected' : ''}>En evaluación</option>
            <option value="EN_SEGUIMIENTO" ${estadoIngresado == 'EN_SEGUIMIENTO' ? 'selected' : ''}>En seguimiento</option>
            <option value="CERRADO" ${estadoIngresado == 'CERRADO' ? 'selected' : ''}>Cerrado</option>
        </select>
    </div>

    <div class="field full"><label>Referencia geográfica<span class="req">*</span></label>
        <input type="text" name="referencia" value="<c:out value='${referenciaIngresada}'/>" placeholder="Ej. A 20 km de la ciudad de..."></div>

    <div class="form-actions">
        <button type="submit" class="btn primary">Guardar</button>
        <a class="btn" href="${pageContext.request.contextPath}/sismos">Cancelar</a>
    </div>
</form>
</div>
</div>
</main>
</body>
</html>
