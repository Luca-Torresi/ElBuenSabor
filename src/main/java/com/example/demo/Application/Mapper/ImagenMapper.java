package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Generic.ImagenModel;
import com.example.demo.Domain.Entities.Imagen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImagenMapper {
    //Reciben la imagen al momento de crear el objeto correspondiente para ser guardado en la base de datos
    Imagen imagenModelToImagen(ImagenModel model);

    //Se utiliza cuando se desea enviar como respuesta de una llamada a un endpoint una imagen en el Dto
    ImagenModel imagenToImagenModel(Imagen imagen);
}
