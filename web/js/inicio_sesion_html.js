/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$( document ).ready(function() {
    $(document).on('click', "#iniciar_sesion_btn", function() {
        $('#inicio_alert').remove();
        var id=$('#id_text').val();
        var contrasena=$('#contrasena_text').val();
        if (id.trim() === "" || contrasena.trim() === ""){
            $('#iniciarModal').append(' <div id="inicio_alert" data-alert class="alert-box warning">Sus datos no son correctos</div>');
            return;
        }
        if ($("#profesor_radio").prop("checked")) {
            console.log('Entrando a post profesor, correo: ' + id + " contraseña: " + contrasena);
            $.post('../Profesor',{tipo:0,id:id,contrasena:contrasena},function(responseText) { 
                console.log('Respuesta de post' + responseText);
                if (responseText.trim() === "true"){
                    localStorage.setItem('tipo','profesor');
                    localStorage.setItem('id',id);
                    location.reload();
                } else if(responseText.trim() === "false"){
                    $('#iniciarModal').append(' <div id="inicio_alert" data-alert class="alert-box warning">Sus datos no son correctos</div>');
                } else {
                    $('#iniciarModal').append(' <div id="inicio_alert" data-alert class="alert-box warning">Hubo un error el servidor</div>');
                }
            });
        } else {
            console.log('Entrando a post estudiante, correo: ' + id + " contraseña: " + contrasena);
            $.post('../Estudiante',{tipo:0,id:id,contrasena:contrasena},function(responseText) { 
                console.log('Respuesta de post' + responseText);
                if (responseText.trim() === "true"){
                    localStorage.setItem('tipo','estudiante');
                    localStorage.setItem('id',id);
                    location.reload();
                } else if(responseText.trim() === "false"){
                    $('#iniciarModal').append(' <div id="inicio_alert" data-alert class="alert-box warning">Sus datos no son correctos</div>');
                } else {
                    $('#iniciarModal').append(' <div id="inicio_alert" data-alert class="alert-box warning">Hubo un error el servidor</div>');
                }
            });
        }
    });
    $(document).on('click', "#cerrar_sesion_btn", function() {
        console.log("Cerrar sesión");
        $('#mensaje').text("Sesión Cerrada ");
        localStorage.removeItem("tipo");
        localStorage.removeItem("id");
        window.location.href = "http://localhost:8080/Mamba/index.html";
    });
});