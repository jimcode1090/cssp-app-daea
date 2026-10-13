<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Editar departamento · Sistema de Gestión de Sismos</title>
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
    <jsp:param name="activa" value="ubicaciones"/>
</jsp:include>
<main class="page narrow">
    <div class="crumbs"><a href="${pageContext.request.contextPath}/ubicaciones/departamentos">Departamentos</a> /
        Editar
    </div>
    <div class="page-head">
        <div><h1>Editar departamento</h1>
            <p class="lead">Modifique el nombre del departamento</p></div>
    </div>

    <div class="card">
        <div class="card-h">Datos del departamento <small>(*) Campo obligatorio</small></div>
        <div class="card-b">

            <c:if test="${not empty error}">
                <div class="alert err"><b>Zona de mensajes de validación:</b> <c:out value="${error}"/></div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/ubicaciones/departamentos/editar"
                  class="form-grid">
                <input type="hidden" name="id" value="${departamento.id}">
                <div class="field"><label>Nombre<span class="req">*</span></label>
                    <input type="text" name="nombre" value="<c:out value='${departamento.nombre}'/>"></div>
                <div class="form-actions">
                    <button type="submit" class="btn primary">Guardar</button>
                    <a class="btn" href="${pageContext.request.contextPath}/ubicaciones/departamentos">Cancelar</a>
                </div>
            </form>
        </div>
    </div>
</main>
</body>
</html>
