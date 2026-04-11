# DOSW_ParcialT2_StivenPardo

**Nombre Completo:** Stiven Pardo  
**Grupo:** 2


## 1 Funcionalidades

### Roles identificados

- **ANONYMOUS:** Usuario no autenticado (solo registro y login)
- **PASSENGER:** Pasajero autenticado
- **DRIVER:** Conductor autenticado con vehículo asociado
- **ADMIN:** Administrador del sistema con acceso total a todas las funcionalidades

### Tabla de funcionalidades

| # | Funcionalidad | a. Verbo HTTP | b. ¿Idempotente? | c. Razón técnica | d. Roles con acceso |
|---|---|:---:|:---:|---|---|
| 1 | Registrarse | `POST` | No | Cada ejecución intenta crear un nuevo recurso en la BD. Dos peticiones con el mismo email producen resultados diferentes (error de duplicado en la segunda). | ANONYMOUS |
| 2 | Autenticarse (Login) | `POST` | No | Cada petición genera un nuevo token JWT con diferente `iat`/`exp`. El resultado varía con cada llamada aunque los datos de entrada sean iguales. | ANONYMOUS |
| 3 | Listar vehículos disponibles | `GET` | Si | Operación de solo lectura. No modifica el estado del servidor. Repetir la petición siempre devuelve el mismo resultado. | PASSENGER, DRIVER, ADMIN |
| 4 | Solicitar viaje | `POST` | No | Cada petición intentaría crear una nueva entidad `Trip`. El negocio limita 1 viaje activo por pasajero, pero la operación en sí no es idempotente por naturaleza. | PASSENGER |
| 5 | Cancelar viaje | `PATCH` | Si | Actualiza el estado del viaje a `CANCELADO`. Repetir la misma operación N veces produce siempre el mismo estado final sin efectos adicionales. | PASSENGER, ADMIN |
| 6 | Actualizar estado del viaje (EN_CURSO / FINALIZADO) | `PATCH` | Si | Lleva el estado a un valor fijo y definitivo. Repetir la petición no cambia el resultado final ni genera efectos secundarios. | DRIVER |
| 7 | Consultar historial de viajes | `GET` | Si | Operación de solo lectura. No produce cambios en el sistema. Repetir la petición retorna siempre la misma colección de viajes. | PASSENGER, ADMIN |

---

### e, f, g — Detalle por funcionalidad

#### F1 — Registrarse · `POST /api/auth/register`

**e. Datos de entrada y salida**

| Dato | Tipo | Obligatorio | Descripción |
|---|---|:---:|---|
| **ENTRADA** | | | |
| `name` | String | Si | Nombre completo del usuario |
| `email` | String | Si | Correo institucional |
| `password` | String | Si | Contraseña segura |
| `role` | Enum: `PASSENGER` / `DRIVER` | Si | Rol del usuario |
| `licensePlate` | String | Solo si DRIVER | Placa del vehículo |
| `brand` | String | Solo si DRIVER | Marca del vehículo |
| `model` | String | Solo si DRIVER | Modelo del vehículo |
| **SALIDA** | | | |
| `userId` | String (UUID) | Si | Identificador único generado |
| `name` | String | Si | Nombre registrado |
| `email` | String | Si | Correo registrado |
| `role` | String | Si | Rol asignado |
| `message` | String | Si | Mensaje de confirmación |

**f. Ejemplo de entrada y salida**

```json
// Entrada (DRIVER)
{
  "name": "Stiven Pardo",
  "email": "spardo@escuela.edu.co",
  "password": "Pass1234!",
  "role": "DRIVER",
  "licensePlate": "ABC-123",
  "brand": "Renault",
  "model": "Logan"
}

// Salida — 201 Created
{
  "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "name": "Stiven Pardo",
  "email": "spardo@escuela.edu.co",
  "role": "DRIVER",
  "message": "Usuario registrado exitosamente"
}
```

**g. Validaciones**

| Tipo | Regla |
|---|---|
| Input | `email` no puede estar vacío ni nulo |
| Input | `email` debe tener formato de correo válido |
| Input | `password` debe tener mínimo 8 caracteres |
| Input | `name` no puede estar vacío |
| Input | `role` debe ser `PASSENGER` o `DRIVER` |
| Input | Si `role=DRIVER`: `licensePlate`, `brand` y `model` son obligatorios |
| Negocio | El email debe pertenecer al dominio institucional (`@escuela.edu.co`) |
| Negocio | El email no puede estar ya registrado en el sistema |
| Negocio | Un conductor solo puede tener 1 vehículo asociado |
| Negocio | Un usuario no puede tener rol `PASSENGER` y `DRIVER` simultáneamente |

---

#### F2 — Autenticarse (Login) · `POST /api/auth/login`

**e. Datos de entrada y salida**

| Dato | Tipo | Obligatorio | Descripción |
|---|---|:---:|---|
| **ENTRADA** | | | |
| `email` | String | Si | Correo institucional registrado |
| `password` | String | Si | Contraseña del usuario |
| **SALIDA** | | | |
| `token` | String (JWT) | Si | Token de acceso para las demás operaciones |
| `userId` | String | Si | ID del usuario autenticado |
| `role` | String | Si | Rol del usuario |
| `expiresIn` | Long (ms) | Si | Tiempo de vida del token en milisegundos |

**f. Ejemplo de entrada y salida**

```json
// Entrada
{
  "email": "spardo@escuela.edu.co",
  "password": "Pass1234!"
}

// Salida — 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": "a1b2c3d4-...",
  "role": "DRIVER",
  "expiresIn": 86400000
}
```

**g. Validaciones**

| Tipo | Regla |
|---|---|
| Input | `email` no puede estar vacío |
| Input | `password` no puede estar vacío |
| Negocio | El email debe existir en el sistema |
| Negocio | La contraseña debe coincidir con el hash almacenado en la BD |

---

#### F3 — Listar vehículos disponibles · `GET /api/vehicles`

**e. Datos de entrada y salida**

| Dato | Tipo | Obligatorio | Descripción |
|---|---|:---:|---|
| **ENTRADA** | | | |
| `Authorization` | String (Header `Bearer <JWT>`) | Si | Token de autenticación |
| **SALIDA** | | | |
| `vehicleId` | String | Si | Identificador del vehículo |
| `licensePlate` | String | Si | Placa del vehículo |
| `brand` | String | Si | Marca |
| `model` | String | Si | Modelo |
| `driverName` | String | Si | Nombre del conductor dueño |

**f. Ejemplo de entrada y salida**

```json
// Salida — 200 OK
[
  {
    "vehicleId": "v001",
    "licensePlate": "ABC-123",
    "brand": "Renault",
    "model": "Logan",
    "driverName": "Stiven Pardo"
  },
  {
    "vehicleId": "v002",
    "licensePlate": "XYZ-789",
    "brand": "Chevrolet",
    "model": "Spark",
    "driverName": "Juan Pérez"
  }
]
```

**g. Validaciones**

| Tipo | Regla |
|---|---|
| Input | El header `Authorization` debe estar presente |
| Input | El token debe ser un JWT con formato válido y no expirado |
| Negocio | Solo usuarios autenticados (PASSENGER, DRIVER o ADMIN) pueden ver el listado |

---

#### F4 — Solicitar viaje · `POST /api/trips`

**e. Datos de entrada y salida**

| Dato | Tipo | Obligatorio | Descripción |
|---|---|:---:|---|
| **ENTRADA** | | | |
| `vehicleId` | String | Si | ID del vehículo seleccionado |
| `origin` | String | Si | Dirección de origen del viaje |
| `destination` | String | Si | Dirección de destino del viaje |
| **SALIDA** | | | |
| `tripId` | String | Si | ID del viaje creado |
| `status` | String | Si | Estado inicial: `SOLICITADO` |
| `origin` | String | Si | Origen confirmado |
| `destination` | String | Si | Destino confirmado |
| `vehicle` | Object | Si | Información del vehículo |
| `passenger` | Object | Si | Información del pasajero |
| `createdAt` | String (ISO 8601) | Si | Fecha y hora de creación |

**f. Ejemplo de entrada y salida**

```json
// Entrada
{
  "vehicleId": "v001",
  "origin": "Calle 72 #10-34, Bogotá",
  "destination": "Carrera 15 #93-47, Bogotá"
}

// Salida — 201 Created
{
  "tripId": "t001",
  "status": "SOLICITADO",
  "origin": "Calle 72 #10-34, Bogotá",
  "destination": "Carrera 15 #93-47, Bogotá",
  "vehicle": { "vehicleId": "v001", "licensePlate": "ABC-123" },
  "passenger": { "userId": "p001", "name": "Laura García" },
  "createdAt": "2026-04-11T21:00:00"
}
```

**g. Validaciones**

| Tipo | Regla |
|---|---|
| Input | `vehicleId` no puede estar vacío |
| Input | `origin` no puede estar vacío |
| Input | `destination` no puede estar vacío |
| Negocio | Solo un usuario con rol `PASSENGER` puede solicitar un viaje |
| Negocio | Un pasajero solo puede tener 1 viaje activo (estado `SOLICITADO` o `EN_CURSO`) |
| Negocio | El vehículo seleccionado debe existir en el sistema |

---

#### F5 — Cancelar viaje · `PATCH /api/trips/{tripId}/cancel`

**e. Datos de entrada y salida**

| Dato | Tipo | Obligatorio | Descripción |
|---|---|:---:|---|
| **ENTRADA** | | | |
| `tripId` | String (Path variable) | Si | ID del viaje a cancelar |
| **SALIDA** | | | |
| `tripId` | String | Si | ID del viaje cancelado |
| `status` | String | Si | Nuevo estado: `CANCELADO` |
| `message` | String | Si | Mensaje de confirmación |

**f. Ejemplo de entrada y salida**

```json
// PATCH /api/trips/t001/cancel

// Salida — 200 OK
{
  "tripId": "t001",
  "status": "CANCELADO",
  "message": "Viaje cancelado exitosamente"
}
```

**g. Validaciones**

| Tipo | Regla |
|---|---|
| Input | `tripId` no puede estar vacío y debe ser un UUID válido |
| Negocio | Solo el pasajero propietario del viaje puede cancelarlo |
| Negocio | Solo se puede cancelar si el viaje está en estado `SOLICITADO` |

---

#### F6 — Actualizar estado del viaje · `PATCH /api/trips/{tripId}/status`

**e. Datos de entrada y salida**

| Dato | Tipo | Obligatorio | Descripción |
|---|---|:---:|---|
| **ENTRADA** | | | |
| `tripId` | String (Path variable) | Si | ID del viaje |
| `status` | Enum: `EN_CURSO` / `FINALIZADO` | Si | Nuevo estado del viaje |
| **SALIDA** | | | |
| `tripId` | String | Si | ID del viaje actualizado |
| `status` | String | Si | Estado actualizado |
| `message` | String | Si | Mensaje de confirmación |

**f. Ejemplo de entrada y salida**

```json
// Entrada — Conductor toma el viaje
{ "status": "EN_CURSO" }

// Salida — 200 OK
{
  "tripId": "t001",
  "status": "EN_CURSO",
  "message": "Estado del viaje actualizado exitosamente"
}

// Entrada — Conductor finaliza el viaje
{ "status": "FINALIZADO" }

// Salida — 200 OK
{
  "tripId": "t001",
  "status": "FINALIZADO",
  "message": "Viaje finalizado exitosamente"
}
```

**g. Validaciones**

| Tipo | Regla |
|---|---|
| Input | `status` debe ser `EN_CURSO` o `FINALIZADO` |
| Input | `tripId` no puede estar vacío y debe ser un UUID válido |
| Negocio | Solo el conductor autenticado puede modificar el estado |
| Negocio | Solo se puede cambiar a `EN_CURSO` desde el estado `SOLICITADO` |
| Negocio | Solo se puede cambiar a `FINALIZADO` desde el estado `EN_CURSO` |

---

#### F7 — Consultar historial de viajes · `GET /api/trips/history`

**e. Datos de entrada y salida**

| Dato | Tipo | Obligatorio | Descripción |
|---|---|:---:|---|
| **ENTRADA** | | | |
| `Authorization` | String (Header `Bearer <JWT>`) | Si | Token del pasajero autenticado |
| **SALIDA** | | | |
| `tripId` | String | Si | ID del viaje |
| `origin` | String | Si | Dirección de origen |
| `destination` | String | Si | Dirección de destino |
| `status` | String | Si | Estado final del viaje |
| `vehicle` | Object | Si | Info del vehículo usado |
| `createdAt` | String (ISO 8601) | Si | Fecha de creación del viaje |

**f. Ejemplo de entrada y salida**

```json
// Salida — 200 OK
[
  {
    "tripId": "t001",
    "origin": "Calle 72 #10-34, Bogotá",
    "destination": "Carrera 15 #93-47, Bogotá",
    "status": "FINALIZADO",
    "vehicle": { "licensePlate": "ABC-123", "brand": "Renault", "model": "Logan" },
    "createdAt": "2026-04-10T15:00:00"
  },
  {
    "tripId": "t002",
    "origin": "Av. El Dorado #68-05",
    "destination": "Calle 26 #92-32",
    "status": "CANCELADO",
    "vehicle": { "licensePlate": "XYZ-789", "brand": "Chevrolet", "model": "Spark" },
    "createdAt": "2026-04-09T10:30:00"
  }
]
```

**g. Validaciones**

| Tipo | Regla |
|---|---|
| Input | El header `Authorization` debe estar presente y el JWT no puede estar expirado |
| Negocio | Solo el pasajero autenticado (o ADMIN) puede consultar el historial |
| Negocio | Solo se retornan viajes con estado `FINALIZADO` o `CANCELADO` |

---

## 2 Diferencia entre Validaciones de input y Validaciones de negocio

Las validaciones de input se realizan en la capa del controlador, antes de que la lógica de negocio intervenga. Verifican la estructura, el formato y la integridad básica de los datos recibidos sin necesidad de consultar el estado del sistema. Ejemplos:
- Que un campo no llegue vacío o nulo (`@NotBlank`)
- Que el email tenga formato válido (`@Email`)
- Que un número esté dentro de un rango permitido (`@Min`, `@Max`)
- Que la longitud de un campo sea la esperada (`@Size`)

Las validaciones de negocio se ejecutan en la capa de servicios o validadores y evalúan si los datos cumplen con las reglas propias del dominio de la aplicación. Requieren consultar el estado actual del sistema. Ejemplos:
- Que el email sea del dominio institucional `@escuela.edu.co`
- Que un conductor NO pueda solicitar un viaje (violación de rol)
- Que un pasajero no tenga más de 1 viaje activo simultáneamente
- Que el correo no esté ya registrado en la base de datos

Podemos entonces concluir que Las validaciones de input garantizan la integridad de formato de los datos, mientras que las de negocio garantizan la coherencia del dominio y las reglas de la aplicación.

## 3 Diferencias entre autenticación, autorización e integridad

**Autenticación :** Validaria que el usuario si sea el.

**Autorización :** Serian las acciones que puede realizar el usuario dentro de la plataforma.

**Integridad :** revisaria que los datos o informacion no fueran alterados.


## 4 Diagrama de Componentes general

![](https://github.com/Exael74/DOSW_ParcialT2_StivenPardo/blob/featureTeorica/docs/uml/Diagrama%20de%20componentes%20general.png)

## 5 ¿Qué problemas pueden surgir si no se separan correctamente las capas dentro de un proyecto de software?

Se pueden generar varios problemas como un cambio BD que pueda afectar a Backend o Front, tambien puede generar dificultad en hacer pruebas y que es muy poco escalable ya que cualquier cambio afecta todo el programa 

## 6 Diagrama de componentes especificos

![](https://github.com/Exael74/DOSW_ParcialT2_StivenPardo/blob/develop/docs/uml/Diagramas%20de%20componentes%20especificos.png)

## 7 ¿Cuáles son las diferencias entre un validador, una utilidad y un servicio?

**validador  :** maneja cosas como las reglas de negocio como el que un conductor no pueda pedir un viaje

**Utilidad :** maneja las funcionalidades que puede hacer como darme la lista de vehiculos 
 
**Servicio :** es el que maneja los casos de uso



## 8 Diagrama de clases


## 9 Diagrama de entidad relacion

![](https://github.com/Exael74/DOSW_ParcialT2_StivenPardo/blob/featureTeorica/docs/uml/Diagrama%20Entidad-Relaci%C3%B3n.png)

## 10 2 indices para mejorar el rendimiento de las consultas

### Indice 1 — users.email

La consulta de autenticacion (login) busca un usuario por su email en cada peticion. Sin un indice, la base de datos tiene que recorrer toda la tabla de usuarios fila por fila (Full Table Scan) para encontrar el registro. Al agregar un indice sobre la columna email, la base de datos puede ubicar el usuario directamente en tiempo O(log n), lo que mejora considerablemente el tiempo de respuesta a medida que crece la cantidad de usuarios registrados.

```sql
CREATE UNIQUE INDEX idx_users_email ON users(email);
```

En JPA/Hibernate esto se expresa en la entidad:

```java
@Table(name = "users", indexes = {
    @Index(name = "idx_users_email", columnList = "email", unique = true)
})
```

Consulta que se beneficia:

```sql
SELECT * FROM users WHERE email = 'spardo@escuela.edu.co';
```

---

### Indice 2 — trips(passenger_id, status)

Las dos consultas mas frecuentes sobre la tabla de viajes son: verificar si un pasajero ya tiene un viaje activo antes de crear uno nuevo (passenger_id + status IN ('SOLICITADO','EN_CURSO')) y consultar el historial de viajes finalizados o cancelados de un pasajero (passenger_id + status IN ('FINALIZADO','CANCELADO')). Ambas filtran siempre por passenger_id y status juntos, por lo que un indice compuesto sobre esas dos columnas en ese orden permite que la base de datos resuelva las dos consultas sin escanear toda la tabla.

```sql
CREATE INDEX idx_trips_passenger_status ON trips(passenger_id, status);
```

En JPA/Hibernate esto se expresa en la entidad:

```java
@Table(name = "trips", indexes = {
    @Index(name = "idx_trips_passenger_status", columnList = "passenger_id, status")
})
```

Consultas que se benefician:

```sql
-- Validacion de negocio: el pasajero no puede tener mas de 1 viaje activo
SELECT * FROM trips WHERE passenger_id = 'p001' AND status IN ('SOLICITADO', 'EN_CURSO');

-- Historial de viajes del pasajero
SELECT * FROM trips WHERE passenger_id = 'p001' AND status IN ('FINALIZADO', 'CANCELADO');
```


## 11 TDD para Solicitar viaje 

# Fase RED

Primero escribiríamos las pruebas para que fallen, ya que no tenemos código, por eso todas las pruebas fallaran. En este caso revisamos el comportamiento que estos tendrán.

# Fase Green

Ya escribimos el código para que las pruebas pasen, aquí buscamos que los test sean exitosos con la lógica que tenemos

# Fase Refactor

Ya con las pruebas pasadas, ahora si nos centramos en que el código tenga mas calidad y sea mas limpio para una mejor funcionalidad


## 12 Como las pruebas garantizan el cumplimiento de las reglas de negocio y la integridad del sistema

Las pruebas son importantes para validar las reglas de negocio, ya que nos muestran si no se están cumpliendo o si esta fallando los criterios que nos dieron para el proyecto. Con la integridad del sistema, porque nos muestran errores que hacen que el programa falle, además las pruebas al ser casos en especifico nos indican exactamente que debemos arreglar en el código.

## 13 Describir las etapas principales de un pipelien y en que consiste

- El tenemos que obtener el codigo 
- Revisar que si compila 
- Al ralizar las pruebas ver que tiene una buena cobertura 
- Realizar el analizis estatico del codigo para ver si tiene una buena calidad
- Al final Realizar el despliege

## 14 ¿Qué sucede si una prueba falla en el pipeline? ¿Debe permitirse el despliegue?

Si en el pipeline ocurre una falla no se debe permitir el despliege ya que el pipeline nos indica que el programa cumple los requisitos que tenemos y asegura la buena funcionalidad del codigo

## 15 Explique el concepto de logging en el manejo de errores

## a ¿Qué información debería registrarse?

- La fecha y hora en la que ocurrio el error
- El endpoint y el metodo HTTP que se utilizo
- El mensaje de error que retorno

## b ¿Qué NO debería registrarse (por seguridad)?

- Contraseñas
- Token de autentificacion
- Informacion personal del usuario

## 16 Figma

## Evidencias de Cuentas

### Herramienta de Modelado (Lucidchart / Draw.io / Miro)

### Herramienta de Diseño de Interfaces (Figma)





https://github.com/6Sebastian6/PreParcial-T2/tree/develop

