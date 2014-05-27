 var correoVar;
 if(typeof(Storage)!=="undefined") {
    correoVar = localStorage.getItem("id");
} else {}

$(document).ready(function() {
    
    $('#hora_Inicio').html('<option value="0">00:00</option>');
    $('#hora_Inicio').append('<option value="0t">00:30</option>');
    for (var i = 1; i < 10; i++) {
        $('#hora_Inicio').append('<option value="' + i + '">0' + i + ':00</option>');
        $('#hora_Inicio').append('<option value="' + i + 't">0' + i + ':30</option>');
    }
    
    for (var i = 10; i < 24; i++) {
        $('#hora_Inicio').append('<option value="' + i + '">' + i + ':00</option>');
        $('#hora_Inicio').append('<option value="' + i + 't">' + i + ':30</option>');
    }
    
    $('#hora_Final').html('<option value="0">00:00</option>');
    $('#hora_Final').append('<option value="0t">00:30</option>');
    for (var i = 1; i < 10; i++) {
        $('#hora_Final').append('<option value="' + i + '">0' + i + ':00</option>');
        $('#hora_Final').append('<option value="' + i + 't">0' + i + ':30</option>');
    }
    
    for (var i = 10; i < 24; i++) {
        $('#hora_Final').append('<option value="' + i + '">' + i + ':00</option>');
        $('#hora_Final').append('<option value="' + i + 't">' + i + ':30</option>');
    }
    
    $.post('Profesor?operacion=obtener_Notificaciones_Pendientes', {
            correo_Profesor : correoVar
        }, function (respuesta) {
            $('#notificaciones_Pendientes').html(respuesta);
            $(document).foundation();
    });
    
    $.post('Profesor?operacion=obtener_Cursos_Espera', {
            correo_Profesor : correoVar
        }, function (respuesta) {
            $('#cursos_Espera').html(respuesta);
    });
    
    $.post('Profesor?operacion=obtener_Cursos_Actuales', {
            correo_Profesor : correoVar
        }, function (respuesta) {
            $('#cursos_Actuales').html(respuesta);
    });
    
    $.post('Profesor?operacion=obtener_Cursos_Finalizados', {
            correo_Profesor : correoVar
        }, function (respuesta) {
            $('#cursos_Finalizados').html(respuesta);
    });
    
    $('#subir').click(function(event) {
        var nombreVar = $('#nombre_Profesor').val();
        var correoVar1 = $('#correo_Profesor').val();
        var contraseniaVar = $('#contrasenia_Profesor').val();
        if (nombreVar == "" && correoVar1 == "" && contraseniaVar == "") {
            
        } else {
            $.post('Profesor?operacion=editar_Profesor', {
                correo_Profesor : correoVar,
                nuevo_nombre_Profesor : nombreVar,
                nuevo_correo_Profesor : correoVar1,
                nuevo_contrasenia_Profesor : contraseniaVar
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
                location.href = "profesorConf.html";
            });
        }
    });
                    
    $('#borrar').click(function(event) {
        $.post('Profesor?operacion=eliminar_Profesor', {
            correo_Profesor : correoVar
            }, function(respuesta) {
            var confirmacion = parseInt(respuesta);

            if (confirmacion === 4) {
                localStorage.removeItem("id");
                localStorage.removeItem("tipo");
                
                localStorage.setItem('mensaje', 'Lo sentimos, esperamos que regreses.');
                location.href="index.html";
            } else if(confirmacion === 5) {
                localStorage.setItem('mensaje_error', "No se ha podido borrar la cuenta");
            } else {
                localStorage.setItem('mensaje_error', "Hubo un problema al borrar la cuenta. Intentalo después.");
            }
        });
    });
});

var crear_curso = function() {
        var posicion = document.getElementById('hora_Inicio').options.selectedIndex;
        var hora_Inicio = document.getElementById('hora_Inicio').options[posicion].text;
        var posicion = document.getElementById('hora_Final').options.selectedIndex;
        var hora_Final = document.getElementById('hora_Final').options[posicion].text;
        var posicion = document.getElementById('tipo_Curso').options.selectedIndex;
        var tipo_curso = document.getElementById('tipo_Curso').options[posicion].text;
        
        $.post('Curso?operacion=crear_Curso', {
            correo_Profesor : correoVar,            
            tiempo_Inicio : hora_Inicio,
            tiempo_Final : hora_Final,
            tipo_Curso : tipo_curso
        }, function(respuesta) {          
            if (parseInt(respuesta) === -1) {                
                localStorage.setItem('mensaje_error',"La hora final del curso es menor o igual a la hora inicial");                
            } else if (parseInt(respuesta) === 0) {                
                localStorage.setItem('mensaje', "Se ha creado el curso");                
            } else if (parseInt(respuesta) === 1) {                
                localStorage.setItem('mensaje_error', "No se puede crear el curso, ya está dando un curso a esa hora");                
            } else {                
                localStorage.setItem('mensaje_error', "Hubo un fallo al intentar crear el curso");
            }
            location.href="profesorConf.html";
        });
};

var calificar_curso = function(id_Curso) {
    var calificacion_curso = prompt("Introduce calificación");
    var nota_curso = "";
    
    if (parseInt(calificacion_curso) <= -1) {
        alert("Calificación menor a 0");
    } else if (parseInt(calificacion_curso) > 10) {
        alert("Calificación mayor a 10");
    } else {
    
        if (parseInt(calificacion_curso) <= 5) {
            nota_curso = prompt("Introduce una nota para el curso");
        }
        $.post('Curso?operacion=calificar_Curso', {
            id : id_Curso,
            calificacion : calificacion_curso,
            nota : nota_curso
        }, function(respuesta) {          
            if (parseInt(respuesta) === 2) {                
                localStorage.setItem('mensaje', "Curso calificado");
            } else if (parseInt(respuesta) === 3) {                
                localStorage.setItem('mensaje_error',"No se ha podido calificar el curso");
            }
            location.href = "profesorConf.html";
        });
    }
};


var asignar_curso = function(id_curso, aceptado) {
    $.post('Curso?operacion=asignar_Curso', {
            id_Curso : id_curso,
            aceptado_Curso : aceptado
        }, function(respuesta) {          
            if (parseInt(respuesta) === 6) {                
                localStorage.setItem('mensaje', "Acción realizada"); 
            } else if (parseInt(respuesta) === 7) {                
                localStorage.setItem('mensaje_error', "No se ha podido realizar la acción");
            }
            location.href = "profesorConf.html";
    });
};

var eliminar_curso = function(id_curso) {
    $.post('Curso?operacion=eliminar_Curso', {
            id_Curso : id_curso
        }, function(respuesta) {          
            if (parseInt(respuesta) === 4) {                
                localStorage.setItem('mensaje', "Acción realizada"); 
            } else if (parseInt(respuesta) === 5) {                
                localStorage.setItem('mensaje_error', "No se ha podido realizar la acción");
            }
            location.href = "profesorConf.html";
        });
};