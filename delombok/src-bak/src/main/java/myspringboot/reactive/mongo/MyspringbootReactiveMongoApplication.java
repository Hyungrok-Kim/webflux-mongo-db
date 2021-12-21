package myspringboot.reactive.mongo;

import myspringboot.reactive.mongo.entity.Product;
import myspringboot.reactive.mongo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class MyspringbootReactiveMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyspringbootReactiveMongoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ProductRepository repository) {
		return args -> {
			Mono<Void> voidMono = repository.deleteAll();
			voidMono.doOnSuccess(x -> System.out.println("Delete Ok!"))
					.subscribe();

			Flux<Product> productFlux = Flux.just(
					new Product(null, "Big Latte",10, 2.99),
					new Product(null, "Big Decaf",20, 2.49),
					new Product(null, "Green Tea",15, 1.99))
					.flatMap(repository::save);
			//.flatMap(flux -> repository.save(flux));

			productFlux
					.thenMany(repository.findAll())
					.subscribe(System.out::println);
		};
	}

}