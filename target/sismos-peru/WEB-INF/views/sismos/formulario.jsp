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
            <option>Amazonas</option>
            <option>Áncash</option>
            <option>Apurímac</option>
            <option>Arequipa</option>
            <option>Ayacucho</option>
            <option>Cajamarca</option>
            <option>Cusco</option>
            <option>Huancavelica</option>
            <option>Huánuco</option>
            <option>Ica</option>
            <option>Junín</option>
            <option>La Libertad</option>
            <option>Lambayeque</option>
            <option>Lima</option>
            <option>Loreto</option>
            <option>Madre de Dios</option>
            <option>Moquegua</option>
            <option>Pasco</option>
            <option>Piura</option>
            <option>Puno</option>
            <option>San Martín</option>
            <option>Tacna</option>
            <option>Tumbes</option>
            <option>Ucayali</option>
        </select>
    </div>

    <div class="field">
        <label>Estado<span class="req">*</span></label>
        <select name="estado">
            <option>Registrado</option>
            <option>En evaluación</option>
            <option>En seguimiento</option>
            <option>Cerrado</option>
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
