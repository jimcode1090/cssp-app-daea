<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Estación · Sistema de Gestión de Sismos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="topbar">
    <div class="topbar-in"><a class="brand" href="${pageContext.request.contextPath}/sismos">
        <svg viewBox="0 0 32 32" aria-hidden="true">
            <rect width="32" height="32" rx="3" fill="#2C5F9E"/>
            <polyline points="3,17 9,17 12,8 16,25 20,12 23,17 29,17" fill="none" stroke="#fff" stroke-width="2"
                      stroke-linejoin="round" stroke-linecap="round"/>
        </svg>
        <span class="brand-name">CSSP</span><span class="brand-sub">Sistema de Gestión de Sismos</span></a></div>
</header>
<jsp:include page="/WEB-INF/views/_menu.jsp">
    <jsp:param name="activa" value="estaciones"/>
</jsp:include>
<main class="page narrow">
    <div class="crumbs"><a href="${pageContext.request.contextPath}/estaciones">Estaciones</a> /
        <c:choose>
            <c:when test="${modoEdicion}">Editar <c:out value="${codigoIngresado}"/></c:when>
            <c:otherwise>Nueva estación</c:otherwise>
        </c:choose>
    </div>
    <div class="page-head">
        <div><h1><c:out value="${modoEdicion ? 'Editar estación' : 'Nueva estación'}"/></h1>
            <p class="lead"><c:out
                    value="${modoEdicion ? 'Modifique los datos que necesite corregir' : 'Registre los datos de la estación de monitoreo'}"/></p>
        </div>
    </div>

    <div class="card">
        <div class="card-h">Datos de la estación <small>(*) Campo obligatorio</small></div>
        <div class="card-b">

            <c:if test="${not empty error}">
                <div class="alert err"><c:out value="${error}"/></div>
            </c:if>

            <form method="post"
                  action="${pageContext.request.contextPath}${modoEdicion ? '/estaciones/editar' : '/estaciones/nuevo'}"
                  class="form-grid">

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
                            <input type="text" name="codigo" value="<c:out value='${codigoIngresado}'/>"
                                   placeholder="Ej. EST005">
                            <span class="hint">Identificador único de la estación.</span>
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="field"><label>Nombre<span class="req">*</span></label>
                    <input type="text" name="nombre" value="<c:out value='${nombreIngresado}'/>" placeholder="Ej. Estación Arequipa Centro"></div>

                <div class="field"><label>Departamento<span class="req">*</span></label>
                    <select id="departamentoId" name="departamentoId">
                        <option value="">Seleccione un departamento</option>
                        <c:forEach items="${departamentos}" var="departamento">
                            <option value="${departamento.id}" ${departamento.id == departamentoIdIngresado ? 'selected' : ''}>
                                <c:out value="${departamento.nombre}"/></option>
                        </c:forEach>
                    </select></div>

                <div class="field"><label>Provincia<span class="req">*</span></label>
                    <select id="provinciaId" name="provinciaId"
                            data-url="${pageContext.request.contextPath}/ubicaciones/provincias/lista">
                        <option value="">Seleccione una provincia</option>
                        <c:forEach items="${provincias}" var="provincia">
                            <option value="${provincia.id}" ${provincia.id == provinciaIdIngresada ? 'selected' : ''}>
                                <c:out value="${provincia.nombre}"/></option>
                        </c:forEach>
                    </select></div>

                <div class="field"><label>Latitud<span class="req">*</span></label>
                    <input type="number" step="any" name="latitud" value="<c:out value='${latitudIngresada}'/>" placeholder="Ej. -16.3988"></div>

                <div class="field"><label>Longitud<span class="req">*</span></label>
                    <input type="number" step="any" name="longitud" value="<c:out value='${longitudIngresada}'/>" placeholder="Ej. -71.5369">
                </div>

                <div class="field"><label>Fecha de instalación<span class="req">*</span></label>
                    <input type="date" name="fechaInstalacion" value="<c:out value='${fechaInstalacionIngresada}'/>">
                </div>

                <div class="field"><label>Estado<span class="req">*</span></label>
                    <select name="estado">
                        <option value="ACTIVO" ${empty estadoIngresado || estadoIngresado == 'ACTIVO' ? 'selected' : ''}>
                            Activa
                        </option>
                        <option value="INACTIVO" ${estadoIngresado == 'INACTIVO' ? 'selected' : ''}>Inactiva</option>
                    </select></div>

                <div class="form-actions">
                    <button type="submit" class="btn primary">Guardar</button>
                    <a class="btn" href="${pageContext.request.contextPath}/estaciones">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
</main>
<script src="${pageContext.request.contextPath}/js/ubicacion.js"></script>
</body>
</html>
