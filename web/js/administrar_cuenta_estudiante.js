 var correoVar;
 if(typeof(Storage)!=="undefined") {
    correoVar = localStorage.getItem("id");
} else {}

$(document).ready(function() {
    $.post('Estudiante?operacion=obtener_Cursos_Actuales', {
            correo_Estudiante : correoVar
        }, function (respuesta) {
            $('#cursos_Actuales').html(respuesta);
            $(document).foundation();
    });
    
    $.post('Estudiante?operacion=obtener_Cursos_Finalizados', {
            correo_Estudiante : correoVar
        }, function (respuesta) {
            $('#cursos_Finalizados').html(respuesta);
            $(document).foundation();
    });
    
    $('#subir').click(function(event) {
        var nombreVar = $('#nombre_Estudiante').val();
        var correoVar1 = $('#correo_Estudiante').val();
        var contraseniaVar = $('#contrasenia_Estudiante').val();
                
        $.post('Estudiante?operacion=editar_Estudiante', {
            correo_Estudiante : correoVar,
            nuevo_nombre_Estudiante : nombreVar,
            nuevo_correo_Estudiante : correoVar1,
            nuevo_contrasenia_Estudiante : contraseniaVar
        }, function (respuesta) {
            var confirmacion = parseInt(respuesta);
            if (confirmacion === 2) {                
                if (correoVar1.trim() == "") {
                    
                } else {
                    localStorage.setItem("id", correoVar1);
                }
                localStorage.setItem('mensaje', 'Datos modificados');
            } else if (confirmacion === 3) {
                localStorage.setItem('mensaje_error', 'No se pueden modificar los datos, intenta con otro correo');
            } else {
                localStorage.setItem('mensaje_error', 'Hubo un problema al editar la información. Intentalo de nuevo.');
            }
            location.href = "estudianteConf.html";
        });
    });
                    
    $('#borrar').click(function(event) {
        $.post('Estudiante?operacion=eliminar_Estudiante', {
            correo_Estudiante : correoVar
            }, function(respuesta) {
            var confirmacion = parseInt(respuesta);

            if (confirmacion === 4) {
                localStorage.removeItem("id");
                localStorage.removeItem("tipo");
                localStorage.setItem('mensaje', "Lo sentimos, esperamos que regreses.");
            } else if(confirmacion === 5) {
               localStorage.setItem('mensaje_error', "No se ha podido borrar la cuenta");
            } else {
                localStorage.setItem('mensaje', "Hubo un problema al borrar la cuenta. Intentalo después.");
            }
            location.href="index.html";                            
        });
    });
});