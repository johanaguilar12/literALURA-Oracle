package com.alura.literALURA.principal;


import com.alura.literALURA.model.*;
import com.alura.literALURA.repository.repo;
import com.alura.literALURA.service.ConsumoAPI;
import com.alura.literALURA.service.ConvierteDatos;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.System.exit;

public class principal {
    private Scanner teclado = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDato = new ConvierteDatos();
    private repo repositorio;

    public principal (repo reposity){
        this.repositorio=reposity;
    }

    public void mostrarMenu(){
        var opcion = -1;
        while (opcion != 0){

            var menu = """
                    LITERATURA ALURA 📖
                    Elija la opción:
                    1- Buscar libro por título
                    2- Mostrar libros registrados
                    3- Mostrar autores registrados
                    4- Mostrar autores vivos en determinado año
                    5- Mostrar libros por idioma
                    6- Los 5 libros con más descargas
                    7- Estadisticas
                    
                    0- Salir
                    
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarLibroWebPrincipal();
                    break;
                case 2:
                    mostrarLibrosConsola();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    mostrarAutoresPorAnio();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 6:
                    top5LibrosDescargados();
                    break;
                case 7:
                    estadisticasGenerales();
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void estadisticasGenerales() {
        List<Libro> listaDeLibros = repositorio.findAll();
        DoubleSummaryStatistics est = listaDeLibros.stream().collect(Collectors.summarizingDouble(Libro::getNumeroDeDescargas));
        System.out.println("Cantidad de libros: " + est.getCount());
        System.out.println("Mínimo de descargas: " + est.getMin());
        System.out.println("Maximo de descargas: " + est.getMax());
        System.out.println("Promedio de descargas: " + est.getAverage() + "\n");
    }

    private void top5LibrosDescargados() {
        List<Libro> litaTop5 = repositorio.findTop5ByOrderByNumeroDeDescargasDesc();
        System.out.println("+++++++++ TOP 5 DESCARGAS +++++++++");
        litaTop5.forEach(l -> System.out.println(
                "Libro: " + l.getTitulo() + " | Descargas: " + l.getNumeroDeDescargas()));
        System.out.println();
    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("""
                Escriba el idioma del libro:
                ES: Español
                EN: Ingles
                FR: Frances
                IT: Italiano
                PT: Portugues
                """);

        var idiomaSelecionado = teclado.nextLine();

        try {
            List<Libro> libroPorIdioma = repositorio.findByIdiomas(Idioma.valueOf(idiomaSelecionado.toUpperCase()));
            libroPorIdioma.forEach(n -> System.out.println(
                            "Titulo: " + n.getTitulo() +
                            "\nIdioma: " + n.getIdiomas() +
                            "\nAutor: " + n.getAutor().stream().map(Autor::getNombre).collect(Collectors.joining()) +
                            "\nCantidad de descargas: " + n.getNumeroDeDescargas() +
                            "\n"
            ));
        } catch (IllegalArgumentException e){
            System.out.println("Idioma no disponible\n");
        }

    }

    private void mostrarAutoresPorAnio() {
        System.out.println("Ingresa el año:");
        String anio = teclado.nextLine();

        List<Autor> autoresVivos = repositorio.mostrarAutoresVivos(anio);

        if (autoresVivos.isEmpty()){
            System.out.println("Sin autores...\n");
            return;
        }

        Map<String, List<String>> autoresConLibros = autoresVivos.stream()
                .collect(Collectors.groupingBy(
                        Autor::getNombre,
                        Collectors.mapping(a -> a.getLibro().getTitulo(), Collectors.toList())
                ));

        autoresConLibros.forEach((nombre, libros) -> {
            Autor autor = autoresVivos.stream()
                    .filter(a -> a.getNombre().equals(nombre))
                    .findFirst().orElse(null);
            if (autor != null) {
                System.out.println("Nombre: " + nombre);
                System.out.println("Fecha de nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de muerte: " + autor.getFechadeMuerte());
                System.out.println("Libros: " + libros + "\n");
            }
        });
    }

    private void mostrarLibrosConsola() {
        List<Libro> mostrarListaLibros = repositorio.findAll();
        mostrarListaLibros.forEach(l -> System.out.println(
                        "Título: " + l.getTitulo()+
                        "\nIdioma: " + l.getIdiomas()+
                        "\nAutor: " + l.getAutor().stream().map(Autor::getNombre).collect(Collectors.joining()) +
                        "\nCantidad de descargas: " + l.getNumeroDeDescargas() +
                        "\n"
        ));
    }

    private void mostrarAutores(){
        List<Autor> mostarListaAutores = repositorio.mostrarAutores();

        Map<String, List<String>> autoresConLibros = mostarListaAutores.stream()
                .collect(Collectors.groupingBy(
                        Autor::getNombre,
                        Collectors.mapping(a -> a.getLibro().getTitulo(), Collectors.toList())
                ));

        autoresConLibros.forEach((nombre, libros) -> {
            Autor autor = mostarListaAutores.stream()
                    .filter(a -> a.getNombre().equals(nombre))
                    .findFirst().orElse(null);
            if (autor != null) {
                System.out.println("Nombre: " + nombre);
                System.out.println("Fecha de nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de muerte: " + autor.getFechadeMuerte());
                System.out.println("Libros: " + libros + "\n");
            }
        });
    }

    // Buscar libro en la API
    private DatosLibros buscarLibroWeb(){
        System.out.println("Ingresa el nombre del libro: ");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = convierteDato.obtenerDatos(json, DatosGeneral.class);

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()){
            System.out.println("Libro encontrado...");
            return libroBuscado.get();
        } else {
            System.out.println("libro no encontrado, intenta con otro título\n");
            return null;
        }
    }

    private void buscarLibroWebPrincipal(){
        Optional<DatosLibros> datosOpcional = Optional.ofNullable(buscarLibroWeb());

        if(datosOpcional.isPresent()) {
            DatosLibros datos = datosOpcional.get();

            Libro libro = new Libro(datos);
            List<Autor> autores = new ArrayList<>();
            for (DatosAutor datosAutor : datos.listaAutor()) {
                Autor autor = new Autor(datosAutor);
                autor.setLibro(libro);
                autores.add(autor);
            }
            libro.setAutor(autores);
            try {
                repositorio.save(libro);
                System.out.println(libro.getTitulo() + " Guardado");
            } catch (DataIntegrityViolationException e) {
                System.out.println("Error. Ya existe el libro.\n");
            }
        }
    }
}

