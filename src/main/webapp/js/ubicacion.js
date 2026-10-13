document.addEventListener('DOMContentLoaded', function () {
    var departamento = document.getElementById('departamentoId');
    var provincia = document.getElementById('provinciaId');
    var distrito = document.getElementById('distritoId');

    function llenar(select, url, textoInicial) {
        select.innerHTML = '';
        select.add(new Option(textoInicial, ''));
        if (!url) {
            return;
        }
        fetch(url)
            .then(function (respuesta) {
                return respuesta.json();
            })
            .then(function (items) {
                items.forEach(function (item) {
                    select.add(new Option(item.nombre, item.id));
                });
            });
    }

    departamento.addEventListener('change', function () {
        llenar(distrito, null, 'Seleccione un distrito');
        llenar(provincia, departamento.value ? provincia.dataset.url + '?departamentoId=' + departamento.value : null, 'Seleccione una provincia');
    });

    provincia.addEventListener('change', function () {
        llenar(distrito, provincia.value ? distrito.dataset.url + '?provinciaId=' + provincia.value : null, 'Seleccione un distrito');
    });
});
