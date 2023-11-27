# Fitness V4 (dkt-fitness-libs)

## Table of contents

## _Motivation_

![Alt text](https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/Decathlon_Logo.svg/320px-Decathlon_Logo.svg.png?sanitize=true)

**Fitness** is an evolution from **fitness** (v2 and v3) inspired in a hexagonal architecture and spring starters architecture modules for creating microservices

## Microservices architecture

It is a clean and simple architecture in which "plugable" components are defined based on starters to build microservices.

### Why define an architecture?

Defining an architecture can be summarized as building a set of components prior to the development of an app, which solve the typical problems that all development faces. The need to do this has multiple reasons.

- The definition of high availability components that a designer knows how to configure and knows in advance that will be needed
- Reusable components, define a component once and have it used by multiple applications
- Reduce the footprint when starting any project at a technical level
- These components are extensively tested even before development begins.
- Include all particular components that the organization needs and that are known in advance should be included in all applications.

### Why are microservices so popular?

Traditionally, software design and development has been carried out using monolithic architectures in which all functional aspects of the software are coupled and subject to the same program. These types of systems can cause long-term problems because they are difficult to scale.

__Microservices__ were born as an alternative solution to satisfy the needs of companies to implement and make changes to software quickly and easily.

### Advantages of microservices

- __Scalability__. Microservices are modular applications that can be easily replicated and integrated, allowing your application to grow faster and provide better service based on business needs.
- __Simple implementation__. Applications based on microservices are modular, so their implementation is more agile and simpler than monolithic applications. Microservices enable continuous integration, making it easy to test new ideas and roll back if something doesn't work.
- __The low cost__ of errors allows for experimentation, makes it easier to update code, and speeds time to market for new features.
- __Reusable code__. Breaking software into small, well-defined modules allows teams to use features for different purposes. A service written for a certain feature can be used as a building block for another feature. This allows an application to bootstrap itself, as developers can create new capabilities without having to write code from scratch.
- __Agility in changes__. Each microservice can be built on a different technology, so you can choose the technology that best suits the application. Teams can work more independently and quickly, shortening development cycle times.
- __Standalone application__. Each microservice is completely independent, so following the code is easier than if it were a comprehensive application. Additionally, each developer can work simultaneously.
- __Lower risk__. Microservices do not need containers to be deployed, so if one part fails, it will not affect the entire application. Something that did happen with traditional development.

But of course, not everything is a bed of roses, microservices also have their drawbacks, the most important of which are:

- You have to deal with the additional complexity of distributed systems. Implement internal communication between services, implement dependencies from one service to the other, requests that can extend to several services, etc.
- The complexity of the deployment itself. It is very difficult to manage microservices. Maintaining that harmony requires perfect coordination, which is in fact impossible without solutions like kubernetes.
- Greater consumption of resources. Since each microservice has its own Operating System and dependencies, in the end it is more expensive in terms of resources to use microservices than a monolith; What is an Operating System, and its dependencies.

### An architecture built “on top of Spring-boot”

One of the advantages of Spring boot is the definition of bill of materials (BOM), this has the advantage of delegating the bulk of library versioning to the Spring starter.

Furthermore, this has the advantage that Spring boot defines a set of libraries and versions that have been tested for correct operation, minimizing the risk of code breaks when using very recent or very obsolete versions of a library. This is why the architecture per-se must already be defined “on top of Spring-boot”, which is summarized in:

The most important POM of the architecture (and therefore of the micros) must be Spring-Boot and the architecture should only include those libraries that are not included in Spring-boot, eg. Lombok, commons-beanutils, etc.

## Features of this architecture
- Using Spring boot 3.1.x
- Moving to JDK 17
- Based on single responsability modules
- Plugables modules
- Non-intrusive configuration
- Spring boot dependency management as main source of dependency versioning
- Based completely on starters from spring (Self configured modules)
- Simple and self explanatory naming conventions
- Reduce the footprint at beginning of a microservices project, by solving the repetitive (commons) configurations
- Pre-configured and tested components, httpFactory, jdbc connectors, etc
- Pre-configured tools like swagger, api monitor, etc
- Flexible configuration as almost every configuration property can be rewritten by the projects using it
- Centralized dates format handler in controllers
- i18n pre-configured
- Coverage over 90%
- Uses undertow as Non-blocking IO server (for better performance)

## Modules

### Modules briefing

For this architecture we have defined __10 (ten) modules__, each of them defined based on the premise of __"Low Coupling, High Cohesion"__, every responsability has been carefully defined based on the needs the product could have.

| Module  | Briefing |
| ------ | -------- |
| fitness-rest-services-bom 								| A preconfigured POM with most of needs for a complete microservices impl |
| dkt-fitness-rest-services-commons 					| Common libraries used in most of the starters |
| dkt-fitness-boot-starter-rest-components 			| A set of preconfigured components for rest implementations |
| dkt-fitness-boot-starter-openapi						| When added, the swagger is enabled |
| dkt-fitness-boot-starter-externalized-properties	| A set of classes to allow create/read/write application properties from DB |
| dkt-fitness-boot-starter-security-oauth2-test		| Classes to use in security tests, mock users, mock jdbc, etc |
| dkt-fitness-boot-starter-security-oauth2				| A set of classes where the OAuth JWT implementation is preconfigured and ready to use  |
| dkt-fitness-boot-starter-jpa-postgresql				| Full configuration for PostgreSQL JPA |
| dkt-fitness-boot-starter-logging-support				| A preconfigured Logbook classes, plus a set of classes to do auditing using logs |
| dkt-fitness-boot-starter-cloud-support				| Cloud support to use classes like feign. |

### dkt-fitness-boot-starter-cloud-support

This module is responsible of cloud features like:
- **Spring cloud config**: provides server-side and client-side support for externalized configuration in a distributed system.
- **Spring Cloud OpenFeign**: is a declarative web service client. It makes writing web service clients easier.
- **Eureka client**: eureka is the *Netflix Service Discovery Client* to use with service discovery systems.

#### Libraries included in the module

| Group  					| Artifact |
| ------------------------- | -------- |
| org.springframework.cloud | spring-cloud-starter-**config** |
| org.springframework.cloud | spring-cloud-starter-**openfeign** |
| org.springframework.cloud | spring-cloud-starter-**netflix-eureka-client** |

#### Usage

Simply add the dependency in the project and you will have it working.

```xml
<dependency>
	<groupId>com.decathlon</groupId>
	<artifactId>dkt-fitness-boot-starter-cloud-support</artifactId>
</dependency>
```

Create a configuration class, to add a **Authorization Header** eg.

```java
public class FeignPlanningClientConfiguration {

	@Bean
	RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			requestTemplate.header("Content-Type", "application/json");
			requestTemplate.header("Accept", "application/json;application/problem+json");
			requestTemplate.header(
					"Authorization",
					"Bearer " + JwtUtils.getJwtAuthenticationToken().getToken().getTokenValue());
		};
	}
}

```

And create the feign client as:

```java
@FeignClient(
		name = "planningFeignClient",
		configuration = FeignPlanningClientConfiguration.class,
		url = "${client.planning.base-url}")
public interface PlanningClient {

	@GetMapping("/v1/planningByReference")
	ResponseEntity<PlanningDestinyBasicResponse> getPlanningByReference(
			@RequestParam(name = "reference") final String reference);

...
}
```

And use it in any service inside the app

```java
@Service
@RequiredArgsConstructor
public class CmrServiceImpl implements CmrService {

	private final PlanningClient planningClient;

	private PlanningResponse bodyRequest(final String referencePlanning) {
		ResponseEntity<PlanningDestinyBasicResponse> clientResponse =
				planningClient.getPlanningByReference(referencePlanning);
	....
	}
```

For more information about how to use feign please read the [official documentation](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)

#### Default properties

This properties are present at module level, so you can replace them by project values specifically

| Property 												        | Default value |
| ------------------------------------------------------------- | ---- |
| eureka.instance.prefer-ip-address 								| **true** |
| spring.cloud.openfeign.client.config.default.connect-timeout	| **5000** |
| spring.cloud.openfeign.client.config.default.read-timeout		| **5000** |
| spring.cloud.openfeign.client.config.default.logger-level		| **basic** |

### dkt-fitness-boot-starter-externalized-properties

- This module is responsible for reading externalized properties from either a **database** or **in memory** repository.
- The main purpose is to establish a **read/write blocking access** to properties in database when concurrent process flags needs to be read/write from different microservices.
- This module will try to read properties from a database table (query set in the application.properties) and if it does not exist in BD, will try to get from the **application.properties** file inside the project.
- This module **uses a specific table as control** for all the properties created/written/read, set in the application __app.database.configurations-table*__ properties.

Imagine you need to execute a process only when a specific flag is false, and avoid any other MS to execute the same process if this flag is true (as the write operation is blocked by a SELECT FOR UPDATE statement), once the first process that change the value to true (as this value only could be changed by a process), there are an example below.

#### Libraries included in the module

| Group  				| Artifact |
| -------------------| -------- |
| org.springframework	| spring-jdbc |
| org.apache.commons	| commons-lang3 |

#### Usage

Simply add the dependency in the project and you will have it working.

```xml
<dependency>
	<groupId>com.decathlon</groupId>
	<artifactId>dkt-fitness-boot-starter-externalized-properties</artifactId>
</dependency>
```

Create the table in **flyway** eg. previously to use the module:

```sql
CREATE SEQUENCE IF NOT EXISTS configuration_id_seq;

CREATE TABLE IF NOT EXISTS public."configuration" (
	id integer NOT NULL DEFAULT nextval('configuration_id_seq'),
	property VARCHAR(64) NOT NULL,
	property_value VARCHAR(255) NOT NULL
);


ALTER TABLE public."configuration"
ADD CONSTRAINT property_unique
UNIQUE ( property );
```

Create a configuration class like:

```java
@Configuration
public class ExternalizedPropertiesConfiguration {

	@Bean
	ExternalizedPropertiesService databaseExternalizedPropertiesService(
			Environment env, PropertiesRepository databasePropertiesRepository) {
		return new DatabaseExternalizedPropertiesService(env, databasePropertiesRepository);
	}
}

```

And use in a service like this:

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessSoivre {

	public static final String ESTACICE_OPERATION_RUNNING = "estacice.operation.running";
	public static final String ESTACICE_OPERATION_RUNNING_SINCE =
			"estacice.operation.running.since";

	....
	private final ExternalizedPropertiesService properties;	<-- Declaration
```

And you can use it, to create/read/overwrite properties in a database in the following way:

##### Create properties at application boot with default values

```java

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessSoivre {

	...

	@PostConstruct
	private void postConstruct() {
		try {
			properties.createProperty("scheduled.processUploadedSolicitudes.enabled", "true");
		} catch (PropertyDuplicatedException e) {
			// Do nothing
		}
		try {
			properties.createProperty("scheduled.processSolicitudes.enabled", "true");
		} catch (PropertyDuplicatedException e) {
			// Do nothing
		}
		try {
			properties.createProperty("scheduled.getEstaciceResponse.enabled", "true");
		} catch (PropertyDuplicatedException e) {
			// Do nothing
		}
		try {
			properties.createProperty(ESTACICE_OPERATION_RUNNING, "false");
		} catch (PropertyDuplicatedException e) {
			// Do nothing
		}
		try {
			properties.createProperty(ESTACICE_OPERATION_RUNNING_SINCE, "reset");
		} catch (PropertyDuplicatedException e) {
			// Do nothing
		}
	}
```

##### Read properties

```java
public void releaseEstaciceProcessFlag() {

	LocalDateTime lastLoginDate = LocalDateTime.now();

	try {
		lastLoginDate =
				LocalDateTime.parse(
						properties.getConfigurationProperty(
								ProcessSoivre.ESTACICE_OPERATION_RUNNING_SINCE));
	} catch (DateTimeParseException e) {
		// Keep now() value
	}
....
}
```

##### Modify a property in concurrent mode

```java
private void resetEstaciceRunningFlag() throws EstaciceSessionLoginException {
	log.info("Resetting the {} flag", ESTACICE_OPERATION_RUNNING);

	try {
		properties.updatePropertyInConcurrentMode(
				ProcessSoivre.ESTACICE_OPERATION_RUNNING,	<-- Property
				Boolean.TRUE.toString(),			<-- Current value should have in DB
				Boolean.FALSE.toString());			<-- New value to set in DB if previous value exists
	} catch (PropertyConcurrentAccessException e) {
		throw new EstaciceSessionLoginException(
				"The process to reset the "
						+ ESTACICE_OPERATION_RUNNING
						+ " flag has failed, this should not happen. Anyway, the flag was already reset");
	}
}
```

#### Default properties

This properties are present at module level, so you can replace them by project values specifically

| Property 												    | Default value |
| ------------------------------------------------------ | ---- |
| app.database.configurations-table.enabled 						| **true** |
| app.database.configurations-table-select					| **SELECT PROPERTY_VALUE FROM CONFIGURATION WHERE PROPERTY = ?** |
| app.database.configurations-table-insert					| **INSERT INTO CONFIGURATION(PROPERTY, PROPERTY_VALUE) VALUES (?,?)** |
| app.database.configurations-table-select-for-update		| **SELECT PROPERTY_VALUE, PROPERTY FROM CONFIGURATION WHERE PROPERTY = ?** |

### dkt-fitness-boot-starter-jpa-postgresql

This module is responsible of :
- Default **Flyway** configuration (to allow flyway execution only after Hibernate schema creation)
- Set a lot of default hibernate properties (explained in the properties table)
- Set json module configuration for:
	- **ProblemModule**
	- **ConstraintViolationProblemModule**
	- **JavaTimeModule**
- Set json default configuration flags as:
	- builder.featuresToDisable(**SerializationFeature.WRITE_DATES_AS_TIMESTAMPS**);
	- builder.serializationInclusion(**JsonInclude.Include.NON_NULL**);
	- builder.propertyNamingStrategy(**PropertyNamingStrategies.SNAKE_CASE**);
- Exception handling (AdviceTrait) for classes:
	- **DataIntegrityViolationException**
	- **ObjectNotFoundException**
	- **EntityNotFoundException**
	- **ResourceNotFoundException**
- A **StringListConverter class**, to store items list as string in DB field and retrieve it as array from the String saved in DB
- A **SuffixIdSeqNamingStrategy** for databases where the sequence name ends like "sequence_name_**id_seq**"
- **ContraintsNameResolver** class to get the exact definition constraint name from database

#### Libraries included in the module

| Group  					| Artifact |
| -------------------------------- | -------- |
| org.springframework.boot			| **spring-boot-starter-data-jpa** |
| org.postgresql						| **postgresql** |
| org.hibernate.orm					| **hibernate-jcache** |
| com.github.ben-manes.caffeine		| **jcache** second level cache for hibernate |
| org.zalando							| **problem-spring-web** |
| org.zalando							| **jackson-datatype-problem** |
| org.flywaydb							| **flyway-core** |
| com.fasterxml.jackson.datatype	| **jackson-datatype-jsr310** |

#### Usage

Simply add the dependency in the project and you will have it working.

```xml
<dependency>
	<groupId>com.decathlon</groupId>
	<artifactId>dkt-fitness-boot-starter-jpa-postgresql</artifactId>
</dependency>
```

##### Default JPA exception handler controller

###### DataIntegrityViolationException
When a exception arises from `DataIntegrityViolationException` the resulting json is a response like this:

```json
{
"title": "Conflict",
"status": 409,
"detail": "Data constraint violation: foreign key found 'ERROR: insert or update on table \"real_state_managers\" violates foreign key constraint \"fkj3hpjclldufb6kc08p39mhpqh\"\n  Detail: Key (identity_document_type_id)=(0) is not present in table \"identity_document_types\".' but not found the description for the constraint in the resource bundle",
"constraint_key": "exception.constraint.translation.undefined"
}
```

But, if you want a more human friendly message, you must use the `ContraintsNameResolver` to extract the constraint name and translate into a better message, in the exception above the constraint name is __"fkj3hpjclldufb6kc08p39mhpqh"__, so, add to the map a key with this value and a value with the key for the translation.

```java
@Component
public class CustomConstraintNameResolver implements ContraintsNameResolver {

	private static Map<String, String> constraintCodeMap = new HashMap<>();

	static {
		constraintCodeMap.put(
				"fkj3hpjclldufb6kc08p39mhpqh", "exception.example.primary-key.exists");
	}

	@Override
	public Map<String, String> getConstraintCodeMap() {
		return constraintCodeMap;
	}
}
```
If you don't put a translation in the `messages.properties` with a key for "exception.example.primary-key.exists" you will have the same exception as:

```json
{
"title": "Conflict",
"status": 409,
"detail": "Data constraint violation: 'exception.example.primary-key.exists'",
"constraint_key": "exception.example.primary-key.exists"
}
```

But if you add to `src/main/resources/i18n/messages.properties` a key for "exception.example.primary-key.exists"

```properties
exception.example.primary-key.exists=The primary key already exists
```

The json will be a much better exception message

```json
{
"title": "Conflict",
"status": 409,
"detail": "Data constraint violation: 'The primary key already exists'",
"constraint_key": "exception.example.primary-key.exists"
}
```

###### 404 responses generated from ObjectNotFoundException, EntityNotFoundException and ResourceNotFoundException exceptions

When a `ObjectNotFoundException` is thrown, the resulting json is:

```json
{
"title": "Not Found",
"status": 404,
"detail": "No row with the given identifier exists: [not found#1]"
}
```

When a `EntityNotFoundException` is thrown, the resulting json is:

```json
{
"title": "Not Found",
"status": 404
}
```

When a `ResourceNotFoundException` is thrown, the resulting json is:

```json
{
"title": "Not Found",
"status": 404,
"detail": "EntityRepresentationModel not found!"
}
```

##### Database column converter for list of strings

There is a class `StringListConverter` to use when you need to store a String array into a DB field. eg:

```java
@Column(name = "servers_urls", nullable = false)
@Convert(converter = StringListConverter.class)	<-- Using the converter
private List<String> serversUrls;
```

In the table, an array with values `["value1", "value2", "value3"]` will be represented in the field separated by '|' char:

| other_colum | servers_urls |
| ----------- | ------------ |
| ..value ..  | value1\|value2\|value3 |

And when you get it from the database you will have a `List<String>` array back.


#### Default properties

This properties are present at module level, so you can replace them by project values specifically

| Property 												        | Default value | Description |
| ------------------------------------------------------------- | ---- | ---- |
| spring.jpa.open-in-view						| **false**	| Disable anti-pattern open in view |
| spring.data.jpa.repositories.bootstrap-mode	| **lazy**		| Repository bean definitions are considered lazy, lazily inject and only initialized on first use, i.e. the application might have fully started without the repositories initialized. |
| spring.flyway.enabled							| **false**	| Flyway is disabled by default and configured via Bean mode, thus, this will be only triggered when Hibernate finishes to create the schema [StackOverflow thread](https://stackoverflow.com/questions/37097876/spring-boot-hibernate-and-flyway-boot-order) |
| spring.flyway.repair							| **false**	| When a database history needs to be repaired (cause of hash changed in a flyway script), set this flag to true, and take it back again in the next deployment |
| spring.flyway.locations						| **classpath:flyway**		| Sets the default location to **src/main/resources/flyway** |
| spring.flyway.table								| **flyway_schema_history**	| This is the same default value as flyway, only to know you can change it here |
| spring.jackson.property-naming-strategy		| com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy | One of the constants on Jackson's PropertyNamingStrategies. Can also be a fully-qualified class name of a PropertyNamingStrategy implementation.
| spring.jackson.default-property-inclusion	| non-null | Controls the inclusion of properties during serialization. Configured with one of the values in Jackson's JsonInclude.Include enumeration.
| spring.jackson.serialization.write-dates-as-timestamps	| false | Jackson on/off features that affect the way Java objects are serialized.
| spring.jpa.hibernate.ddl-auto						| **none**		| DDL mode. This is actually a shortcut for the "hibernate.hbm2ddl.auto" property. |
| spring.jpa.hibernate.naming.physical-strategy	| **org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy**	| Fully qualified name of the physical naming strategy. |
| spring.jpa.properties.hibernate.order_updates	| **true**						| See Hibernate documentation |
| spring.jpa.properties.hibernate.jdbc.batch_versioned_data		| **true**						| See Hibernate documentation |
| spring.jpa.properties.hibernate.default_batch_fetch_size		| **16**						| See Hibernate documentation |
| spring.jpa.properties.hibernate.cache.use_second_level_cache	| **true**						| See Hibernate documentation |
| spring.jpa.properties.hibernate.cache.use_query_cache			| **true**						| See Hibernate documentation |
| spring.jpa.properties.hibernate.connection.autocommit			| **false**					| See Hibernate documentation |
| spring.jpa.properties.hibernate.jdbc.batch_size					| **25**						| See Hibernate documentation |
| spring.jpa.properties.hibernate.show_sql							| **false**					| See Hibernate documentation |
| spring.jpa.properties.hibernate.format_sql						| **true**						| See Hibernate documentation |
| spring.jpa.properties.hibernate.cache.region.factory_class		| **org.hibernate.cache.jcache.internal.JCacheRegionFactory** | See Hibernate documentation |
| spring.jpa.properties.hibernate.jdbc.time_zone					| **UTC**						| See Hibernate documentation |
| spring.jpa.properties.hibernate.id.db_structure_naming_strategy	| **not set**					| Values:com.decathlon.data.strategies.SuffixIdSeqNamingStrategy when need to add id_seq at the end |

### dkt-fitness-boot-starter-logging-support

This module is responsible of:
- Configure `logbook` filter from zalando
- Generate "auditable" log messages to follow with other tools like Elastic

#### Libraries included in the module

| Group | Artifact |
| ----- | -------- |
| org.zalando	| logbook-spring-boot-starter |
| org.aspectj	| aspectjweaver |
| org.apache.commons	| commons-lang3 |
| commons-beanutils	| commons-beanutils |

#### Usage

Simply add the dependency in the project and you will have it working.

```xml
<dependency>
	<groupId>com.decathlon</groupId>
	<artifactId>dkt-fitness-boot-starter-logging-support</artifactId>
</dependency>
```

##### Generate undertow accesslog

Inside the folder logs/access_log.log you will see eg.

```text
[20/Nov/2023:22:11:02 +0100] 0:0:0:0:0:0:0:1 GET /api/countries HTTP/1.1 404 (- ms)
[20/Nov/2023:22:11:41 +0100] 0:0:0:0:0:0:0:1 GET /api/countries HTTP/1.1 200 (- ms)
[20/Nov/2023:22:12:14 +0100] 0:0:0:0:0:0:0:1 GET /api/countries HTTP/1.1 200 (- ms)
[20/Nov/2023:22:12:35 +0100] 0:0:0:0:0:0:0:1 GET /api/countries HTTP/1.1 200 (- ms)
[20/Nov/2023:22:13:01 +0100] 0:0:0:0:0:0:0:1 GET /api/countries HTTP/1.1 200 (- ms)
```

If you want to disable this use:

`server.undertow.accesslog.enabled=false`

##### Generate logbook messages

__Logbook__ is an extensible Java library to enable complete request and response logging for different client- and server-side technologies.

First you need to activate the logbook trace logger level in the `logback.xml` file

```xml
<logger name="org.zalando.logbook" level="trace" />
```

When a request is sent, in the log you will see:

```log
2023-11-20 22:23:22.673 TRACE [ XNIO-20 task-2] org.zalando.logbook.Logbook Incoming Request: fbadb6f7a3f2cd22
Remote: 0:0:0:0:0:0:0:1
GET http://localhost:9191/api/countries HTTP/1.1
accept: application/json
Accept-Encoding: gzip, deflate, br
Accept-Language: en,es-ES;q=0.9,es;q=0.8,ca;q=0.7,pt-PT;q=0.6,pt;q=0.5,fr;q=0.4,und;q=0.3
Connection: keep-alive
Host: localhost:9191
Referer: http://localhost:9191/api/swagger-ui/index.html
sec-ch-ua: "Google Chrome";v="113", "Chromium";v="113", "Not-A.Brand";v="24"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Linux"
Sec-Fetch-Dest: empty
Sec-Fetch-Mode: cors
Sec-Fetch-Site: same-origin
User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36
Hibernate:
	select
		c1_0.id,
		c1_0.created_by,
		c1_0.created_date,
		c1_0.iso,
		c1_0.iso3,
		c1_0.last_modified_by,
		c1_0.last_modified_date,
		c1_0.name,
		c1_0.nice_name,
		c1_0.num_code,
		c1_0.phone_code,
		c1_0.version
	from
		countries c1_0
[2023-11-20 22:23:22.690 TRACE [ XNIO-20 task-2] [org.zalando.logbook.Logbook  Outgoing Response: fbadb6f7a3f2cd22
Duration: 15 ms
HTTP/1.1 200 OK
Connection: keep-alive
Content-Encoding: gzip
Content-Type: application/json; charset=UTF-8
Date: Mon, 20 Nov 2023 21:23:22 GMT
Transfer-Encoding: chunked
Vary: Origin, Access-Control-Request-Method, Access-Control-Request-Headers

{"items":[{"id":1,"name":"Afghanistan","phone_code":93},{"id":2,"name":"Albania","phone_code":355},{"id":3,"name":"Algeria","phone_code":213},{"id":4,"name":"American Samoa","phone_code":1684},...]}

```

For more information about how to use __logbook zalando__ please read the [official documentation](https://github.com/zalando/logbook)

##### Generate auditable messages

There is an annotation you can use to generate centralized messages with a same format always, additionally:
- When a method executes and it's ok, it will print 'Result:OK' automatically otherwise 'Result:KO'
- You can customize the ok and error messages

```java
@Auditable(
	category = "Category for non parameter event",
	code = "001",
	message = "Simple message for audit event, no params")
public ResponseEntity<CountriesResponse> getAllCountries(String acceptLanguage) { ...
```

Will generate:

```log
2023-11-20 22:32:04.121 INFO [  XNIO-2 task-2] [c.d.logging.audit.SimpleEventsLogger -------> 'Category:Category for non parameter event' 'Code:001' 'Result:OK' 'Description:Simple message for audit event, no params' 'Params:{}'
```

If you need to print a failed message when a exception arises you can do:

```java
@Auditable(
	categoryWhenErrorOccurs = "Category error for non parameter event",
	codeWhenErrorOccurs = "ERROR 001",
	messageWhenErrorOccurs = "Simple error message for audit event, no params")
public ResponseEntity<CountriesResponse> getAllCountries(String acceptLanguage) {...
	an exception occurs inside the method
}
```

Will generate:

```log
2023-11-20 22:32:04.121 INFO [  XNIO-2 task-2] [c.d.logging.audit.SimpleEventsLogger -------> 'Category:Category error for non parameter event' 'Code:ERROR 001' 'Result:KO' 'Description:Simple error message for audit event, no params' 'Params:{}'
```

#### Default properties

This properties are present at module level, so you can replace them by project values specifically

| Property | Default value |
| --------- | ---- |
| server.undertow.accesslog.enabled	| **true** |
| server.undertow.accesslog.pattern	| **%t %a %r %s (%D ms)** |
| logbook.filter.enabled				| **true** |
| logbook.secure-filter.enabled		| **true** |
| logbook.include						| **${server.servlet.context-path}\/*\*** |
| logbook.format.style				| **http** |
| app.logging.auditable.annotation.enabled	| **true** |
| app.logging.correlation.filter.url		| **/\*** |

### dkt-fitness-boot-starter-openapi

This module is responsible by features like:
- **Swagger and OpenAPI**: Swagger is an open source set of rules, specifications and tools for developing and describing RESTful APIs. The Swagger framework allows developers to create interactive, machine and human-readable API documentation.
- **Swagger security**: A preconfigured swagger that allows to include JWT and API-Key into the requests from the swagger UI.

#### Libraries included in the module

| Group | Artifact |
| ----- | -------- |
| org.springdoc | springdoc-openapi-starter-webmvc-ui |
| org.openapitools | jackson-databind-nullable |

#### Usage

Simply add the dependency in the project and you will have it working.

```xml
<dependency>
	<groupId>com.decathlon</groupId>
	<artifactId>dkt-fitness-boot-starter-openapi</artifactId>
</dependency>
```

For more information about how to use swagger please read the [official documentation](https://springdoc.org/faq.html)

#### Default properties

This properties are present at module level, so you can replace them by project values specifically

| Property | Default value | Description |
| -------- | ------------- | -----------
| springdoc.api-docs.version								| **openapi-3-0** | The OpenAPI specification version |
| springdoc.version										| **1.0** | The documentation version, will be visible in the UI |
| springdoc.api-title										| **API rest for service** | The title for the swagger page |
| springdoc.cache.disabled								| **true** | Sometimes the same swagger-ui is served behind internal and external proxies. some users want the server URL, to be computed on each http request. |
| springdoc.swagger-ui.disable-swagger-default-url	| **true** | Disable the default swagger petstore URL |
| app.open-api-bean.enabled								| **true** | Enable the security schemas for the UI Page, in order to inject JWT and API Key
| app.swagger.server-paths								| **http://localhost:8888/set-this-property-correctly/app.swagger.server-path** | Property to be replaced for the servers where the API is deployed, accept values separated by commas.

### dkt-fitness-boot-starter-rest-components

REST module with multiple preconfigured properties for use in building microservices, e.g.:
- Correct global configuration for the format of __dates__ in parameters and body of JSON bodies, in addition to the preconfiguration of the serializers to cover all the points where dates are indicated and to be able to homogenize the formats, very flexible
- Correct configuration of __CORS__ and the possibility of establishing __“origins” and “methods”__ through properties
- Preconfiguring json using __SNAKE_CASE__ for json bodies
- Preconfiguration of components for translation, using the __ACCEPT-LANGUAGE__ header and automatic translation.
- Language and default language settings
- Multiple utility classes for different operations such as:
	- Convert dates from string to preconfigured date formats and vice versa.
	- Configuration to handle multipart files
- __DefaultResponseErrorHandler__ for Resttemplate extensible to depending on a code coming in an error, throw a particular exception
- Preconfigured properties for homogeneous behavior of all the MS built with the BOM of this architecture e.g. (all with the possibility of being rewritten by the micros -- Flexibility 💪
- __Server compression__ enabled by default
- __Multipart file size__ preconfigured
- Multiple actuator endpoints visible

#### Libraries included in the module

| Group | Artifact |
| ----- | -------- |
| org.springframework.boot	| spring-boot-starter-json |
| org.springframework.boot	| spring-boot-starter-validation |
| org.springframework.boot	| spring-boot-starter-actuator |
| org.apache.commons			| commons-lang3 |
| org.glassfish.jaxb 			| jaxb-runtime |
| org.zalando					| problem-spring-web |
| org.zalando 					| jackson-datatype-problem |
| org.openapitools 			| jackson-databind-nullable |

#### Usage

Simply add the dependency in the project and you will have it working.

```xml
<dependency>
	<groupId>com.decathlon</groupId>
	<artifactId>dkt-fitness-boot-starter-rest-components</artifactId>
</dependency>
```

##### Api Endpoint

There is a `ping endpoint` defined in `http(s)://\<server\>/\<context\>/v1/ping`

Note: this endpoint is exactly produced by the **Actuator Ping endpoint**

##### Cors configuration

The cors is configured under the properties in `application.properties`:

```properties
- app.cors.path=/**
- app.cors.allowed-origins=*
- app.cors.allowed-headers=*
- app.cors.allowed-methods=*
```

##### Content negotiation parameter

Whether a request parameter ("format" by default) should be used to determine the requested media type. For this option to work you must register media type mappings. eg

If you call an endpoint with the format parameter:

`/api/v1/types-handler?param=text/plain`

It will respond with `text/plain` content header, see below:

```java
@Test
void ok_for_text_plain_format_parameter() throws Exception {
	MvcResult textPlainResult =
			mockMvc.perform(
							get("/api/v1/types-handler")
									.param("format", MediaType.TEXT_PLAIN_VALUE))
					.andDo(print())
					.andExpect(header().string("Content-Type", "text/plain;charset=UTF-8"))
					.andReturn();
	assertEquals("Prueba de string", textPlainResult.getResponse().getContentAsString());
}
```

##### Centralized dates configuration

All the dates for:

- URL query parameters
- Json body

The date types configured are for those declared as:

- `java.time.LocalDate`
- `java.time.LocalDateTime`
- `java.util.Date`

Will be parsed using the configuration defined in `application.properties`:

```properties
app.dates.date-time-format=yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
app.dates.date-format=dd/MM/yyyy
```

Also, the format used to write back the json fields that use dates, will be the ones previously configured

###### `DateUtils` utility class

There is an __utility class__ to parse dates programatically name `DateUtils` that you can inject anywhere:

```java
String dateToString(Date date);

Date stringToDate(String date) throws ParseException;

String localDateToString(LocalDate date);

LocalDate stringToLocalDate(String date);

String localDateTimeToString(LocalDateTime date);

LocalDateTime stringToLocalDateTime(String date) throws ParseException;
```

##### LocaleResolver for `Accept-Language` header

Under the folder i18n where are defined the messages for translation:
- messages_ca.properties
- messages_en.properties
- messages_es.properties
- messages_fr.properties
- messages_pt.properties
- messages.properties

With the properties defined in `application.properties` you can define how many languages you will have and the one that is the language by default

```properties
app.locales.default-locale=es
app.locales.supported-locales=es,fr,en,pt,ca
```

###### `Translator` component

There is a component called `Translator` that can be injected anywhere and with the api:

```java
String toLocale(String msg);

String toLocale(String msg, Locale locale);

String toLocale(String msg, Object[] args);

String toLocale(String msg, Object[] args, Locale locale);
```

This component will take care of translate the message in the language sent in the `Accept-Language` header without the need to capture it in the request, a test can clarify how to use it:

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/language-test")
public class TranslationRestController {

	private final Translator translator;

	@GetMapping
	public ResponseEntity<KeyValueResponseDto> languageHeaderResponse() {
		return new ResponseEntity<>(
				new KeyValueResponseDto(translator.toLocale("msg.translated")), HttpStatus.OK);
	}
}
```

```java
@Test
void when_locale_es() throws Exception {
	mockMvc.perform(
					get("/api/v1/language-test")
							.characterEncoding("utf-8")
							.header("Accept-Language", "es"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.value", is("Mensaje traducido")));
}

@Test
void when_locale_en() throws Exception {
	mockMvc.perform(
					get("/api/v1/language-test")
							.characterEncoding("utf-8")
							.header("Accept-Language", "en"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.value", is("Message translated")));
}
```
##### Handlers for RestTemplate component

If you need to do calls to others endpoints using `RestTemplate` you can configure it to:

- Pass the `Accept-Language` header in the call (`LocaleHeaderInterceptor` class)
- Attach a `ResponseErrorHandler` to parse and throw `LogicException` (`CustomResponseErrorHandler` class)

A high performance configuration for a RestTemplate could be like this:

```java
@Configuration
public class RestTemplateConfiguration {

	@Bean
	RestTemplate restTemplate(
			MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
		RestTemplate restTemplate = new RestTemplate();

		restTemplate.setErrorHandler(
				new CustomResponseErrorHandler(
						mappingJackson2HttpMessageConverter.getObjectMapper()));
		restTemplate.getInterceptors().add(new LocaleHeaderInterceptor());
		restTemplate.setRequestFactory(
				new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

		return restTemplate;
	}
}

```

#### Default properties

This properties are present at module level, so you can replace them by project values specifically

| Property | Default value | Description |
| -------- | ------------- | -----------
| server.compression.enabled								| **true**	| Whether response compression is enabled. |
| server.servlet.context-path							| **/**	| Context path of the application. |
| spring.main.lazy-initialization						| **true** | Whether initialization should be performed lazily. |
| spring.servlet.multipart.enabled						| **true** | Whether to enable support of multipart uploads. |
| spring.servlet.multipart.max-file-size				| **10MB** | Max file size. |
| spring.servlet.multipart.max-request-size			| **11MB** | Max request size. |
| spring.web.resources.add-mappings						| **false** | Whether to enable default resource handling. |
| spring.mvc.throw-exception-if-no-handler-found		| **true** | Whether a "NoHandlerFoundException" should be thrown if no Handler was found to process a request. |
| server.servlet.encoding.force							| **true** | Whether to force the encoding to the configured charset on HTTP requests and responses. |
| management.endpoints.web.exposure.include			| **\*** | Endpoint IDs that should be included or '*' for all. |
| management.endpoint.health.show-details				| **always** | When to show full health details. |
| management.endpoint.health.probes.enabled			| **true** | Whether to enable liveness and readiness probes. |
| management.info.env.enabled							| **true** | Whether to enable environment info. |
| spring.jackson.property-naming-strategy				| com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy | Set json bodies to use snake_case properties naming |
| spring.jackson.default-property-inclusion			| non-null		| Only retrieve json with non-null properties
| spring.jackson.serialization.write-dates-as-timestamps	| false		| Must be deactivated in order to use the dates |
| spring.jackson.date-format | ${app.dates.date-time-format}		| set the format for json date properties |
| app.dates.date-time-format								| **yyyy-MM-dd'T'HH:mm:ss.SSS'Z'** | The date time format to use in json bodies and request params |
| app.dates.date-format									| **dd/MM/yyyy** | The date format to use in json bodies and request params |
| app.cors.path											| __\/\*\*__ | Cors applied in the established path |
| app.cors.allowed-origins								| __*__ | Origins allowed '*' for all. |
| app.cors.allowed-headers								| __*__ | Headers allowed '*' for all. |
| app.cors.allowed-methods								| __*__ | Methods allowed '*' for all. |
| app.locales.default-locale								| **es** | Default language to use |
| app.locales.supported-locales							| **es,fr,en,pt,ca** | Set of languages allowed |
