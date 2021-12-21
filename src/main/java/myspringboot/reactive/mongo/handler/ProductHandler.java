package myspringboot.reactive.mongo.handler;

import lombok.RequiredArgsConstructor;
import myspringboot.reactive.mongo.dto.ProductDto;
import myspringboot.reactive.mongo.repository.ProductRepository;
import myspringboot.reactive.mongo.utils.AppUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}
