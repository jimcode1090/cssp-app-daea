<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Nuevo sismo · Sistema de Gestión de Sismos</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="topbar"><div class="topbar-in"><a class="brand" href="${pageContext.request.contextPath}/sismos"><svg viewBox="0 0 32 32" aria-hidden="true"><rect width="32" height="32" rx="3" fill="#2C5F9E"/><polyline points="3,17 9,17 12,8 16,25 20,12 23,17 29,17" fill="none" stroke="#fff" stroke-width="2" stroke-linejoin="round" stroke-linecap="round"/></svg><span class="brand-name">CSSP</span><span class="brand-sub">Sistema de Gestión de Sismos</span></a><div class="userbox">Módulo Sismos · Primera entrega</div></div></header>
<main class="page narrow">
<div class="crumbs"><a href="${pageContext.request.contextPath}/sismos">Sismos</a> / Nuevo sismo</div>
<div class="page-head">
    <div><h1>Nuevo sismo</h1><p class="lead">Complete los datos del reporte</p></div>
</div>

<div class="card">
<div class="card-h">Datos del sismo <small>(*) Campo obligatorio</small></div>
<div class="card-b">

<c:if test="${not empty error}">
    <div class="alert err"><b>Zona de mensajes de validación:</b> <c:out value="${error}"/></div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/sismos/nuevo" class="form-grid">

    <div class="field">
        <label>Código<span class="req">*</span></label>
        <input type="text" name="codigo" value="<c:out value='${codigoIngresado}'/>" placeholder="Ej. SIS006">
        <span class="hint">Identificador único del sismo.</span>
    </div>

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
        <label>Departamento<span class="req">*</span></label>
        <select name="departamento">
            <option value="">Seleccione</option>
            <option <c:if test="${departamentoIngresado == 'Amazonas'}">selected</c:if>>Amazonas</option>
            <option <c:if test="${departamentoIngresado == 'Áncash'}">selected</c:if>>Áncash</option>
            <option <c:if test="${departamentoIngresado == 'Apurímac'}">selected</c:if>>Apurímac</option>
            <option <c:if test="${departamentoIngresado == 'Arequipa'}">selected</c:if>>Arequipa</option>
            <option <c:if test="${departamentoIngresado == 'Ayacucho'}">selected</c:if>>Ayacucho</option>
            <option <c:if test="${departamentoIngresado == 'Cajamarca'}">selected</c:if>>Cajamarca</option>
            <option <c:if test="${departamentoIngresado == 'Cusco'}">selected</c:if>>Cusco</option>
            <option <c:if test="${departamentoIngresado == 'Huancavelica'}">selected</c:if>>Huancavelica</option>
            <option <c:if test="${departamentoIngresado == 'Huánuco'}">selected</c:if>>Huánuco</option>
            <option <c:if test="${departamentoIngresado == 'Ica'}">selected</c:if>>Ica</option>
            <option <c:if test="${departamentoIngresado == 'Junín'}">selected</c:if>>Junín</option>
            <option <c:if test="${departamentoIngresado == 'La Libertad'}">selected</c:if>>La Libertad</option>
            <option <c:if test="${departamentoIngresado == 'Lambayeque'}">selected</c:if>>Lambayeque</option>
            <option <c:if test="${departamentoIngresado == 'Lima'}">selected</c:if>>Lima</option>
            <option <c:if test="${departamentoIngresado == 'Loreto'}">selected</c:if>>Loreto</option>
            <option <c:if test="${departamentoIngresado == 'Madre de Dios'}">selected</c:if>>Madre de Dios</option>
            <option <c:if test="${departamentoIngresado == 'Moquegua'}">selected</c:if>>Moquegua</option>
            <option <c:if test="${departamentoIngresado == 'Pasco'}">selected</c:if>>Pasco</option>
            <option <c:if test="${departamentoIngresado == 'Piura'}">selected</c:if>>Piura</option>
            <option <c:if test="${departamentoIngresado == 'Puno'}">selected</c:if>>Puno</option>
            <option <c:if test="${departamentoIngresado == 'San Martín'}">selected</c:if>>San Martín</option>
            <option <c:if test="${departamentoIngresado == 'Tacna'}">selected</c:if>>Tacna</option>
            <option <c:if test="${departamentoIngresado == 'Tumbes'}">selected</c:if>>Tumbes</option>
            <option <c:if test="${departamentoIngresado == 'Ucayali'}">selected</c:if>>Ucayali</option>
        </select>
    </div>

    <div class="field">
        <label>Estado<span class="req">*</span></label>
        <select name="estado">
            <option <c:if test="${empty estadoIngresado || estadoIngresado == 'Registrado'}">selected</c:if>>Registrado</option>
            <option <c:if test="${estadoIngresado == 'En evaluación'}">selected</c:if>>En evaluación</option>
            <option <c:if test="${estadoIngresado == 'En seguimiento'}">selected</c:if>>En seguimiento</option>
            <option <c:if test="${estadoIngresado == 'Cerrado'}">selected</c:if>>Cerrado</option>
        </select>
    </div>

    <div class="field full"><label>Referencia geográfica<span class="req">*</span></label>
        <input type="text" name="referencia" value="<c:out value='${referenciaIngresada}'/>" placeholder="Ej. A 20 km de la ciudad de..."></div>

    <div class="form-actions">
        <button type="submit" class="btn primary">Guardar</button>
        <a class="btn" href="${pageContext.request.contextPath}/sismos">Cancelar</a>
    </div>
</form>
<p class="nota">La validación HTML ayuda al usuario; el Servlet vuelve a validar porque no debe confiar en el cliente.</p>
</div>
</div>
</main>
</body>
</html>
