package com.bolsadeideas.springboot.reactor.spring_boot_reactor;

import com.bolsadeideas.springboot.reactor.spring_boot_reactor.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Usuario> nombres = Flux.just("Andres sanchez", "Pedro Cortes", "Diego San", "Juan Carrasco", "Paco Perico", "Bruce Lee", "Bruce Willy")
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().toLowerCase().equals("bruce"))
				.doOnNext(usuario -> {
					if(usuario == null){
						throw new RuntimeException("Nombres no puede ser vacíos");
					}
					System.out.println(usuario);
				}).map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(usuario.getNombre().concat(" ".concat(usuario.getApellido())));
					return usuario;
				});


		// Subscribe
		nombres.subscribe(e -> log.info(e.getNombre()),
				error -> log.error(error.getMessage()),
				//Multitarea que se ejecuta en paralelo
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecución del observable con éxito");
					}
				});
	}
}
