# Fitness (dkt-fitness-libs)

## _Motivation_

![Alt text](https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/Decathlon_Logo.svg/320px-Decathlon_Logo.svg.png?sanitize=true)

**Fitness** is an evolution from **fitness** (v2 and v3) inspired in a hexagonal architecture and spring starters architecture modules for creating microservices

## Features

- Migration to Spring boot 2.7.x (https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
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
- Coverage over 70%
- Uses undertow as Non-blocking IO server (for better performance)
- Automatic generation of entities from database definition with Hibernate-tools (no need to build entities by hand anymore)

## Upgrading to version

Fitness has the same modules and libraries than fitness (v2,v3) and add a lot more with more self explanatory nomenclature and packaging model based on starters. The following table explains the main modules from fitness to fitness 3

### Examples

#### Added dependencies directly into api

Before, the lombok dependency was coming transitively by parent projects, but the good practice says, that eg.

1. Lombok now must be present in every project that need it

```java
	<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<scope>provided</scope>
	</dependency>
```

#### RestTemplate calls

Definition of the **RestTemplate** in the ms to use, setting the **error handlers, converters and interceptors** needed

There is no need to wrap the **RestTemplate** functionality as we loose many of the features to use inline when needed, instead, define as many **RestTemplates** as you need, with different configurations in singleton scope.

```java
@Configuration
public class RestTemplatesConfiguration {

	@Bean
	public RestTemplate restTemplate(
			MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter,
			ClientHttpRequestFactory clientHttpRequestFactory,
			Logbook logbook) {
		RestTemplate restTemplate = new RestTemplate();

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		//        messageConverters.add(new FormHttpMessageConverter());
		//        messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(mappingJackson2HttpMessageConverter);

		restTemplate.setRequestFactory(clientHttpRequestFactory);
		restTemplate.setErrorHandler(
				new CustomResponseErrorHandler(
						mappingJackson2HttpMessageConverter.getObjectMapper()));
		restTemplate.setMessageConverters(messageConverters);
		restTemplate.getInterceptors().add(new LocaleHeaderInterceptor());
		restTemplate.getInterceptors().add(new AuthorizationHeaderInterceptor());
		restTemplate.getInterceptors().add(new LogbookClientHttpRequestInterceptor(logbook));

		return restTemplate;
	}
}
```

Using it in any module

```java
private static final String PREFIX = "/api/v1/persons";

@Autowired // This way only for this example, always use constructor autowiring
private RestTemplate restTemplate;

private UriComponentsBuilder buildUriComponent() {
	return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(serverPort);
}

private methodUsingRestemplate() {
	ResponseEntity<PersonDto> savedPersonResponse =
				restTemplate.exchange(
						buildUriComponent().path(PREFIX).build().toUriString(),
						HttpMethod.POST,
						new HttpEntity<>(personDto),
						PersonDto.class);
}
```

Never define a handler exception for Resttemplate outside it

**Bad Practice**

```java
final ResponseEntity<S> response = restTemplate.exchange(api.getUri(), api.getHttpMethod(),
		new HttpEntity<>(body, getHeadrs(headersParams)), responseType);

if (!response.getStatusCode().equals(HttpStatus.OK)) {
	throw new FitnessException("API call error with status: " + response.getStatusCode());
}
```

instead of it, create a **CustomResponseErrorHandler** as **ErrorHandler** in the **RestTemplate configuration** (as shown in the **RestTemplatesConfiguration** class)

#### Never use a unique global exception for everything

Instead, you can use it as a base Exception but then define as many exceptions as you need

#### Avoid to use try/catch in controllers

**Bad Practice**

```java
@GetMapping("/registration")
public Request<ListRequest> getRegistration(
		@RequestParam(name = "registrationName", required = true) final String registrationName) {
	try {
		return request(new ListRequest<>(regService.getRegistration(registrationName)));
	} catch (final Exception e) {       --> This has the inconvenience you can forgot to define
		return error(e);
	}
}
```

**Good practice** define a **@RestControllerAdvice** as centralized exception handler point

```java
@RestControllerAdvice
public class DefaultRestExceptionHandlerController implements ProblemHandling {

	@ExceptionHandler(LogicException.class) --> Define as many exceptions you want
	public ResponseEntity<ErrorResponse> exceptionHandler(LogicException ex) {
		// Build a response for this exception
	}

}
```

And the previous controller method will remains as:

```java
@GetMapping("/registration")
public Request<ListRequest> getRegistration(
		@RequestParam(name = "registrationName", required = true) final String registrationName) {
	return request(new ListRequest<>(regService.getRegistration(registrationName)));
}
```

## Installation

Fitness requires:
- **JDK17+**
- Docker (when compile and test)

For maintenance and testing in local
```sh
mvn spotless:check verify
```

## Next in documentation

### How to write a CRUD controller using fitness-crud-components module

### How to generate @Entities from Database definition

Text to force diff
