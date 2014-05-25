$(document).ready(function() {            
    $('#registrarEstudiante').click(function(event) {
        var nombreVar = $('#nombre_Estudiante').val();
        var correoVar = $('#correo_Estudiante').val();
        var contraseniaVar = $('#contrasenia_Estudiante').val();
        $('.alerta').remove();
        if (nombreVar === "" || correoVar === "" || contraseniaVar === "") {
            $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning alerta">Algún campo está vacío</div>');
        } else {
        
            $.post('Estudiante?operacion=registrar_Estudiante', {
                nombre_Estudiante : nombreVar,
                correo_Estudiante : correoVar,            
                contrasenia_Estudiante : contraseniaVar            
            }, function(respuesta) {
                $('.alerta').remove();
                if (parseInt(respuesta) === 0) {                
                    $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box alerta">Registro exitoso</div>');
                } else if (parseInt(respuesta) === 1) {                
                    $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning alerta">Ya existe ese correo. Intente con otro.</div>');
                } else {                
                    $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning alerta">Hubo un fallo en el registro.</div>');
                }
            });
        }
    });
});