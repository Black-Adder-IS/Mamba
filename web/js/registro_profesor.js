var verificar_profesor = function() {

    var cadena_1 = "[a-zA-Z]{4,100}";
    var cadena_2 = ".{6,50}";
    var cadena_3 = "^[^@]+@[a-z]+(\.[a-z]{2,4})+$";
    
    re = new RegExp(cadena_1);
    var activar = false;
 
    if (document.getElementById("nombre_Profesor").value.match(re))
        activar = true;
    else
        return false;

    re = new RegExp(cadena_2);

    if (document.getElementById("contrasenia_Profesor").value.match(re))
       activar = activar && true;
   else
        return false;
   
   re = new RegExp(cadena_3);

    if (document.getElementById("correo_Profesor").value.match(re))
       activar = activar && true;
   else
        return false;
   return activar;
};
$(document).ready(function() {
    
    $('#registrarProfesor').click(function(event) {
        if (!verificar_profesor()) {
            $('.aviso').remove();
            $('#registroMaestroModal').append('<div id="solicitar_alert" data-alert class="alert-box warning aviso">Revisa los datos de los campos, alguno no cumple con las especificaciones.</div>');
        } else {
        var nombreVar = $('#nombre_Profesor').val();
        var correoVar = $('#correo_Profesor').val();
        var contraseniaVar = $('#contrasenia_Profesor').val();

        $('.aviso').remove();
        if (nombreVar === "" || correoVar === "" || contraseniaVar === "") {
            $('#registroMaestroModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Algún campo está vacío</div>');
        } else {
            $('.aviso').remove();
            $('#registroMaestroModal').append(' <div id="solicitar_alert" data-alert class="alert-box aviso">Intentando registro</div>');
            $.post('Profesor?operacion=registrar_Profesor', {
                nombre_Profesor: nombreVar,
                correo_Profesor: correoVar,
                contrasenia_Profesor: contraseniaVar
            }, function(respuesta) {
                if (parseInt(respuesta) === 0) {
                    $('.aviso').remove();
                    $('#registroMaestroModal').append(' <div id="solicitar_alert" data-alert class="alert-box success aviso">Registro exitoso</div>');
                    //$('#mensaje').text("Registro exitoso");             
                } else if (parseInt(respuesta) === 1) {
                    $('.aviso').remove();
                    $('#registroMaestroModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Ya existe ese correo. Intente con otro.</div>');
                    //$('#mensaje').text("Ya existe ese correo. Intente con otro.");                
                } else {
                    $('.aviso').remove();
                    $('#registroMaestroModal').append(' <div id="solicitar_alert" data-alert class="alert-box warning aviso">Hubo un fallo en el registro.</div>');
                    //$('#mensaje').text("Hubo un fallo en el registro");
                }
            });
        }}
    });
});