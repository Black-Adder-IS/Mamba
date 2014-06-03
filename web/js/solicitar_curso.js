/* 
 * 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$( document ).ready(function() {

    $(document).on('click', ".solicitar_curso", function() {
        $('.alerta').remove();
        if( !(localStorage.tipo === "estudiante") ){
            $('body').prepend(' <div id="solicitar_alert" data-alert class="alert-box warning alerta">Debes ser un estudiante para solicitar un curso</div>');
            return;
        }
        $('.pag_profesor').removeClass('current');
        var renglon = $(this).closest('tr');
        var profesor = $(this).data('profesor');
        var curso = $(this).data('curso');
        
        
        //Pide los profesores que cazan con el filtro
        $.post('../Curso',{tipo:3, estudiante:localStorage.id, curso:curso},function(profesores) { 
            renglon.remove();
            $('body').prepend(' <div id="solicitar_alert" data-alert class="alert-box alerta">Curso solicitado</div>');
        });
    });
});