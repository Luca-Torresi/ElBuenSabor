package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Generic.ImagenDto;
import com.example.demo.Domain.Entities.ImagenArticulo;
import com.example.demo.Domain.Entities.ImagenCategoria;
import com.example.demo.Domain.Entities.ImagenInsumo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImagenMapper {
    //Reciben la imagen al momento de crear el objeto correspondiente para ser guardado en la base de datos
    ImagenInsumo imagenDtoToImagenInsumo(ImagenDto imagenDto);
    ImagenCategoria imagenDtoToImagenCategoria(ImagenDto imagenDto);
    ImagenArticulo imagenDtoToImagenArticulo(ImagenDto imagenDto);

    //Se utiliza cuando se desea enviar como respuesta de una llamada a un endpoint una imagen en el Dto
    ImagenDto imagenInsumoToImagenDto(ImagenInsumo imagenInsumo);
    ImagenDto imagenCategoriaToImagenDto(ImagenCategoria imagenCategoria);
    ImagenDto imagenArticuloToImagenDto(ImagenArticulo imagenArticulo);
}
