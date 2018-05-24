package com.philspelman.beer_three;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class BeerThreeApplication {

    public static void main(String[] args) {
        SpringApplication
                .run(BeerThreeApplication.class, args);

    }


}




@Entity
class Beer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Beer() {
    }

    public Beer(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}

//@Repository
@RepositoryRestResource
interface BeerRepository extends JpaRepository<Beer, Long> {
}

@Component
class BeerCommandLineRunner implements CommandLineRunner {

    private final BeerRepository repository;

    public BeerCommandLineRunner(BeerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {


        Stream.of("Kentucky Brunch Brand Stout",
                "Good Morning",
                "Very Hazy",
                "King Julius",
                "Budweiser",
                "Coors Light",
                "PBR").forEach(name -> repository.save(new Beer(name))
        );
        repository.findAll().forEach(System.out::println);

    }
}


@RestController
@CrossOrigin(origins = {"http://localhost:3000","localhost:3000", "http://127.0.0.1:3000", "127.0.0.1:3000", "http://localhost:5000"})
class BeerController {
    private BeerRepository repository;

    public BeerController(BeerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/good-beers")
    public Collection<Beer> goodBeers() {
        return repository.findAll().stream()
                .filter(this::isGreat)
                .collect(Collectors.toList());
    }

    private boolean isGreat(Beer beer) {
        return !beer.getName().equals("Budweiser") &&
                !beer.getName().equals("Coors Light") &&
                !beer.getName().equals("PBR");
    }

}


@EnableWebMvc
class corsConfigurer {
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*").allowedOrigins("http://localhost:3000");
        registry.addMapping("/*").allowedOrigins("localhost:3000");
        registry.addMapping("/*").allowedOrigins("http://127.0.0.1:3000");
        registry.addMapping("/*").allowedOrigins("127.0.0.1:3000");
    }
}
