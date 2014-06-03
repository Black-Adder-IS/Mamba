var verificar_estudiante = function() {

    var cadena_1 = "[a-zA-Z]{4,100}";
    var cadena_2 = ".{6,50}";
    var cadena_3 = "^[^@]+@[a-z]+(\.[a-z]{2,4})+$";
    
    re = new RegExp(cadena_1);
    var activar = false;
 
    if (document.getElementById("nombre_Estudiante").value.match(re))
        activar = true;
    else
        return false;

    re = new RegExp(cadena_2);

    if (document.getElementById("contrasenia_Estudiante").value.match(re))
       activar = activar && true;
   else
        return false;
   
   re = new RegExp(cadena_3);

    if (document.getElementById("correo_Estudiante").value.match(re))
       activar = activar && true;
   else
        return false;
   return activar;
};

$(document).ready(function() {
    $('#registrarEstudiante').click(function(event) {
        console.log(verificar_estudiante());
        if (!verificar_estudiante()) {
            $('.aviso').remove();
            $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Revisa los datos de los campos, alguno no cumple con las especificaciones.</div>');
        } else {
            var nombreVar = $('#nombre_Estudiante').val();
            var correoVar = $('#correo_Estudiante').val();
            var contraseniaVar = $('#contrasenia_Estudiante').val();
            $('.aviso').remove();
            if (nombreVar === "" || correoVar === "" || contraseniaVar === "") {
                $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Algún campo está vacío</div>');
            } else {
                $('.aviso').remove();
                $('#registroAlumnoModal').append(' <div id="solicitar_alert" data-alert class="alert-box aviso">Intentando registro</div>');
                $.post('Estudiante?operacion=registrar_Estudiante', {
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
        }
    });
});