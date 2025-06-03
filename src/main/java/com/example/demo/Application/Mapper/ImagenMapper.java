package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Generic.ImagenDto;
import com.example.demo.Domain.Entities.ImagenCategoria;
import com.example.demo.Domain.Entities.ImagenInsumo;
import com.example.demo.Domain.Entities.ImagenManufacturado;
import com.example.demo.Domain.Entities.ImagenNoElaborado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImagenMapper {
    //Reciben la imagen al momento de crear el objeto correspondiente para ser guardado en la base de datos
    ImagenManufacturado imagenDtoToImagenManufacturado(ImagenDto imagenDto);
    ImagenInsumo imagenDtoToImagenInsumo(ImagenDto imagenDto);
    ImagenCategoria imagenDtoToImagenCategoria(ImagenDto imagenDto);
    ImagenNoElaborado imagenDtoToImagenNoElaborado(ImagenDto imagenDto);

    //Se utiliza cuando se desea enviar como respuesta de una llamada a un endpoint una imagen en el Dto
    ImagenDto imagenManufacturadoToImagenDto(ImagenManufacturado imagenManufacturado);
    ImagenDto imagenInsumoToImagenDto(ImagenInsumo imagenInsumo);
    ImagenDto imagenCategoriaToImagenDto(ImagenCategoria imagenCategoria);
    ImagenDto imagenNoElaboradoToImagenDto(ImagenNoElaborado imagenNoElaborado);
}
