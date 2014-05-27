/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    var muestra_mensajes = function() {
        var cuerpo = $('body');
        if (localStorage.mensaje) {
            cuerpo.prepend('<div data-alert class="alert-box alerta pulse animated">' + localStorage.mensaje + '</div>');
            localStorage.removeItem("mensaje");
        }
        if (localStorage.mensaje_error) {
            cuerpo.prepend('<div data-alert class="alert-box warning alerta pulse animated">' + localStorage.mensaje_error + '</div>');
            localStorage.removeItem("mensaje_error");
        }
    };
    $(document).on('click', ".alerta", function() {
        $(this).hide(function() {
            $(this).remove();
        });
    });
    muestra_mensajes();
});