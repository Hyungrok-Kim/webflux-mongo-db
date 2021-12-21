package myspringboot.reactive.mongo.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LogFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        log.info("Serving '{}'", path);

        return chain.filter(exchange).doAfterTerminate(() -> {
            //exchange.getResponse() => ServerHttpResponse
            //exchange.getResponse().getHeaders() => HttpHeaders (Map)
            //exchange.getResponse().getHeaders().entrySet() => Set<Map.Entry<String, List<String>>>
                    exchange.getResponse().getHeaders().entrySet().forEach(e ->
                            log.info("Response Headers '{}' : '{}'",e.getKey(),e.getValue()));
                    log.info("Served '{}' as {} in {} ms",
                            path,
                            exchange.getResponse().getStatusCode(),
                            System.currentTimeMillis() - startTime);
                }
        );
    }
}