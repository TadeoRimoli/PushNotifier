PushNotifier - Notificador de Push

PushNotifier es un microservicio desarrollado en Java con Spring Boot que permite el envío de notificaciones push a dispositivos móviles utilizando la API de Expo. El servicio permite crear, almacenar y enviar notificaciones en momentos programados.

Características
- Registro y autenticación: Maneja el registro y autenticación de usuarios utilizando tokens JWT.
- Gestión de notificaciones: Permite la creación y almacenamiento de notificaciones con título, mensaje y token de Expo.
- Envío programado: Un scheduler verifica y envía notificaciones programadas a los dispositivos móviles utilizando la API de Expo.
- Manejo de tokens de actualización: Refresca tokens de acceso cuando expiran.

Requisitos
- Java 11 o superior
- Maven 3.6.3 o superior
- PostgreSQL (o cualquier otra base de datos configurada en application.properties)
