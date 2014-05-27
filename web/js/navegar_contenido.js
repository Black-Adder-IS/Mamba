/* 
 * 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$( document ).ready(function() {
    var elementos_por_pagina = 5;
    //$( "#filtro_todos" ).click(function( event ) {
    //    //var id=$('#id_text').val();
    //    //var contrasena=$('#contrasena_text').val();
    //    $.getJSON('../Profesor',{tipo:2},function(profesores) { 
    //        $('#profesores_tbl').html("");
    //        $.each(profesores, function(i, profesor) {
    //            console.log(profesor);
    //            $('#profesores_tbl').append(profesor);
    //        });
    //    });
    //});
    var renueva_pag_prof = function (total, tam_pagina) {
        $('#paginas_profesor').html('<li class="arrow unavailable"><a href="#">&laquo;</a></li>');
        $('#paginas_profesor').append('<li class="pag_profesor current" data-pagina="0"><a href="#">1</a></li>');
        var index = 1;
        console.log("Index: " + index + " Total: " + total + " Tam_pagina: " + tam_pagina);
        for(;index*tam_pagina < total;index++){
             $('#paginas_profesor').append('<li class="pag_profesor" data-pagina="' + index +'"><a href="#">' + (index + 1) + '</a></li>');
        }
        $('#paginas_profesor').append('<li class="arrow unavailable"><a href="#">&raquo;</a></li>');
    };
    
    var renueva_pag_curso = function (total, tam_pagina) {
        $('#paginas_curso').html('<li class="arrow unavailable"><a href="#">&laquo;</a></li>');
        $('#paginas_curso').append('<li class="pag_curso current" data-pagina="0"><a href="#">1</a></li>');
        var index = 1;
        console.log("Index: " + index + " Total: " + total + " Tam_pagina: " + tam_pagina);
        for(;index*tam_pagina < total;index++){
             $('#paginas_curso').append('<li class="pag_curso" data-pagina="' + index +'"><a href="#">' + (index + 1) + '</a></li>');
        }
        $('#paginas_curso').append('<li class="arrow unavailable"><a href="#">&raquo;</a></li>');
    };    
    
    //$( ".filtro" ).click(function() {
    //    console.log("Presionado: " + $(this).data('filtro'));
    //    $('.filtro').removeClass('active');
    //    $(this).addClass('active');
    //    var cantidad = -1;
    //    var filtro = $(this).data('filtro');
    //    //Pide la cantidad de elementos que cumplen con el filtro para poder paginar
    //    $.get('../Profesor',{tipo:1, filtro:filtro},function(cuantos) { 
    //        cantidad = cuantos;
    //        console.log("Cuantos: " + cantidad);
    //        //Pide los profesores que cazan con el filtro
    //        $.post('../Profesor',{tipo:2, filtro:filtro, cantidad:elementos_por_pagina, pagina:0},function(profesores) { 
    //            $('#profesores_tbl').html("");
    //            $.each(profesores, function(i, profesor) {
    //                console.log(profesor);
    //                $('#profesores_tbl').append(profesor);
    //            });
    //            renueva_pag_prof(cantidad, elementos_por_pagina);
    //        });
    //    });
    //});
    $(document).on('click', ".pag_profesor", function() {
        $('.pag_profesor').removeClass('current');
        $(this).addClass('current');
        var pagina = $(this).data('pagina');
        var filtro = " ";
        $('.filtro').each(function(i, obj) {
            //test
            if ($(obj).hasClass('active')){
                filtro = $(obj).data('filtro').trim();
            }
        });
        //Pide la cantidad de elementos que cumplen con el filtro para poder paginar
        $.get('../Profesor',{tipo:1, filtro:filtro},function(cuantos) { 
            cantidad = parseInt(cuantos);
        });
        //Pide los profesores que cazan con el filtro
        $.post('../Profesor',{tipo:2, filtro:filtro, cantidad:elementos_por_pagina, pagina:pagina},function(profesores) { 
            console.log("pagina" + pagina);
            $('#profesores_tbl').html("");
            $.each(profesores, function(i, profesor) {
                //console.log(profesor);
                $('#profesores_tbl').append(profesor);
            });
        });
    });
    
    
    
    
    
    var renueva_pag_curso = function (total, tam_pagina) {
        $('#paginas_curso').html('<li class="arrow unavailable"><a href="#">&laquo;</a></li>');
        $('#paginas_curso').append('<li class="pag_curso current" data-pagina="0"><a href="#">1</a></li>');
        var index = 1;
        console.log("Index: " + index + " Total: " + total + " Tam_pagina: " + tam_pagina);
        for(;index*tam_pagina < total;index++){
             $('#paginas_curso').append('<li class="pag_curso" data-pagina="' + index +'"><a href="#">' + (index + 1) + '</a></li>');
        }
        $('#paginas_curso').append('<li class="arrow unavailable"><a href="#">&raquo;</a></li>');
    };
    
    $( ".filtro" ).click(function() {
        console.log("Presionado: " + $(this).data('filtro'));
        $('.filtro').removeClass('active');
        $(this).addClass('active');
        var cantidad = -1;
        var filtro = $(this).data('filtro');
        
        if($(this).hasClass("filtro_profesor")){
            $.get('../Profesor',{tipo:1, filtro:filtro},function(cuantos) { 
                cantidad = cuantos;
                console.log("Cuantos: " + cantidad);
                //Pide los profesores que cazan con el filtro
                $.post('../Profesor',{tipo:2, filtro:filtro, cantidad:elementos_por_pagina, pagina:0},function(profesores) { 
                    $('#profesores_tbl').html("");
                    $.each(profesores, function(i, profesor) {
                        //console.log(profesor);
                        $('#profesores_tbl').append(profesor);
                    });
                    renueva_pag_prof(cantidad, elementos_por_pagina);
                });
            });
        } else if($(this).hasClass("filtro_curso")){
            $.get('../Curso',{tipo:1, filtro:filtro},function(cuantos) { 
                cantidad = cuantos;
                console.log("Cuantos: " + cantidad);
                //Pide los profesores que cazan con el filtro
                $.post('../Curso',{tipo:2, filtro:filtro, cantidad:elementos_por_pagina, pagina:0},function(cursos) { 
                    $('#cursos_tbl').html("");
                    $.each(cursos, function(i, curso) {
                        console.log(curso);
                        $('#cursos_tbl').append(curso);
                    });
                    renueva_pag_curso(cantidad, elementos_por_pagina);
                });
            });
        }
        
        //Pide la cantidad de elementos que cumplen con el filtro para poder paginar
    });
    $(document).on('click', ".pag_curso", function() {
        $('.pag_curso').removeClass('current');
        $(this).addClass('current');
        var pagina = $(this).data('pagina');
        var filtro = " ";
        $('.filtro').each(function(i, obj) {
            //test
            if ($(obj).hasClass('active')){
                filtro = $(obj).data('filtro').trim();
            }
        });
        //Pide la cantidad de elementos que cumplen con el filtro para poder paginar
        $.get('../Curso',{tipo:1, filtro:filtro},function(cuantos) { 
            cantidad = parseInt(cuantos);
        });
        //Pide los profesores que cazan con el filtro
        $.post('../Curso',{tipo:2, filtro:filtro, cantidad:elementos_por_pagina, pagina:pagina},function(cursos) { 
            $('#cursos_tbl').html("");
            $.each(cursos, function(i, curso) {
                console.log(curso);
                $('#cursos_tbl').append(curso);
            });
        });
    });
    
    $('#filtro_profesor_todos').click();
    $('#filtro_curso_todos').click();
});