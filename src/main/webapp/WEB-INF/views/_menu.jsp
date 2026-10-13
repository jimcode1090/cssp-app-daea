<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<nav class="menubar">
    <div class="menubar-in">
        <a class="${param.activa == 'sismos' ? 'on' : ''}" href="${pageContext.request.contextPath}/sismos">Sismos</a>
        <a class="${param.activa == 'estaciones' ? 'on' : ''}"
           href="${pageContext.request.contextPath}/estaciones">Estaciones</a>
        <a class="${param.activa == 'ubicaciones' ? 'on' : ''}"
           href="${pageContext.request.contextPath}/ubicaciones/departamentos">Ubicaciones</a>
    </div>
</nav>
