# 🍽️ Backend - Sistema de Pedidos para Restaurante

Este proyecto corresponde al backend de una aplicación web diseñada para la gestión de un restaurante con pedidos online. Proporciona funcionalidades clave como registro e inicio de sesión de clientes, gestión de productos, control de pedidos, administración de usuarios y visualización de estadísticas.

## 🚀 Funcionalidades principales

- Registro y autenticación de clientes con Auth0.
- Gestión de usuarios (clientes y empleados).
- Creación y seguimiento de pedidos.
- Administración de productos y categorías.
- Visualización de estadísticas de ventas.

## 🧱 Arquitectura

El proyecto está desarrollado en **Java con Spring Boot** y sigue una estructura en capas que facilita el mantenimiento y escalabilidad de la aplicación:

📦 src  
├── 📁 controller → Maneja las solicitudes HTTP (REST API).  
├── 📁 service → Contiene la lógica de negocio de la aplicación.  
├── 📁 repository → Acceso a registros de la base de datos.  
├── 📁 entity → Representa las entidades del sistema.   
├── 📁 dto → Objetos para transferencia de datos entre capas.  
├── 📁 config → Configuraciones.  

## ⚙️ Requisitos

- Java 17 o superior  
- Gradle  
- Base de datos:MySQL
- IDE recomendado: IntelliJ

## 🛠️ Instalación y ejecución

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu_usuario/ElBuenSabor.git
   cd ElBuenSabor
Configurar variables de entorno en el archivo application.properties.

Documentación disponible en Swagger: http://localhost:8080/swagger-ui.html  

📬 Contacto
Para dudas o sugerencias, podés contactarme a lucatorresifontana@gmail.com

