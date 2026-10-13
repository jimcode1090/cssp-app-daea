<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Distritos · Sistema de Gestión de Sismos</title>
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
<main class="page">
    <div class="page-head">
        <div><h1>Ubicaciones geográficas</h1>
            <p class="lead">Distritos</p></div>
    </div>

    <jsp:include page="_pestanas.jsp">
        <jsp:param name="activa" value="distritos"/>
    </jsp:include>

    <c:if test="${not empty error}">
        <div class="alert err"><b>Zona de mensajes de validación:</b> <c:out value="${error}"/></div>
    </c:if>

    <div class="card">
        <div class="card-h">Nuevo distrito</div>
        <div class="card-b">
            <form method="post" action="${pageContext.request.contextPath}/ubicaciones/distritos/nuevo"
                  class="form-grid">
                <div class="field"><label>Provincia<span class="req">*</span></label>
                    <select name="provinciaId">
                        <option value="">Seleccione una provincia</option>
                        <c:forEach items="${provincias}" var="provincia">
                            <option value="${provincia.id}" ${provincia.id == provinciaIdIngresada ? 'selected' : ''}>
                                <c:out value="${provincia.nombre} (${provincia.departamento.nombre})"/></option>
                        </c:forEach>
                    </select></div>
                <div class="field"><label>Nombre<span class="req">*</span></label>
                    <input type="text" name="nombre" value="<c:out value='${nombreIngresado}'/>"></div>
                <div class="form-actions">
                    <button type="submit" class="btn primary">Agregar</button>
                </div>
            </form>
        </div>
    </div>

    <div class="card">
        <div class="card-b flush">
            <table class="data">
                <thead>
                <tr>
                    <th>Distrito</th>
                    <th>Provincia</th>
                    <th>Departamento</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${distritos}" var="distrito">
                    <tr>
                        <td><c:out value="${distrito.nombre}"/></td>
                        <td><c:out value="${distrito.provincia.nombre}"/></td>
                        <td><c:out value="${distrito.provincia.departamento.nombre}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${distrito.estado == 'ACTIVO'}"><span
                                        class="badge activa">Activo</span></c:when>
                                <c:otherwise><span class="badge inactiva">Inactivo</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="btn-row">
                                <a class="btn sm"
                                   href="${pageContext.request.contextPath}/ubicaciones/distritos/editar?id=${distrito.id}">Editar</a>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/ubicaciones/distritos/estado">
                                    <input type="hidden" name="id" value="${distrito.id}">
                                    <button type="submit"
                                            class="btn sm ${distrito.estado == 'ACTIVO' ? 'danger' : ''}"><c:out
                                            value="${distrito.estado == 'ACTIVO' ? 'Desactivar' : 'Activar'}"/></button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</main>
</body>
</html>
