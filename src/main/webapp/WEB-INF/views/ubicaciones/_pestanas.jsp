<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<div class="tabs">
    <a class="${param.activa == 'departamentos' ? 'on' : ''}"
       href="${pageContext.request.contextPath}/ubicaciones/departamentos">Departamentos</a>
    <a class="${param.activa == 'provincias' ? 'on' : ''}"
       href="${pageContext.request.contextPath}/ubicaciones/provincias">Provincias</a>
    <a class="${param.activa == 'distritos' ? 'on' : ''}"
       href="${pageContext.request.contextPath}/ubicaciones/distritos">Distritos</a>
</div>
