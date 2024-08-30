package com.bolsadeideas.springboot.reactor.spring_boot_reactor;

import com.bolsadeideas.springboot.reactor.spring_boot_reactor.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	//TODO: DOCUMENTACION MONGO REACTIVO PARA JAVA https://docs.spring.io/spring-data/mongodb/reference/mongodb/getting-started.html

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		convertToString();

	}

	public void convertToString() throws Exception {

		List<Usuario> listUsuarios = new ArrayList<>();
		listUsuarios.add(new Usuario("Andres", "sanchez"));
		listUsuarios.add(new Usuario("Pedro","Cortes"));
		listUsuarios.add(new Usuario("Diego","San"));
		listUsuarios.add(new Usuario("Juan","Carrasco"));
		listUsuarios.add(new Usuario("Paco","Perico"));
		listUsuarios.add(new Usuario("Bruce","Lee"));
		listUsuarios.add(new Usuario("Bruce","Willy"));

		Flux.fromIterable(listUsuarios)
				.map(usuario -> usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase()))
				.flatMap(usuario -> {
					if (usuario.contains("bruce".toUpperCase())){
						return Mono.just(usuario);
					}else{
						return Mono.empty();
					}
				})
				.map(nombre -> {
					return nombre.toLowerCase();
				}).subscribe(u -> log.info(u.toString()));
	}



	public void ejemploFlatmap() throws Exception {

		List<String> listUsuarios = new ArrayList<>();
		listUsuarios.add("Andres sanchez");
		listUsuarios.add("Pedro Cortes");
		listUsuarios.add("Diego San");
		listUsuarios.add("Juan Carrasco");
		listUsuarios.add("Paco Perico");
		listUsuarios.add("Bruce Lee");
		listUsuarios.add("Bruce Willy");

		Flux.fromIterable(listUsuarios)
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {
					if (usuario.getNombre().equalsIgnoreCase("bruce")){
						return Mono.just(usuario);
					}else{
						return Mono.empty();
					}
				})
				.map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(usuario.getNombre().concat(" ".concat(usuario.getApellido())));
					return usuario;
				}).subscribe(u -> log.info(u.getNombre()));
	}

	public void ejemploIterable() throws Exception {

		List<String> listUsuarios = new ArrayList<>();
		listUsuarios.add("Andres sanchez");
		listUsuarios.add("Pedro Cortes");
		listUsuarios.add("Diego San");
		listUsuarios.add("Juan Carrasco");
		listUsuarios.add("Paco Perico");
		listUsuarios.add("Bruce Lee");
		listUsuarios.add("Bruce Willy");

		Flux<String> nombres = Flux.fromIterable(listUsuarios);


		// Flux<String> nombres2 = Flux.just("Andres sanchez", "Pedro Cortes", "Diego San", "Juan Carrasco", "Paco Perico", "Bruce Lee", "Bruce Willy");

		Flux<Usuario> usuarios = nombres
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
		usuarios.subscribe(e -> log.info(e.getNombre()),
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
