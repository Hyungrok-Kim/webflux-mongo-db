package myspringboot.reactive.mongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto
{
    @Id
    private String id;
    private String name;
    private int qty;
    private double price;

    public double getTotalprice()
    {
        double tPrice = qty * price;
        return Math.round(tPrice * 100) / 100.0;
    }
}
