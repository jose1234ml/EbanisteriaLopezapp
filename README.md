# Ebanistería López 🪑✨

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/Hilt-009688?style=for-the-badge&logo=google&logoColor=white)](https://developer.android.com/training/dependency-injection/hilt-android)
[![Retrofit](https://img.shields.io/badge/Retrofit-FF6C37?style=for-the-badge&logo=retrofit&logoColor=white)](https://square.github.io/retrofit/)
[![Coroutines](https://img.shields.io/badge/Coroutines-00C3FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/docs/coroutines-overview.html)
[![Material3](https://img.shields.io/badge/Material3-6200EE?style=for-the-badge&logo=materialdesign&logoColor=white)](https://m3.material.io/)

---
## Links de video y descarga de apk

## Apk:
https://drive.google.com/file/d/1xvZIbuKLf1QWpXpipThBx_mHiYaIm_dL/view?usp=sharing

## Youtube:
https://youtu.be/yo479CY_j4o?si=BZGjy7_zxcMaPpBR
---

## Descripción

**Ebanistería López** es una aplicación Android desarrollada completamente en **Kotlin** utilizando **Jetpack Compose**, diseñada para ofrecer una experiencia completa en la exploración, cotización y compra de productos de ebanistería de manera moderna y eficiente.  

La app sigue una **arquitectura limpia (Clean Architecture) con MVI puro**, separando las capas de **Data**, **Domain** y **Presentation**:  

- **Data:** Gestión de comunicación con APIs remotas mediante `RemoteDataSources` y repositorios que encapsulan la lógica de acceso a datos.  
- **Domain:** Definición de modelos, casos de uso y repositorios abstractos para mantener la lógica de negocio desacoplada.  
- **Presentation:** Pantallas con Jetpack Compose, `ViewModels`, manejo de estados e `intents` para control de flujo reactivo y escalable.  

---

## Funcionalidades

- Autenticación con **login y registro**.  
- Navegación por **productos destacados y categorías**.  
- Consulta de **ofertas semanales**.  
- Visualización de **detalles completos de productos**.  
- Envío de **cotizaciones detalladas vía WhatsApp**, incluyendo información del cliente, producto y especificaciones.  
- Realización de **órdenes de compra** directamente enviadas a la API.  
- **Búsqueda avanzada** de productos y **filtros por categorías**.  
- **Banners destacados**, **botones de acción** y manejo de errores con estados de carga.  

---

## Tecnologías

- **Lenguaje:** Kotlin  
- **UI:** Jetpack Compose, Material3  
- **Arquitectura:** Clean Architecture + MVI  
- **Dependencias:** Hilt (DI), Retrofit (API), Kotlin Coroutines (concurrencia)  
- **Pruebas:** JUnit, Mockk  
- **Navegación:** Navigation Compose  

---

## Diseño y UX

- **Responsive** y accesible.  
- Componentes modernos de Compose: `LazyColumn`, `LazyRow`, `LazyVerticalGrid`, `Scaffold`.  
- Manejo de **estado y errores** en toda la app.  
- Repositorios desacoplados que no se conectan directamente a la API.  

---

## 📱 Capturas de Pantalla

Explora la experiencia completa de **Ebanistería López**:

<div style="display: flex; flex-wrap: wrap; justify-content: center; gap: 10px;">

<div style="flex: 1; min-width: 200px; max-width: 40%;">
  <img src="https://github.com/user-attachments/assets/7d45690d-2bce-4f53-bb56-6c025a43a876" alt="Pantalla 1" width="100%" style="border-radius:8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1);" />
  <p align="center" style="font-size: 0.9em;">Pantalla de inicio y navegación principal</p>
</div>

<div style="flex: 1; min-width: 200px; max-width: 40%;">
  <img src="https://github.com/user-attachments/assets/bd3a7444-7343-4511-8777-424749c93029" alt="Pantalla 2" width="100%" style="border-radius:8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1);" />
  <p align="center" style="font-size: 0.9em;">Lista de productos destacados</p>
</div>

<div style="flex: 1; min-width: 200px; max-width: 40%;">
  <img src="https://github.com/user-attachments/assets/a17b3e8e-88bf-4933-b11f-ba195f03aa38" alt="Pantalla 3" width="100%" style="border-radius:8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1);" />
  <p align="center" style="font-size: 0.9em;">Detalle de producto con información completa</p>
</div>

<div style="flex: 1; min-width: 200px; max-width: 40%;">
  <img src="https://github.com/user-attachments/assets/c25311c8-c5db-461e-821b-b2b6e37e9949" alt="Pantalla 4" width="100%" style="border-radius:8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1);" />
  <p align="center" style="font-size: 0.9em;">Formulario de cotización rápida vía WhatsApp</p>
</div>

<div style="flex: 1; min-width: 200px; max-width: 40%;">
  <img src="https://github.com/user-attachments/assets/70cc269e-88e5-40dd-a3eb-6c67059a0694" alt="Pantalla 5" width="100%" style="border-radius:8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1);" />
  <p align="center" style="font-size: 0.9em;">Confirmación de orden y resumen de compra</p>
</div>

</div>
