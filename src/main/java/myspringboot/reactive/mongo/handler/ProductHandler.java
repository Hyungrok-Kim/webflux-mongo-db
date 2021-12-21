package myspringboot.reactive.mongo.handler;

import lombok.RequiredArgsConstructor;
import myspringboot.reactive.mongo.dto.ProductDto;
import myspringboot.reactive.mongo.repository.ProductRepository;
import myspringboot.reactive.mongo.utils.AppUtils;
import org.springframework.data.domain.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Service
@RequiredArgsConstructor
public class ProductHandler
{
    private final ProductRepository repository;

    private Mono<ServerResponse> response404 = ServerResponse.notFound().build();
    private Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    public Mono<ServerResponse> getProducts(ServerRequest request)
    {
        Flux<ProductDto> productDtoFlux = repository.findAll().map(AppUtils::entityToDto);
        return ServerResponse.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(productDtoFlux, ProductDto.class);
    }

    public Mono<ServerResponse> getProduct(ServerRequest request)
    {
        String id = request.pathVariable("id");
        return repository.findById(id)  // Mono<Product>
                         .map(AppUtils::entityToDto) // Mono<ProductDto>
                         .flatMap(productDto -> ServerResponse.ok()
                                                            .contentType(MediaType.APPLICATION_JSON)
                                                            .body(fromValue(productDto))
                         )  // Mono<ServerResponse>
                         .switchIfEmpty(response404);
    }

    public Mono<ServerResponse> getProductInRange(ServerRequest request)
    {
        double min = Double.parseDouble(request.queryParam("min").orElseGet(() -> Double.toString(Double.MIN_VALUE)));
        System.out.println("min = " + min);
        double max = Double.parseDouble(request.queryParam("max").orElseGet(() -> Double.toString(Double.MAX_VALUE)));
        System.out.println("max = " + max);

        Flux<ProductDto> productDtoFlux = repository.findByPriceBetween(Range.closed(min, max)).map(AppUtils::entityToDto);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                  .body(productDtoFlux, productDto.class)
                                  .switchIfEmpty(response404);
    }
}
