<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Departamentos · Sistema de Gestión de Sismos</title>
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
            <p class="lead">Departamentos</p></div>
    </div>

    <jsp:include page="_pestanas.jsp">
        <jsp:param name="activa" value="departamentos"/>
    </jsp:include>

    <c:if test="${param.resultado == 'registrado' || param.resultado == 'actualizado' || param.resultado == 'desactivado' || param.resultado == 'activado'}">
        <div class="alert ok">El departamento <c:out value="${param.nombre}"/> fue <c:out value="${param.resultado}"/> correctamente.</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert err"><c:out value="${error}"/></div>
    </c:if>

    <div class="card">
        <div class="card-h">Nuevo departamento</div>
        <div class="card-b">
            <form method="post" action="${pageContext.request.contextPath}/ubicaciones/departamentos/nuevo"
                  class="form-inline">
                <div class="field"><label>Nombre<span class="req">*</span></label>
                    <input type="text" name="nombre" value="<c:out value='${nombreIngresado}'/>" placeholder="Ej. Arequipa"></div>
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
                    <th>Departamento</th>
                    <th class="num">Provincias</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${departamentos}" var="departamento">
                    <tr>
                        <td><c:out value="${departamento.nombre}"/></td>
                        <td class="num"><c:out
                                value="${empty provinciasPorDepartamento[departamento.id] ? 0 : provinciasPorDepartamento[departamento.id]}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${departamento.estado == 'ACTIVO'}"><span
                                        class="badge activa">Activo</span></c:when>
                                <c:otherwise><span class="badge inactiva">Inactivo</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="btn-row">
                                <a class="btn sm"
                                   href="${pageContext.request.contextPath}/ubicaciones/departamentos/editar?id=${departamento.id}">Editar</a>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/ubicaciones/departamentos/estado">
                                    <input type="hidden" name="id" value="${departamento.id}">
                                    <button type="submit"
                                            class="btn sm ${departamento.estado == 'ACTIVO' ? 'danger' : ''}"><c:out
                                            value="${departamento.estado == 'ACTIVO' ? 'Desactivar' : 'Activar'}"/></button>
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
