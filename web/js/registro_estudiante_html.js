$(document).ready(function() {
    $('#registrarEstudiante').click(function(event) {
        var nombreVar = $('#nombre_Estudiante').val();
        var correoVar = $('#correo_Estudiante').val();
        var contraseniaVar = $('#contrasenia_Estudiante').val();
        $('.aviso').remove();
        if (nombreVar === "" || correoVar === "" || contraseniaVar === "") {
            $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Algún campo está vacío</div>');
        } else {
            $('.aviso').remove();
            $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box aviso">Intentando registro</div>');
            $.post('../Estudiante?operacion=registrar_Estudiante', {
                nombre_Estudiante: nombreVar,
                correo_Estudiante: correoVar,
                contrasenia_Estudiante: contraseniaVar
            }, function(respuesta) {
                if (parseInt(respuesta) === 0) {
                    $('.aviso').remove();
                    $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box success aviso">Registro exitoso</div>');
                } else if (parseInt(respuesta) === 1) {
                    $('.aviso').remove();
                    $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Ya existe ese correo. Intente con otro.</div>');
                } else {
                    $('.aviso').remove();
                    $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Hubo un fallo en el registro.</div>');
                }
            });
        }
    });
});