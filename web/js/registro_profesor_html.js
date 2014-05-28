$(document).ready(function() {
    $('#registrarProfesor').click(function(event) {
        var nombreVar = $('#nombre_Profesor').val();
        var correoVar = $('#correo_Profesor').val();
        var contraseniaVar = $('#contrasenia_Profesor').val();

        $('.aviso').remove();
        if (nombreVar === "" || correoVar === "" || contraseniaVar === "") {
            $('#registroMaestroModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Algún campo está vacío</div>');
        } else {
            $('.aviso').remove();
            $('#registroMaestroModal').append(' <div id="solicitar_alert" data-alert class="alert-box aviso">Intentando registro</div>');
            $.post('../Profesor?operacion=registrar_Profesor', {
                nombre_Profesor: nombreVar,
                correo_Profesor: correoVar,
                contrasenia_Profesor: contraseniaVar
            }, function(respuesta) {
                if (parseInt(respuesta) === 0) {
                    $('#registroMaestroModal').append(' <div id="solicitar_alert" data-alert class="alert-box success aviso">Registro exitoso</div>');
                    //$('#mensaje').text("Registro exitoso");                
                } else if (parseInt(respuesta) === 1) {
                    $('#registroMaestroModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Ya existe ese correo. Intente con otro.</div>');
                    //$('#mensaje').text("Ya existe ese correo. Intente con otro.");                
                } else {
                    $('#registroMaestroModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Hubo un fallo en el registro.</div>');
                    //$('#mensaje').text("Hubo un fallo en el registro");
                }
            });
        }
    });
});